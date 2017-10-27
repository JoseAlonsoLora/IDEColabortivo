/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author raymu
 */
public class DesarrollaJpaController implements Serializable {

    public DesarrollaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Desarrolla desarrolla) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Programador idUsuario = desarrolla.getIdUsuario();
            if (idUsuario != null) {
                idUsuario = em.getReference(idUsuario.getClass(), idUsuario.getIdUsuario());
                desarrolla.setIdUsuario(idUsuario);
            }
            Proyecto idProyecto = desarrolla.getIdProyecto();
            if (idProyecto != null) {
                idProyecto = em.getReference(idProyecto.getClass(), idProyecto.getIdProyecto());
                desarrolla.setIdProyecto(idProyecto);
            }
            em.persist(desarrolla);
            if (idUsuario != null) {
                idUsuario.getDesarrollaCollection().add(desarrolla);
                idUsuario = em.merge(idUsuario);
            }
            if (idProyecto != null) {
                idProyecto.getDesarrollaCollection().add(desarrolla);
                idProyecto = em.merge(idProyecto);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Desarrolla desarrolla) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Desarrolla persistentDesarrolla = em.find(Desarrolla.class, desarrolla.getIdDesarrolla());
            Programador idUsuarioOld = persistentDesarrolla.getIdUsuario();
            Programador idUsuarioNew = desarrolla.getIdUsuario();
            Proyecto idProyectoOld = persistentDesarrolla.getIdProyecto();
            Proyecto idProyectoNew = desarrolla.getIdProyecto();
            if (idUsuarioNew != null) {
                idUsuarioNew = em.getReference(idUsuarioNew.getClass(), idUsuarioNew.getIdUsuario());
                desarrolla.setIdUsuario(idUsuarioNew);
            }
            if (idProyectoNew != null) {
                idProyectoNew = em.getReference(idProyectoNew.getClass(), idProyectoNew.getIdProyecto());
                desarrolla.setIdProyecto(idProyectoNew);
            }
            desarrolla = em.merge(desarrolla);
            if (idUsuarioOld != null && !idUsuarioOld.equals(idUsuarioNew)) {
                idUsuarioOld.getDesarrollaCollection().remove(desarrolla);
                idUsuarioOld = em.merge(idUsuarioOld);
            }
            if (idUsuarioNew != null && !idUsuarioNew.equals(idUsuarioOld)) {
                idUsuarioNew.getDesarrollaCollection().add(desarrolla);
                idUsuarioNew = em.merge(idUsuarioNew);
            }
            if (idProyectoOld != null && !idProyectoOld.equals(idProyectoNew)) {
                idProyectoOld.getDesarrollaCollection().remove(desarrolla);
                idProyectoOld = em.merge(idProyectoOld);
            }
            if (idProyectoNew != null && !idProyectoNew.equals(idProyectoOld)) {
                idProyectoNew.getDesarrollaCollection().add(desarrolla);
                idProyectoNew = em.merge(idProyectoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = desarrolla.getIdDesarrolla();
                if (findDesarrolla(id) == null) {
                    throw new NonexistentEntityException("The desarrolla with id " + id + " no longer exists.");
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
            Desarrolla desarrolla;
            try {
                desarrolla = em.getReference(Desarrolla.class, id);
                desarrolla.getIdDesarrolla();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The desarrolla with id " + id + " no longer exists.", enfe);
            }
            Programador idUsuario = desarrolla.getIdUsuario();
            if (idUsuario != null) {
                idUsuario.getDesarrollaCollection().remove(desarrolla);
                idUsuario = em.merge(idUsuario);
            }
            Proyecto idProyecto = desarrolla.getIdProyecto();
            if (idProyecto != null) {
                idProyecto.getDesarrollaCollection().remove(desarrolla);
                idProyecto = em.merge(idProyecto);
            }
            em.remove(desarrolla);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Desarrolla> findDesarrollaEntities() {
        return findDesarrollaEntities(true, -1, -1);
    }

    public List<Desarrolla> findDesarrollaEntities(int maxResults, int firstResult) {
        return findDesarrollaEntities(false, maxResults, firstResult);
    }

    private List<Desarrolla> findDesarrollaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Desarrolla.class));
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

    public Desarrolla findDesarrolla(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Desarrolla.class, id);
        } finally {
            em.close();
        }
    }

    public int getDesarrollaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Desarrolla> rt = cq.from(Desarrolla.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
