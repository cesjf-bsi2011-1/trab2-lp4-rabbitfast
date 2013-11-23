/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.auadottonizaidem.dao;

import br.auadottonizaidem.dao.exceptions.NonexistentEntityException;
import br.auadottonizaidem.dao.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.auadottonizaidem.entity.Entrega;
import br.auadottonizaidem.entity.RotaPercurso;
import br.auadottonizaidem.entity.Status;
import br.auadottonizaidem.entity.StatusPK;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Aparecida
 */
public class StatusJpaController implements Serializable {

    public StatusJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Status status) throws PreexistingEntityException, Exception {
        if (status.getStatusPK() == null) {
            status.setStatusPK(new StatusPK());
        }
        status.getStatusPK().setIdEntrega(status.getEntrega().getIdEntrega());
        status.getStatusPK().setIdRota(status.getRotaPercurso().getRotaPercursoPK().getIdRota());
        status.getStatusPK().setIdRotaPercurso(status.getRotaPercurso().getRotaPercursoPK().getIdPontoReferencia());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Entrega entrega = status.getEntrega();
            if (entrega != null) {
                entrega = em.getReference(entrega.getClass(), entrega.getIdEntrega());
                status.setEntrega(entrega);
            }
            RotaPercurso rotaPercurso = status.getRotaPercurso();
            if (rotaPercurso != null) {
                rotaPercurso = em.getReference(rotaPercurso.getClass(), rotaPercurso.getRotaPercursoPK());
                status.setRotaPercurso(rotaPercurso);
            }
            em.persist(status);
            if (entrega != null) {
                entrega.getStatusList().add(status);
                entrega = em.merge(entrega);
            }
            if (rotaPercurso != null) {
                rotaPercurso.getStatusList().add(status);
                rotaPercurso = em.merge(rotaPercurso);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findStatus(status.getStatusPK()) != null) {
                throw new PreexistingEntityException("Status " + status + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Status status) throws NonexistentEntityException, Exception {
        status.getStatusPK().setIdEntrega(status.getEntrega().getIdEntrega());
        status.getStatusPK().setIdRota(status.getRotaPercurso().getRotaPercursoPK().getIdRota());
        status.getStatusPK().setIdRotaPercurso(status.getRotaPercurso().getRotaPercursoPK().getIdPontoReferencia());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Status persistentStatus = em.find(Status.class, status.getStatusPK());
            Entrega entregaOld = persistentStatus.getEntrega();
            Entrega entregaNew = status.getEntrega();
            RotaPercurso rotaPercursoOld = persistentStatus.getRotaPercurso();
            RotaPercurso rotaPercursoNew = status.getRotaPercurso();
            if (entregaNew != null) {
                entregaNew = em.getReference(entregaNew.getClass(), entregaNew.getIdEntrega());
                status.setEntrega(entregaNew);
            }
            if (rotaPercursoNew != null) {
                rotaPercursoNew = em.getReference(rotaPercursoNew.getClass(), rotaPercursoNew.getRotaPercursoPK());
                status.setRotaPercurso(rotaPercursoNew);
            }
            status = em.merge(status);
            if (entregaOld != null && !entregaOld.equals(entregaNew)) {
                entregaOld.getStatusList().remove(status);
                entregaOld = em.merge(entregaOld);
            }
            if (entregaNew != null && !entregaNew.equals(entregaOld)) {
                entregaNew.getStatusList().add(status);
                entregaNew = em.merge(entregaNew);
            }
            if (rotaPercursoOld != null && !rotaPercursoOld.equals(rotaPercursoNew)) {
                rotaPercursoOld.getStatusList().remove(status);
                rotaPercursoOld = em.merge(rotaPercursoOld);
            }
            if (rotaPercursoNew != null && !rotaPercursoNew.equals(rotaPercursoOld)) {
                rotaPercursoNew.getStatusList().add(status);
                rotaPercursoNew = em.merge(rotaPercursoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                StatusPK id = status.getStatusPK();
                if (findStatus(id) == null) {
                    throw new NonexistentEntityException("The status with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(StatusPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Status status;
            try {
                status = em.getReference(Status.class, id);
                status.getStatusPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The status with id " + id + " no longer exists.", enfe);
            }
            Entrega entrega = status.getEntrega();
            if (entrega != null) {
                entrega.getStatusList().remove(status);
                entrega = em.merge(entrega);
            }
            RotaPercurso rotaPercurso = status.getRotaPercurso();
            if (rotaPercurso != null) {
                rotaPercurso.getStatusList().remove(status);
                rotaPercurso = em.merge(rotaPercurso);
            }
            em.remove(status);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Status> findStatusEntities() {
        return findStatusEntities(true, -1, -1);
    }

    public List<Status> findStatusEntities(int maxResults, int firstResult) {
        return findStatusEntities(false, maxResults, firstResult);
    }

    private List<Status> findStatusEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Status.class));
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

    public Status findStatus(StatusPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Status.class, id);
        } finally {
            em.close();
        }
    }

    public int getStatusCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Status> rt = cq.from(Status.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
