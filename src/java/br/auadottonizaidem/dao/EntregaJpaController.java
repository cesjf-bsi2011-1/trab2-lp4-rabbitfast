/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.auadottonizaidem.dao;

import br.auadottonizaidem.dao.exceptions.IllegalOrphanException;
import br.auadottonizaidem.dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.auadottonizaidem.entity.Veiculo;
import br.auadottonizaidem.entity.Rota;
import br.auadottonizaidem.entity.Cliente;
import br.auadottonizaidem.entity.Entrega;
import br.auadottonizaidem.entity.Status;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Aparecida
 */
public class EntregaJpaController implements Serializable {

    public EntregaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Entrega entrega) {
        if (entrega.getStatusList() == null) {
            entrega.setStatusList(new ArrayList<Status>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Veiculo placaVeiculo = entrega.getPlacaVeiculo();
            if (placaVeiculo != null) {
                placaVeiculo = em.getReference(placaVeiculo.getClass(), placaVeiculo.getPlacaVeiculo());
                entrega.setPlacaVeiculo(placaVeiculo);
            }
            Rota idRota = entrega.getIdRota();
            if (idRota != null) {
                idRota = em.getReference(idRota.getClass(), idRota.getIdRota());
                entrega.setIdRota(idRota);
            }
            Cliente idCliente = entrega.getIdCliente();
            if (idCliente != null) {
                idCliente = em.getReference(idCliente.getClass(), idCliente.getIdCliente());
                entrega.setIdCliente(idCliente);
            }
            List<Status> attachedStatusList = new ArrayList<Status>();
            for (Status statusListStatusToAttach : entrega.getStatusList()) {
                statusListStatusToAttach = em.getReference(statusListStatusToAttach.getClass(), statusListStatusToAttach.getStatusPK());
                attachedStatusList.add(statusListStatusToAttach);
            }
            entrega.setStatusList(attachedStatusList);
            em.persist(entrega);
            if (placaVeiculo != null) {
                placaVeiculo.getEntregaList().add(entrega);
                placaVeiculo = em.merge(placaVeiculo);
            }
            if (idRota != null) {
                idRota.getEntregaList().add(entrega);
                idRota = em.merge(idRota);
            }
            if (idCliente != null) {
                idCliente.getEntregaList().add(entrega);
                idCliente = em.merge(idCliente);
            }
            for (Status statusListStatus : entrega.getStatusList()) {
                Entrega oldEntregaOfStatusListStatus = statusListStatus.getEntrega();
                statusListStatus.setEntrega(entrega);
                statusListStatus = em.merge(statusListStatus);
                if (oldEntregaOfStatusListStatus != null) {
                    oldEntregaOfStatusListStatus.getStatusList().remove(statusListStatus);
                    oldEntregaOfStatusListStatus = em.merge(oldEntregaOfStatusListStatus);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Entrega entrega) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Entrega persistentEntrega = em.find(Entrega.class, entrega.getIdEntrega());
            Veiculo placaVeiculoOld = persistentEntrega.getPlacaVeiculo();
            Veiculo placaVeiculoNew = entrega.getPlacaVeiculo();
            Rota idRotaOld = persistentEntrega.getIdRota();
            Rota idRotaNew = entrega.getIdRota();
            Cliente idClienteOld = persistentEntrega.getIdCliente();
            Cliente idClienteNew = entrega.getIdCliente();
            List<Status> statusListOld = persistentEntrega.getStatusList();
            List<Status> statusListNew = entrega.getStatusList();
            List<String> illegalOrphanMessages = null;
            for (Status statusListOldStatus : statusListOld) {
                if (!statusListNew.contains(statusListOldStatus)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Status " + statusListOldStatus + " since its entrega field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (placaVeiculoNew != null) {
                placaVeiculoNew = em.getReference(placaVeiculoNew.getClass(), placaVeiculoNew.getPlacaVeiculo());
                entrega.setPlacaVeiculo(placaVeiculoNew);
            }
            if (idRotaNew != null) {
                idRotaNew = em.getReference(idRotaNew.getClass(), idRotaNew.getIdRota());
                entrega.setIdRota(idRotaNew);
            }
            if (idClienteNew != null) {
                idClienteNew = em.getReference(idClienteNew.getClass(), idClienteNew.getIdCliente());
                entrega.setIdCliente(idClienteNew);
            }
            List<Status> attachedStatusListNew = new ArrayList<Status>();
            for (Status statusListNewStatusToAttach : statusListNew) {
                statusListNewStatusToAttach = em.getReference(statusListNewStatusToAttach.getClass(), statusListNewStatusToAttach.getStatusPK());
                attachedStatusListNew.add(statusListNewStatusToAttach);
            }
            statusListNew = attachedStatusListNew;
            entrega.setStatusList(statusListNew);
            entrega = em.merge(entrega);
            if (placaVeiculoOld != null && !placaVeiculoOld.equals(placaVeiculoNew)) {
                placaVeiculoOld.getEntregaList().remove(entrega);
                placaVeiculoOld = em.merge(placaVeiculoOld);
            }
            if (placaVeiculoNew != null && !placaVeiculoNew.equals(placaVeiculoOld)) {
                placaVeiculoNew.getEntregaList().add(entrega);
                placaVeiculoNew = em.merge(placaVeiculoNew);
            }
            if (idRotaOld != null && !idRotaOld.equals(idRotaNew)) {
                idRotaOld.getEntregaList().remove(entrega);
                idRotaOld = em.merge(idRotaOld);
            }
            if (idRotaNew != null && !idRotaNew.equals(idRotaOld)) {
                idRotaNew.getEntregaList().add(entrega);
                idRotaNew = em.merge(idRotaNew);
            }
            if (idClienteOld != null && !idClienteOld.equals(idClienteNew)) {
                idClienteOld.getEntregaList().remove(entrega);
                idClienteOld = em.merge(idClienteOld);
            }
            if (idClienteNew != null && !idClienteNew.equals(idClienteOld)) {
                idClienteNew.getEntregaList().add(entrega);
                idClienteNew = em.merge(idClienteNew);
            }
            for (Status statusListNewStatus : statusListNew) {
                if (!statusListOld.contains(statusListNewStatus)) {
                    Entrega oldEntregaOfStatusListNewStatus = statusListNewStatus.getEntrega();
                    statusListNewStatus.setEntrega(entrega);
                    statusListNewStatus = em.merge(statusListNewStatus);
                    if (oldEntregaOfStatusListNewStatus != null && !oldEntregaOfStatusListNewStatus.equals(entrega)) {
                        oldEntregaOfStatusListNewStatus.getStatusList().remove(statusListNewStatus);
                        oldEntregaOfStatusListNewStatus = em.merge(oldEntregaOfStatusListNewStatus);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = entrega.getIdEntrega();
                if (findEntrega(id) == null) {
                    throw new NonexistentEntityException("The entrega with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Entrega entrega;
            try {
                entrega = em.getReference(Entrega.class, id);
                entrega.getIdEntrega();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The entrega with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Status> statusListOrphanCheck = entrega.getStatusList();
            for (Status statusListOrphanCheckStatus : statusListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Entrega (" + entrega + ") cannot be destroyed since the Status " + statusListOrphanCheckStatus + " in its statusList field has a non-nullable entrega field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Veiculo placaVeiculo = entrega.getPlacaVeiculo();
            if (placaVeiculo != null) {
                placaVeiculo.getEntregaList().remove(entrega);
                placaVeiculo = em.merge(placaVeiculo);
            }
            Rota idRota = entrega.getIdRota();
            if (idRota != null) {
                idRota.getEntregaList().remove(entrega);
                idRota = em.merge(idRota);
            }
            Cliente idCliente = entrega.getIdCliente();
            if (idCliente != null) {
                idCliente.getEntregaList().remove(entrega);
                idCliente = em.merge(idCliente);
            }
            em.remove(entrega);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Entrega> findEntregaEntities() {
        return findEntregaEntities(true, -1, -1);
    }

    public List<Entrega> findEntregaEntities(int maxResults, int firstResult) {
        return findEntregaEntities(false, maxResults, firstResult);
    }

    private List<Entrega> findEntregaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Entrega.class));
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

    public Entrega findEntrega(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Entrega.class, id);
        } finally {
            em.close();
        }
    }

    public int getEntregaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Entrega> rt = cq.from(Entrega.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
