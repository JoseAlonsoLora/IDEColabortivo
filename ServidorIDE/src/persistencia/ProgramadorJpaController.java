/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import persistencia.exceptions.NonexistentEntityException;
import persistencia.exceptions.PreexistingEntityException;

/**
 *
 * @author raymu
 */
public class ProgramadorJpaController implements Serializable {

    public ProgramadorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Programador programador) throws PreexistingEntityException, Exception {
        if (programador.getDesarrollaCollection() == null) {
            programador.setDesarrollaCollection(new ArrayList<Desarrolla>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Desarrolla> attachedDesarrollaCollection = new ArrayList<Desarrolla>();
            for (Desarrolla desarrollaCollectionDesarrollaToAttach : programador.getDesarrollaCollection()) {
                desarrollaCollectionDesarrollaToAttach = em.getReference(desarrollaCollectionDesarrollaToAttach.getClass(), desarrollaCollectionDesarrollaToAttach.getIdDesarrolla());
                attachedDesarrollaCollection.add(desarrollaCollectionDesarrollaToAttach);
            }
            programador.setDesarrollaCollection(attachedDesarrollaCollection);
            em.persist(programador);
            for (Desarrolla desarrollaCollectionDesarrolla : programador.getDesarrollaCollection()) {
                Programador oldNombreUsuarioOfDesarrollaCollectionDesarrolla = desarrollaCollectionDesarrolla.getNombreUsuario();
                desarrollaCollectionDesarrolla.setNombreUsuario(programador);
                desarrollaCollectionDesarrolla = em.merge(desarrollaCollectionDesarrolla);
                if (oldNombreUsuarioOfDesarrollaCollectionDesarrolla != null) {
                    oldNombreUsuarioOfDesarrollaCollectionDesarrolla.getDesarrollaCollection().remove(desarrollaCollectionDesarrolla);
                    oldNombreUsuarioOfDesarrollaCollectionDesarrolla = em.merge(oldNombreUsuarioOfDesarrollaCollectionDesarrolla);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProgramador(programador.getNombreUsuario()) != null) {
                throw new PreexistingEntityException("Programador " + programador + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Programador programador) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Programador persistentProgramador = em.find(Programador.class, programador.getNombreUsuario());
            Collection<Desarrolla> desarrollaCollectionOld = persistentProgramador.getDesarrollaCollection();
            Collection<Desarrolla> desarrollaCollectionNew = programador.getDesarrollaCollection();
            Collection<Desarrolla> attachedDesarrollaCollectionNew = new ArrayList<Desarrolla>();
            for (Desarrolla desarrollaCollectionNewDesarrollaToAttach : desarrollaCollectionNew) {
                desarrollaCollectionNewDesarrollaToAttach = em.getReference(desarrollaCollectionNewDesarrollaToAttach.getClass(), desarrollaCollectionNewDesarrollaToAttach.getIdDesarrolla());
                attachedDesarrollaCollectionNew.add(desarrollaCollectionNewDesarrollaToAttach);
            }
            desarrollaCollectionNew = attachedDesarrollaCollectionNew;
            programador.setDesarrollaCollection(desarrollaCollectionNew);
            programador = em.merge(programador);
            for (Desarrolla desarrollaCollectionOldDesarrolla : desarrollaCollectionOld) {
                if (!desarrollaCollectionNew.contains(desarrollaCollectionOldDesarrolla)) {
                    desarrollaCollectionOldDesarrolla.setNombreUsuario(null);
                    desarrollaCollectionOldDesarrolla = em.merge(desarrollaCollectionOldDesarrolla);
                }
            }
            for (Desarrolla desarrollaCollectionNewDesarrolla : desarrollaCollectionNew) {
                if (!desarrollaCollectionOld.contains(desarrollaCollectionNewDesarrolla)) {
                    Programador oldNombreUsuarioOfDesarrollaCollectionNewDesarrolla = desarrollaCollectionNewDesarrolla.getNombreUsuario();
                    desarrollaCollectionNewDesarrolla.setNombreUsuario(programador);
                    desarrollaCollectionNewDesarrolla = em.merge(desarrollaCollectionNewDesarrolla);
                    if (oldNombreUsuarioOfDesarrollaCollectionNewDesarrolla != null && !oldNombreUsuarioOfDesarrollaCollectionNewDesarrolla.equals(programador)) {
                        oldNombreUsuarioOfDesarrollaCollectionNewDesarrolla.getDesarrollaCollection().remove(desarrollaCollectionNewDesarrolla);
                        oldNombreUsuarioOfDesarrollaCollectionNewDesarrolla = em.merge(oldNombreUsuarioOfDesarrollaCollectionNewDesarrolla);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = programador.getNombreUsuario();
                if (findProgramador(id) == null) {
                    throw new NonexistentEntityException("The programador with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Programador programador;
            try {
                programador = em.getReference(Programador.class, id);
                programador.getNombreUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The programador with id " + id + " no longer exists.", enfe);
            }
            Collection<Desarrolla> desarrollaCollection = programador.getDesarrollaCollection();
            for (Desarrolla desarrollaCollectionDesarrolla : desarrollaCollection) {
                desarrollaCollectionDesarrolla.setNombreUsuario(null);
                desarrollaCollectionDesarrolla = em.merge(desarrollaCollectionDesarrolla);
            }
            em.remove(programador);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Programador> findProgramadorEntities() {
        return findProgramadorEntities(true, -1, -1);
    }

    public List<Programador> findProgramadorEntities(int maxResults, int firstResult) {
        return findProgramadorEntities(false, maxResults, firstResult);
    }

    private List<Programador> findProgramadorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Programador.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Programador findProgramador(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Programador.class, id);
        } finally {
            em.close();
        }
    }

    public int getProgramadorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Programador> rt = cq.from(Programador.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
