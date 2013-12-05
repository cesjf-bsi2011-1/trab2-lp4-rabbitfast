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
import br.auadottonizaidem.entity.Veiculo;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Aparecida
 */
public class VeiculoJpaController implements Serializable {

    public VeiculoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Veiculo veiculo) throws PreexistingEntityException, Exception {
        if (veiculo.getEntregaList() == null) {
            veiculo.setEntregaList(new ArrayList<Entrega>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Entrega> attachedEntregaList = new ArrayList<Entrega>();
            for (Entrega entregaListEntregaToAttach : veiculo.getEntregaList()) {
                entregaListEntregaToAttach = em.getReference(entregaListEntregaToAttach.getClass(), entregaListEntregaToAttach.getIdEntrega());
                attachedEntregaList.add(entregaListEntregaToAttach);
            }
            veiculo.setEntregaList(attachedEntregaList);
            em.persist(veiculo);
            for (Entrega entregaListEntrega : veiculo.getEntregaList()) {
                Veiculo oldPlacaVeiculoOfEntregaListEntrega = entregaListEntrega.getPlacaVeiculo();
                entregaListEntrega.setPlacaVeiculo(veiculo);
                entregaListEntrega = em.merge(entregaListEntrega);
                if (oldPlacaVeiculoOfEntregaListEntrega != null) {
                    oldPlacaVeiculoOfEntregaListEntrega.getEntregaList().remove(entregaListEntrega);
                    oldPlacaVeiculoOfEntregaListEntrega = em.merge(oldPlacaVeiculoOfEntregaListEntrega);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findVeiculo(veiculo.getPlacaVeiculo()) != null) {
                throw new PreexistingEntityException("Veiculo " + veiculo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Veiculo veiculo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Veiculo persistentVeiculo = em.find(Veiculo.class, veiculo.getPlacaVeiculo());
            List<Entrega> entregaListOld = persistentVeiculo.getEntregaList();
            List<Entrega> entregaListNew = veiculo.getEntregaList();
            List<Entrega> attachedEntregaListNew = new ArrayList<Entrega>();
            for (Entrega entregaListNewEntregaToAttach : entregaListNew) {
                entregaListNewEntregaToAttach = em.getReference(entregaListNewEntregaToAttach.getClass(), entregaListNewEntregaToAttach.getIdEntrega());
                attachedEntregaListNew.add(entregaListNewEntregaToAttach);
            }
            entregaListNew = attachedEntregaListNew;
            veiculo.setEntregaList(entregaListNew);
            veiculo = em.merge(veiculo);
            for (Entrega entregaListOldEntrega : entregaListOld) {
                if (!entregaListNew.contains(entregaListOldEntrega)) {
                    entregaListOldEntrega.setPlacaVeiculo(null);
                    entregaListOldEntrega = em.merge(entregaListOldEntrega);
                }
            }
            for (Entrega entregaListNewEntrega : entregaListNew) {
                if (!entregaListOld.contains(entregaListNewEntrega)) {
                    Veiculo oldPlacaVeiculoOfEntregaListNewEntrega = entregaListNewEntrega.getPlacaVeiculo();
                    entregaListNewEntrega.setPlacaVeiculo(veiculo);
                    entregaListNewEntrega = em.merge(entregaListNewEntrega);
                    if (oldPlacaVeiculoOfEntregaListNewEntrega != null && !oldPlacaVeiculoOfEntregaListNewEntrega.equals(veiculo)) {
                        oldPlacaVeiculoOfEntregaListNewEntrega.getEntregaList().remove(entregaListNewEntrega);
                        oldPlacaVeiculoOfEntregaListNewEntrega = em.merge(oldPlacaVeiculoOfEntregaListNewEntrega);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = veiculo.getPlacaVeiculo();
                if (findVeiculo(id) == null) {
                    throw new NonexistentEntityException("The veiculo with id " + id + " no longer exists.");
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
            Veiculo veiculo;
            try {
                veiculo = em.getReference(Veiculo.class, id);
                veiculo.getPlacaVeiculo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The veiculo with id " + id + " no longer exists.", enfe);
            }
            List<Entrega> entregaList = veiculo.getEntregaList();
            for (Entrega entregaListEntrega : entregaList) {
                entregaListEntrega.setPlacaVeiculo(null);
                entregaListEntrega = em.merge(entregaListEntrega);
            }
            em.remove(veiculo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Veiculo> findVeiculoEntities() {
        return findVeiculoEntities(true, -1, -1);
    }

    public List<Veiculo> findVeiculoEntities(int maxResults, int firstResult) {
        return findVeiculoEntities(false, maxResults, firstResult);
    }

    private List<Veiculo> findVeiculoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Veiculo.class));
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

    public Veiculo findVeiculo(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Veiculo.class, id);
        } finally {
            em.close();
        }
    }

    public int getVeiculoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Veiculo> rt = cq.from(Veiculo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<Veiculo> findVeiculoByCriterio(Veiculo sp) {

        String sql = "select c from tb_veiculo c where 1=1 ";
        if (sp.getPlacaVeiculo() != null) {
            sql += "and c.placa_veiculo like :pl_vei ";
        }
        if (sp.getNomeMotorista() != null) {
            sql += "and c.nome_motorista like :nom_mot ";
        }
        if (sp.getTipoVeiculo() != null) {
            sql += "and c.tipo_veiculo= :tv ";
        }


        Query q = getEntityManager().createQuery(sql);
        if (sp.getPlacaVeiculo() != null) {
            q.setParameter("pl_vei", sp.getPlacaVeiculo() + "%");
        }
        if (sp.getNomeMotorista() != null) {
            q.setParameter("nom_mot", sp.getNomeMotorista() + "%");
        }
        if (sp.getPlacaVeiculo() != null) {
            q.setParameter("pl_vei", sp.getPlacaVeiculo() + "%");
        }

        return q.getResultList();

    }
}
