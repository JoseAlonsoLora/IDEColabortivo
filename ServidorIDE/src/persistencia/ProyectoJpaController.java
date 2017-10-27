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
public class ProyectoJpaController implements Serializable {

    public ProyectoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Proyecto proyecto) {
        if (proyecto.getDesarrollaCollection() == null) {
            proyecto.setDesarrollaCollection(new ArrayList<Desarrolla>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Archivo idArchivo = proyecto.getIdArchivo();
            if (idArchivo != null) {
                idArchivo = em.getReference(idArchivo.getClass(), idArchivo.getIdArchivo());
                proyecto.setIdArchivo(idArchivo);
            }
            Collection<Desarrolla> attachedDesarrollaCollection = new ArrayList<Desarrolla>();
            for (Desarrolla desarrollaCollectionDesarrollaToAttach : proyecto.getDesarrollaCollection()) {
                desarrollaCollectionDesarrollaToAttach = em.getReference(desarrollaCollectionDesarrollaToAttach.getClass(), desarrollaCollectionDesarrollaToAttach.getIdDesarrolla());
                attachedDesarrollaCollection.add(desarrollaCollectionDesarrollaToAttach);
            }
            proyecto.setDesarrollaCollection(attachedDesarrollaCollection);
            em.persist(proyecto);
            if (idArchivo != null) {
                idArchivo.getProyectoCollection().add(proyecto);
                idArchivo = em.merge(idArchivo);
            }
            for (Desarrolla desarrollaCollectionDesarrolla : proyecto.getDesarrollaCollection()) {
                Proyecto oldIdProyectoOfDesarrollaCollectionDesarrolla = desarrollaCollectionDesarrolla.getIdProyecto();
                desarrollaCollectionDesarrolla.setIdProyecto(proyecto);
                desarrollaCollectionDesarrolla = em.merge(desarrollaCollectionDesarrolla);
                if (oldIdProyectoOfDesarrollaCollectionDesarrolla != null) {
                    oldIdProyectoOfDesarrollaCollectionDesarrolla.getDesarrollaCollection().remove(desarrollaCollectionDesarrolla);
                    oldIdProyectoOfDesarrollaCollectionDesarrolla = em.merge(oldIdProyectoOfDesarrollaCollectionDesarrolla);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Proyecto proyecto) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Proyecto persistentProyecto = em.find(Proyecto.class, proyecto.getIdProyecto());
            Archivo idArchivoOld = persistentProyecto.getIdArchivo();
            Archivo idArchivoNew = proyecto.getIdArchivo();
            Collection<Desarrolla> desarrollaCollectionOld = persistentProyecto.getDesarrollaCollection();
            Collection<Desarrolla> desarrollaCollectionNew = proyecto.getDesarrollaCollection();
            if (idArchivoNew != null) {
                idArchivoNew = em.getReference(idArchivoNew.getClass(), idArchivoNew.getIdArchivo());
                proyecto.setIdArchivo(idArchivoNew);
            }
            Collection<Desarrolla> attachedDesarrollaCollectionNew = new ArrayList<Desarrolla>();
            for (Desarrolla desarrollaCollectionNewDesarrollaToAttach : desarrollaCollectionNew) {
                desarrollaCollectionNewDesarrollaToAttach = em.getReference(desarrollaCollectionNewDesarrollaToAttach.getClass(), desarrollaCollectionNewDesarrollaToAttach.getIdDesarrolla());
                attachedDesarrollaCollectionNew.add(desarrollaCollectionNewDesarrollaToAttach);
            }
            desarrollaCollectionNew = attachedDesarrollaCollectionNew;
            proyecto.setDesarrollaCollection(desarrollaCollectionNew);
            proyecto = em.merge(proyecto);
            if (idArchivoOld != null && !idArchivoOld.equals(idArchivoNew)) {
                idArchivoOld.getProyectoCollection().remove(proyecto);
                idArchivoOld = em.merge(idArchivoOld);
            }
            if (idArchivoNew != null && !idArchivoNew.equals(idArchivoOld)) {
                idArchivoNew.getProyectoCollection().add(proyecto);
                idArchivoNew = em.merge(idArchivoNew);
            }
            for (Desarrolla desarrollaCollectionOldDesarrolla : desarrollaCollectionOld) {
                if (!desarrollaCollectionNew.contains(desarrollaCollectionOldDesarrolla)) {
                    desarrollaCollectionOldDesarrolla.setIdProyecto(null);
                    desarrollaCollectionOldDesarrolla = em.merge(desarrollaCollectionOldDesarrolla);
                }
            }
            for (Desarrolla desarrollaCollectionNewDesarrolla : desarrollaCollectionNew) {
                if (!desarrollaCollectionOld.contains(desarrollaCollectionNewDesarrolla)) {
                    Proyecto oldIdProyectoOfDesarrollaCollectionNewDesarrolla = desarrollaCollectionNewDesarrolla.getIdProyecto();
                    desarrollaCollectionNewDesarrolla.setIdProyecto(proyecto);
                    desarrollaCollectionNewDesarrolla = em.merge(desarrollaCollectionNewDesarrolla);
                    if (oldIdProyectoOfDesarrollaCollectionNewDesarrolla != null && !oldIdProyectoOfDesarrollaCollectionNewDesarrolla.equals(proyecto)) {
                        oldIdProyectoOfDesarrollaCollectionNewDesarrolla.getDesarrollaCollection().remove(desarrollaCollectionNewDesarrolla);
                        oldIdProyectoOfDesarrollaCollectionNewDesarrolla = em.merge(oldIdProyectoOfDesarrollaCollectionNewDesarrolla);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = proyecto.getIdProyecto();
                if (findProyecto(id) == null) {
                    throw new NonexistentEntityException("The proyecto with id " + id + " no longer exists.");
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
            Proyecto proyecto;
            try {
                proyecto = em.getReference(Proyecto.class, id);
                proyecto.getIdProyecto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The proyecto with id " + id + " no longer exists.", enfe);
            }
            Archivo idArchivo = proyecto.getIdArchivo();
            if (idArchivo != null) {
                idArchivo.getProyectoCollection().remove(proyecto);
                idArchivo = em.merge(idArchivo);
            }
            Collection<Desarrolla> desarrollaCollection = proyecto.getDesarrollaCollection();
            for (Desarrolla desarrollaCollectionDesarrolla : desarrollaCollection) {
                desarrollaCollectionDesarrolla.setIdProyecto(null);
                desarrollaCollectionDesarrolla = em.merge(desarrollaCollectionDesarrolla);
            }
            em.remove(proyecto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Proyecto> findProyectoEntities() {
        return findProyectoEntities(true, -1, -1);
    }

    public List<Proyecto> findProyectoEntities(int maxResults, int firstResult) {
        return findProyectoEntities(false, maxResults, firstResult);
    }

    private List<Proyecto> findProyectoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Proyecto.class));
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

    public Proyecto findProyecto(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Proyecto.class, id);
        } finally {
            em.close();
        }
    }

    public int getProyectoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Proyecto> rt = cq.from(Proyecto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
