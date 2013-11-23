/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.auadottonizaidem.dao;

import br.auadottonizaidem.dao.exceptions.IllegalOrphanException;
import br.auadottonizaidem.dao.exceptions.NonexistentEntityException;
import br.auadottonizaidem.dao.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.auadottonizaidem.entity.PontoReferencia;
import br.auadottonizaidem.entity.Rota;
import br.auadottonizaidem.entity.RotaPercurso;
import br.auadottonizaidem.entity.RotaPercursoPK;
import br.auadottonizaidem.entity.Status;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Aparecida
 */
public class RotaPercursoJpaController implements Serializable {

    public RotaPercursoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(RotaPercurso rotaPercurso) throws PreexistingEntityException, Exception {
        if (rotaPercurso.getRotaPercursoPK() == null) {
            rotaPercurso.setRotaPercursoPK(new RotaPercursoPK());
        }
        if (rotaPercurso.getStatusList() == null) {
            rotaPercurso.setStatusList(new ArrayList<Status>());
        }
        rotaPercurso.getRotaPercursoPK().setIdPontoReferencia(rotaPercurso.getPontoReferencia().getIdPontoReferencia());
        rotaPercurso.getRotaPercursoPK().setIdRota(rotaPercurso.getRota().getIdRota());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PontoReferencia pontoReferencia = rotaPercurso.getPontoReferencia();
            if (pontoReferencia != null) {
                pontoReferencia = em.getReference(pontoReferencia.getClass(), pontoReferencia.getIdPontoReferencia());
                rotaPercurso.setPontoReferencia(pontoReferencia);
            }
            Rota rota = rotaPercurso.getRota();
            if (rota != null) {
                rota = em.getReference(rota.getClass(), rota.getIdRota());
                rotaPercurso.setRota(rota);
            }
            List<Status> attachedStatusList = new ArrayList<Status>();
            for (Status statusListStatusToAttach : rotaPercurso.getStatusList()) {
                statusListStatusToAttach = em.getReference(statusListStatusToAttach.getClass(), statusListStatusToAttach.getStatusPK());
                attachedStatusList.add(statusListStatusToAttach);
            }
            rotaPercurso.setStatusList(attachedStatusList);
            em.persist(rotaPercurso);
            if (pontoReferencia != null) {
                pontoReferencia.getRotaPercursoList().add(rotaPercurso);
                pontoReferencia = em.merge(pontoReferencia);
            }
            if (rota != null) {
                rota.getRotaPercursoList().add(rotaPercurso);
                rota = em.merge(rota);
            }
            for (Status statusListStatus : rotaPercurso.getStatusList()) {
                RotaPercurso oldRotaPercursoOfStatusListStatus = statusListStatus.getRotaPercurso();
                statusListStatus.setRotaPercurso(rotaPercurso);
                statusListStatus = em.merge(statusListStatus);
                if (oldRotaPercursoOfStatusListStatus != null) {
                    oldRotaPercursoOfStatusListStatus.getStatusList().remove(statusListStatus);
                    oldRotaPercursoOfStatusListStatus = em.merge(oldRotaPercursoOfStatusListStatus);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findRotaPercurso(rotaPercurso.getRotaPercursoPK()) != null) {
                throw new PreexistingEntityException("RotaPercurso " + rotaPercurso + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(RotaPercurso rotaPercurso) throws IllegalOrphanException, NonexistentEntityException, Exception {
        rotaPercurso.getRotaPercursoPK().setIdPontoReferencia(rotaPercurso.getPontoReferencia().getIdPontoReferencia());
        rotaPercurso.getRotaPercursoPK().setIdRota(rotaPercurso.getRota().getIdRota());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RotaPercurso persistentRotaPercurso = em.find(RotaPercurso.class, rotaPercurso.getRotaPercursoPK());
            PontoReferencia pontoReferenciaOld = persistentRotaPercurso.getPontoReferencia();
            PontoReferencia pontoReferenciaNew = rotaPercurso.getPontoReferencia();
            Rota rotaOld = persistentRotaPercurso.getRota();
            Rota rotaNew = rotaPercurso.getRota();
            List<Status> statusListOld = persistentRotaPercurso.getStatusList();
            List<Status> statusListNew = rotaPercurso.getStatusList();
            List<String> illegalOrphanMessages = null;
            for (Status statusListOldStatus : statusListOld) {
                if (!statusListNew.contains(statusListOldStatus)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Status " + statusListOldStatus + " since its rotaPercurso field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (pontoReferenciaNew != null) {
                pontoReferenciaNew = em.getReference(pontoReferenciaNew.getClass(), pontoReferenciaNew.getIdPontoReferencia());
                rotaPercurso.setPontoReferencia(pontoReferenciaNew);
            }
            if (rotaNew != null) {
                rotaNew = em.getReference(rotaNew.getClass(), rotaNew.getIdRota());
                rotaPercurso.setRota(rotaNew);
            }
            List<Status> attachedStatusListNew = new ArrayList<Status>();
            for (Status statusListNewStatusToAttach : statusListNew) {
                statusListNewStatusToAttach = em.getReference(statusListNewStatusToAttach.getClass(), statusListNewStatusToAttach.getStatusPK());
                attachedStatusListNew.add(statusListNewStatusToAttach);
            }
            statusListNew = attachedStatusListNew;
            rotaPercurso.setStatusList(statusListNew);
            rotaPercurso = em.merge(rotaPercurso);
            if (pontoReferenciaOld != null && !pontoReferenciaOld.equals(pontoReferenciaNew)) {
                pontoReferenciaOld.getRotaPercursoList().remove(rotaPercurso);
                pontoReferenciaOld = em.merge(pontoReferenciaOld);
            }
            if (pontoReferenciaNew != null && !pontoReferenciaNew.equals(pontoReferenciaOld)) {
                pontoReferenciaNew.getRotaPercursoList().add(rotaPercurso);
                pontoReferenciaNew = em.merge(pontoReferenciaNew);
            }
            if (rotaOld != null && !rotaOld.equals(rotaNew)) {
                rotaOld.getRotaPercursoList().remove(rotaPercurso);
                rotaOld = em.merge(rotaOld);
            }
            if (rotaNew != null && !rotaNew.equals(rotaOld)) {
                rotaNew.getRotaPercursoList().add(rotaPercurso);
                rotaNew = em.merge(rotaNew);
            }
            for (Status statusListNewStatus : statusListNew) {
                if (!statusListOld.contains(statusListNewStatus)) {
                    RotaPercurso oldRotaPercursoOfStatusListNewStatus = statusListNewStatus.getRotaPercurso();
                    statusListNewStatus.setRotaPercurso(rotaPercurso);
                    statusListNewStatus = em.merge(statusListNewStatus);
                    if (oldRotaPercursoOfStatusListNewStatus != null && !oldRotaPercursoOfStatusListNewStatus.equals(rotaPercurso)) {
                        oldRotaPercursoOfStatusListNewStatus.getStatusList().remove(statusListNewStatus);
                        oldRotaPercursoOfStatusListNewStatus = em.merge(oldRotaPercursoOfStatusListNewStatus);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                RotaPercursoPK id = rotaPercurso.getRotaPercursoPK();
                if (findRotaPercurso(id) == null) {
                    throw new NonexistentEntityException("The rotaPercurso with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(RotaPercursoPK id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RotaPercurso rotaPercurso;
            try {
                rotaPercurso = em.getReference(RotaPercurso.class, id);
                rotaPercurso.getRotaPercursoPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The rotaPercurso with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Status> statusListOrphanCheck = rotaPercurso.getStatusList();
            for (Status statusListOrphanCheckStatus : statusListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This RotaPercurso (" + rotaPercurso + ") cannot be destroyed since the Status " + statusListOrphanCheckStatus + " in its statusList field has a non-nullable rotaPercurso field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            PontoReferencia pontoReferencia = rotaPercurso.getPontoReferencia();
            if (pontoReferencia != null) {
                pontoReferencia.getRotaPercursoList().remove(rotaPercurso);
                pontoReferencia = em.merge(pontoReferencia);
            }
            Rota rota = rotaPercurso.getRota();
            if (rota != null) {
                rota.getRotaPercursoList().remove(rotaPercurso);
                rota = em.merge(rota);
            }
            em.remove(rotaPercurso);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<RotaPercurso> findRotaPercursoEntities() {
        return findRotaPercursoEntities(true, -1, -1);
    }

    public List<RotaPercurso> findRotaPercursoEntities(int maxResults, int firstResult) {
        return findRotaPercursoEntities(false, maxResults, firstResult);
    }

    private List<RotaPercurso> findRotaPercursoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RotaPercurso.class));
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

    public RotaPercurso findRotaPercurso(RotaPercursoPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RotaPercurso.class, id);
        } finally {
            em.close();
        }
    }

    public int getRotaPercursoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RotaPercurso> rt = cq.from(RotaPercurso.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
