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
public class ArchivoJpaController implements Serializable {

    public ArchivoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Archivo archivo) {
        if (archivo.getArchivoCollection() == null) {
            archivo.setArchivoCollection(new ArrayList<Archivo>());
        }
        if (archivo.getProyectoCollection() == null) {
            archivo.setProyectoCollection(new ArrayList<Proyecto>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Archivo idArchivoPadre = archivo.getIdArchivoPadre();
            if (idArchivoPadre != null) {
                idArchivoPadre = em.getReference(idArchivoPadre.getClass(), idArchivoPadre.getIdArchivo());
                archivo.setIdArchivoPadre(idArchivoPadre);
            }
            Collection<Archivo> attachedArchivoCollection = new ArrayList<Archivo>();
            for (Archivo archivoCollectionArchivoToAttach : archivo.getArchivoCollection()) {
                archivoCollectionArchivoToAttach = em.getReference(archivoCollectionArchivoToAttach.getClass(), archivoCollectionArchivoToAttach.getIdArchivo());
                attachedArchivoCollection.add(archivoCollectionArchivoToAttach);
            }
            archivo.setArchivoCollection(attachedArchivoCollection);
            Collection<Proyecto> attachedProyectoCollection = new ArrayList<Proyecto>();
            for (Proyecto proyectoCollectionProyectoToAttach : archivo.getProyectoCollection()) {
                proyectoCollectionProyectoToAttach = em.getReference(proyectoCollectionProyectoToAttach.getClass(), proyectoCollectionProyectoToAttach.getIdProyecto());
                attachedProyectoCollection.add(proyectoCollectionProyectoToAttach);
            }
            archivo.setProyectoCollection(attachedProyectoCollection);
            em.persist(archivo);
            if (idArchivoPadre != null) {
                idArchivoPadre.getArchivoCollection().add(archivo);
                idArchivoPadre = em.merge(idArchivoPadre);
            }
            for (Archivo archivoCollectionArchivo : archivo.getArchivoCollection()) {
                Archivo oldIdArchivoPadreOfArchivoCollectionArchivo = archivoCollectionArchivo.getIdArchivoPadre();
                archivoCollectionArchivo.setIdArchivoPadre(archivo);
                archivoCollectionArchivo = em.merge(archivoCollectionArchivo);
                if (oldIdArchivoPadreOfArchivoCollectionArchivo != null) {
                    oldIdArchivoPadreOfArchivoCollectionArchivo.getArchivoCollection().remove(archivoCollectionArchivo);
                    oldIdArchivoPadreOfArchivoCollectionArchivo = em.merge(oldIdArchivoPadreOfArchivoCollectionArchivo);
                }
            }
            for (Proyecto proyectoCollectionProyecto : archivo.getProyectoCollection()) {
                Archivo oldIdArchivoOfProyectoCollectionProyecto = proyectoCollectionProyecto.getIdArchivo();
                proyectoCollectionProyecto.setIdArchivo(archivo);
                proyectoCollectionProyecto = em.merge(proyectoCollectionProyecto);
                if (oldIdArchivoOfProyectoCollectionProyecto != null) {
                    oldIdArchivoOfProyectoCollectionProyecto.getProyectoCollection().remove(proyectoCollectionProyecto);
                    oldIdArchivoOfProyectoCollectionProyecto = em.merge(oldIdArchivoOfProyectoCollectionProyecto);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Archivo archivo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Archivo persistentArchivo = em.find(Archivo.class, archivo.getIdArchivo());
            Archivo idArchivoPadreOld = persistentArchivo.getIdArchivoPadre();
            Archivo idArchivoPadreNew = archivo.getIdArchivoPadre();
            Collection<Archivo> archivoCollectionOld = persistentArchivo.getArchivoCollection();
            Collection<Archivo> archivoCollectionNew = archivo.getArchivoCollection();
            Collection<Proyecto> proyectoCollectionOld = persistentArchivo.getProyectoCollection();
            Collection<Proyecto> proyectoCollectionNew = archivo.getProyectoCollection();
            if (idArchivoPadreNew != null) {
                idArchivoPadreNew = em.getReference(idArchivoPadreNew.getClass(), idArchivoPadreNew.getIdArchivo());
                archivo.setIdArchivoPadre(idArchivoPadreNew);
            }
            Collection<Archivo> attachedArchivoCollectionNew = new ArrayList<Archivo>();
            for (Archivo archivoCollectionNewArchivoToAttach : archivoCollectionNew) {
                archivoCollectionNewArchivoToAttach = em.getReference(archivoCollectionNewArchivoToAttach.getClass(), archivoCollectionNewArchivoToAttach.getIdArchivo());
                attachedArchivoCollectionNew.add(archivoCollectionNewArchivoToAttach);
            }
            archivoCollectionNew = attachedArchivoCollectionNew;
            archivo.setArchivoCollection(archivoCollectionNew);
            Collection<Proyecto> attachedProyectoCollectionNew = new ArrayList<Proyecto>();
            for (Proyecto proyectoCollectionNewProyectoToAttach : proyectoCollectionNew) {
                proyectoCollectionNewProyectoToAttach = em.getReference(proyectoCollectionNewProyectoToAttach.getClass(), proyectoCollectionNewProyectoToAttach.getIdProyecto());
                attachedProyectoCollectionNew.add(proyectoCollectionNewProyectoToAttach);
            }
            proyectoCollectionNew = attachedProyectoCollectionNew;
            archivo.setProyectoCollection(proyectoCollectionNew);
            archivo = em.merge(archivo);
            if (idArchivoPadreOld != null && !idArchivoPadreOld.equals(idArchivoPadreNew)) {
                idArchivoPadreOld.getArchivoCollection().remove(archivo);
                idArchivoPadreOld = em.merge(idArchivoPadreOld);
            }
            if (idArchivoPadreNew != null && !idArchivoPadreNew.equals(idArchivoPadreOld)) {
                idArchivoPadreNew.getArchivoCollection().add(archivo);
                idArchivoPadreNew = em.merge(idArchivoPadreNew);
            }
            for (Archivo archivoCollectionOldArchivo : archivoCollectionOld) {
                if (!archivoCollectionNew.contains(archivoCollectionOldArchivo)) {
                    archivoCollectionOldArchivo.setIdArchivoPadre(null);
                    archivoCollectionOldArchivo = em.merge(archivoCollectionOldArchivo);
                }
            }
            for (Archivo archivoCollectionNewArchivo : archivoCollectionNew) {
                if (!archivoCollectionOld.contains(archivoCollectionNewArchivo)) {
                    Archivo oldIdArchivoPadreOfArchivoCollectionNewArchivo = archivoCollectionNewArchivo.getIdArchivoPadre();
                    archivoCollectionNewArchivo.setIdArchivoPadre(archivo);
                    archivoCollectionNewArchivo = em.merge(archivoCollectionNewArchivo);
                    if (oldIdArchivoPadreOfArchivoCollectionNewArchivo != null && !oldIdArchivoPadreOfArchivoCollectionNewArchivo.equals(archivo)) {
                        oldIdArchivoPadreOfArchivoCollectionNewArchivo.getArchivoCollection().remove(archivoCollectionNewArchivo);
                        oldIdArchivoPadreOfArchivoCollectionNewArchivo = em.merge(oldIdArchivoPadreOfArchivoCollectionNewArchivo);
                    }
                }
            }
            for (Proyecto proyectoCollectionOldProyecto : proyectoCollectionOld) {
                if (!proyectoCollectionNew.contains(proyectoCollectionOldProyecto)) {
                    proyectoCollectionOldProyecto.setIdArchivo(null);
                    proyectoCollectionOldProyecto = em.merge(proyectoCollectionOldProyecto);
                }
            }
            for (Proyecto proyectoCollectionNewProyecto : proyectoCollectionNew) {
                if (!proyectoCollectionOld.contains(proyectoCollectionNewProyecto)) {
                    Archivo oldIdArchivoOfProyectoCollectionNewProyecto = proyectoCollectionNewProyecto.getIdArchivo();
                    proyectoCollectionNewProyecto.setIdArchivo(archivo);
                    proyectoCollectionNewProyecto = em.merge(proyectoCollectionNewProyecto);
                    if (oldIdArchivoOfProyectoCollectionNewProyecto != null && !oldIdArchivoOfProyectoCollectionNewProyecto.equals(archivo)) {
                        oldIdArchivoOfProyectoCollectionNewProyecto.getProyectoCollection().remove(proyectoCollectionNewProyecto);
                        oldIdArchivoOfProyectoCollectionNewProyecto = em.merge(oldIdArchivoOfProyectoCollectionNewProyecto);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = archivo.getIdArchivo();
                if (findArchivo(id) == null) {
                    throw new NonexistentEntityException("The archivo with id " + id + " no longer exists.");
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
            Archivo archivo;
            try {
                archivo = em.getReference(Archivo.class, id);
                archivo.getIdArchivo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The archivo with id " + id + " no longer exists.", enfe);
            }
            Archivo idArchivoPadre = archivo.getIdArchivoPadre();
            if (idArchivoPadre != null) {
                idArchivoPadre.getArchivoCollection().remove(archivo);
                idArchivoPadre = em.merge(idArchivoPadre);
            }
            Collection<Archivo> archivoCollection = archivo.getArchivoCollection();
            for (Archivo archivoCollectionArchivo : archivoCollection) {
                archivoCollectionArchivo.setIdArchivoPadre(null);
                archivoCollectionArchivo = em.merge(archivoCollectionArchivo);
            }
            Collection<Proyecto> proyectoCollection = archivo.getProyectoCollection();
            for (Proyecto proyectoCollectionProyecto : proyectoCollection) {
                proyectoCollectionProyecto.setIdArchivo(null);
                proyectoCollectionProyecto = em.merge(proyectoCollectionProyecto);
            }
            em.remove(archivo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Archivo> findArchivoEntities() {
        return findArchivoEntities(true, -1, -1);
    }

    public List<Archivo> findArchivoEntities(int maxResults, int firstResult) {
        return findArchivoEntities(false, maxResults, firstResult);
    }

    private List<Archivo> findArchivoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Archivo.class));
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

    public Archivo findArchivo(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Archivo.class, id);
        } finally {
            em.close();
        }
    }

    public int getArchivoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Archivo> rt = cq.from(Archivo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
