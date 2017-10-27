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

    public void create(Programador programador) {
        if (programador.getDesarrollaCollection() == null) {
            programador.setDesarrollaCollection(new ArrayList<Desarrolla>());
        }
        if (programador.getCuentaCollection() == null) {
            programador.setCuentaCollection(new ArrayList<Cuenta>());
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
            Collection<Cuenta> attachedCuentaCollection = new ArrayList<Cuenta>();
            for (Cuenta cuentaCollectionCuentaToAttach : programador.getCuentaCollection()) {
                cuentaCollectionCuentaToAttach = em.getReference(cuentaCollectionCuentaToAttach.getClass(), cuentaCollectionCuentaToAttach.getNombreUsuario());
                attachedCuentaCollection.add(cuentaCollectionCuentaToAttach);
            }
            programador.setCuentaCollection(attachedCuentaCollection);
            em.persist(programador);
            for (Desarrolla desarrollaCollectionDesarrolla : programador.getDesarrollaCollection()) {
                Programador oldIdUsuarioOfDesarrollaCollectionDesarrolla = desarrollaCollectionDesarrolla.getIdUsuario();
                desarrollaCollectionDesarrolla.setIdUsuario(programador);
                desarrollaCollectionDesarrolla = em.merge(desarrollaCollectionDesarrolla);
                if (oldIdUsuarioOfDesarrollaCollectionDesarrolla != null) {
                    oldIdUsuarioOfDesarrollaCollectionDesarrolla.getDesarrollaCollection().remove(desarrollaCollectionDesarrolla);
                    oldIdUsuarioOfDesarrollaCollectionDesarrolla = em.merge(oldIdUsuarioOfDesarrollaCollectionDesarrolla);
                }
            }
            for (Cuenta cuentaCollectionCuenta : programador.getCuentaCollection()) {
                Programador oldIdUsuarioOfCuentaCollectionCuenta = cuentaCollectionCuenta.getIdUsuario();
                cuentaCollectionCuenta.setIdUsuario(programador);
                cuentaCollectionCuenta = em.merge(cuentaCollectionCuenta);
                if (oldIdUsuarioOfCuentaCollectionCuenta != null) {
                    oldIdUsuarioOfCuentaCollectionCuenta.getCuentaCollection().remove(cuentaCollectionCuenta);
                    oldIdUsuarioOfCuentaCollectionCuenta = em.merge(oldIdUsuarioOfCuentaCollectionCuenta);
                }
            }
            em.getTransaction().commit();
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
            Programador persistentProgramador = em.find(Programador.class, programador.getIdUsuario());
            Collection<Desarrolla> desarrollaCollectionOld = persistentProgramador.getDesarrollaCollection();
            Collection<Desarrolla> desarrollaCollectionNew = programador.getDesarrollaCollection();
            Collection<Cuenta> cuentaCollectionOld = persistentProgramador.getCuentaCollection();
            Collection<Cuenta> cuentaCollectionNew = programador.getCuentaCollection();
            Collection<Desarrolla> attachedDesarrollaCollectionNew = new ArrayList<Desarrolla>();
            for (Desarrolla desarrollaCollectionNewDesarrollaToAttach : desarrollaCollectionNew) {
                desarrollaCollectionNewDesarrollaToAttach = em.getReference(desarrollaCollectionNewDesarrollaToAttach.getClass(), desarrollaCollectionNewDesarrollaToAttach.getIdDesarrolla());
                attachedDesarrollaCollectionNew.add(desarrollaCollectionNewDesarrollaToAttach);
            }
            desarrollaCollectionNew = attachedDesarrollaCollectionNew;
            programador.setDesarrollaCollection(desarrollaCollectionNew);
            Collection<Cuenta> attachedCuentaCollectionNew = new ArrayList<Cuenta>();
            for (Cuenta cuentaCollectionNewCuentaToAttach : cuentaCollectionNew) {
                cuentaCollectionNewCuentaToAttach = em.getReference(cuentaCollectionNewCuentaToAttach.getClass(), cuentaCollectionNewCuentaToAttach.getNombreUsuario());
                attachedCuentaCollectionNew.add(cuentaCollectionNewCuentaToAttach);
            }
            cuentaCollectionNew = attachedCuentaCollectionNew;
            programador.setCuentaCollection(cuentaCollectionNew);
            programador = em.merge(programador);
            for (Desarrolla desarrollaCollectionOldDesarrolla : desarrollaCollectionOld) {
                if (!desarrollaCollectionNew.contains(desarrollaCollectionOldDesarrolla)) {
                    desarrollaCollectionOldDesarrolla.setIdUsuario(null);
                    desarrollaCollectionOldDesarrolla = em.merge(desarrollaCollectionOldDesarrolla);
                }
            }
            for (Desarrolla desarrollaCollectionNewDesarrolla : desarrollaCollectionNew) {
                if (!desarrollaCollectionOld.contains(desarrollaCollectionNewDesarrolla)) {
                    Programador oldIdUsuarioOfDesarrollaCollectionNewDesarrolla = desarrollaCollectionNewDesarrolla.getIdUsuario();
                    desarrollaCollectionNewDesarrolla.setIdUsuario(programador);
                    desarrollaCollectionNewDesarrolla = em.merge(desarrollaCollectionNewDesarrolla);
                    if (oldIdUsuarioOfDesarrollaCollectionNewDesarrolla != null && !oldIdUsuarioOfDesarrollaCollectionNewDesarrolla.equals(programador)) {
                        oldIdUsuarioOfDesarrollaCollectionNewDesarrolla.getDesarrollaCollection().remove(desarrollaCollectionNewDesarrolla);
                        oldIdUsuarioOfDesarrollaCollectionNewDesarrolla = em.merge(oldIdUsuarioOfDesarrollaCollectionNewDesarrolla);
                    }
                }
            }
            for (Cuenta cuentaCollectionOldCuenta : cuentaCollectionOld) {
                if (!cuentaCollectionNew.contains(cuentaCollectionOldCuenta)) {
                    cuentaCollectionOldCuenta.setIdUsuario(null);
                    cuentaCollectionOldCuenta = em.merge(cuentaCollectionOldCuenta);
                }
            }
            for (Cuenta cuentaCollectionNewCuenta : cuentaCollectionNew) {
                if (!cuentaCollectionOld.contains(cuentaCollectionNewCuenta)) {
                    Programador oldIdUsuarioOfCuentaCollectionNewCuenta = cuentaCollectionNewCuenta.getIdUsuario();
                    cuentaCollectionNewCuenta.setIdUsuario(programador);
                    cuentaCollectionNewCuenta = em.merge(cuentaCollectionNewCuenta);
                    if (oldIdUsuarioOfCuentaCollectionNewCuenta != null && !oldIdUsuarioOfCuentaCollectionNewCuenta.equals(programador)) {
                        oldIdUsuarioOfCuentaCollectionNewCuenta.getCuentaCollection().remove(cuentaCollectionNewCuenta);
                        oldIdUsuarioOfCuentaCollectionNewCuenta = em.merge(oldIdUsuarioOfCuentaCollectionNewCuenta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = programador.getIdUsuario();
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

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Programador programador;
            try {
                programador = em.getReference(Programador.class, id);
                programador.getIdUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The programador with id " + id + " no longer exists.", enfe);
            }
            Collection<Desarrolla> desarrollaCollection = programador.getDesarrollaCollection();
            for (Desarrolla desarrollaCollectionDesarrolla : desarrollaCollection) {
                desarrollaCollectionDesarrolla.setIdUsuario(null);
                desarrollaCollectionDesarrolla = em.merge(desarrollaCollectionDesarrolla);
            }
            Collection<Cuenta> cuentaCollection = programador.getCuentaCollection();
            for (Cuenta cuentaCollectionCuenta : cuentaCollection) {
                cuentaCollectionCuenta.setIdUsuario(null);
                cuentaCollectionCuenta = em.merge(cuentaCollectionCuenta);
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

    public Programador findProgramador(Long id) {
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
