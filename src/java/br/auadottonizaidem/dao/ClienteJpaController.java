/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.auadottonizaidem.dao;

import br.auadottonizaidem.dao.exceptions.NonexistentEntityException;
import br.auadottonizaidem.entity.Cliente;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.auadottonizaidem.entity.Entrega;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

/**
 *
 * @author Aparecida
 */
public class ClienteJpaController implements Serializable {

    public ClienteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cliente cliente) {
        if (cliente.getEntregaList() == null) {
            cliente.setEntregaList(new ArrayList<Entrega>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Entrega> attachedEntregaList = new ArrayList<Entrega>();
            for (Entrega entregaListEntregaToAttach : cliente.getEntregaList()) {
                entregaListEntregaToAttach = em.getReference(entregaListEntregaToAttach.getClass(), entregaListEntregaToAttach.getIdEntrega());
                attachedEntregaList.add(entregaListEntregaToAttach);
            }
            cliente.setEntregaList(attachedEntregaList);
            em.persist(cliente);
            for (Entrega entregaListEntrega : cliente.getEntregaList()) {
                Cliente oldIdClienteOfEntregaListEntrega = entregaListEntrega.getIdCliente();
                entregaListEntrega.setIdCliente(cliente);
                entregaListEntrega = em.merge(entregaListEntrega);
                if (oldIdClienteOfEntregaListEntrega != null) {
                    oldIdClienteOfEntregaListEntrega.getEntregaList().remove(entregaListEntrega);
                    oldIdClienteOfEntregaListEntrega = em.merge(oldIdClienteOfEntregaListEntrega);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cliente cliente) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente persistentCliente = em.find(Cliente.class, cliente.getIdCliente());
            List<Entrega> entregaListOld = persistentCliente.getEntregaList();
            List<Entrega> entregaListNew = cliente.getEntregaList();
            List<Entrega> attachedEntregaListNew = new ArrayList<Entrega>();
            for (Entrega entregaListNewEntregaToAttach : entregaListNew) {
                entregaListNewEntregaToAttach = em.getReference(entregaListNewEntregaToAttach.getClass(), entregaListNewEntregaToAttach.getIdEntrega());
                attachedEntregaListNew.add(entregaListNewEntregaToAttach);
            }
            entregaListNew = attachedEntregaListNew;
            cliente.setEntregaList(entregaListNew);
            cliente = em.merge(cliente);
            for (Entrega entregaListOldEntrega : entregaListOld) {
                if (!entregaListNew.contains(entregaListOldEntrega)) {
                    entregaListOldEntrega.setIdCliente(null);
                    entregaListOldEntrega = em.merge(entregaListOldEntrega);
                }
            }
            for (Entrega entregaListNewEntrega : entregaListNew) {
                if (!entregaListOld.contains(entregaListNewEntrega)) {
                    Cliente oldIdClienteOfEntregaListNewEntrega = entregaListNewEntrega.getIdCliente();
                    entregaListNewEntrega.setIdCliente(cliente);
                    entregaListNewEntrega = em.merge(entregaListNewEntrega);
                    if (oldIdClienteOfEntregaListNewEntrega != null && !oldIdClienteOfEntregaListNewEntrega.equals(cliente)) {
                        oldIdClienteOfEntregaListNewEntrega.getEntregaList().remove(entregaListNewEntrega);
                        oldIdClienteOfEntregaListNewEntrega = em.merge(oldIdClienteOfEntregaListNewEntrega);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cliente.getIdCliente();
                if (findCliente(id) == null) {
                    throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente cliente;
            try {
                cliente = em.getReference(Cliente.class, id);
                cliente.getIdCliente();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.", enfe);
            }
            List<Entrega> entregaList = cliente.getEntregaList();
            for (Entrega entregaListEntrega : entregaList) {
                entregaListEntrega.setIdCliente(null);
                entregaListEntrega = em.merge(entregaListEntrega);
            }
            em.remove(cliente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cliente> findClienteEntities() {
        return findClienteEntities(true, -1, -1);
    }

    public List<Cliente> findClienteEntities(int maxResults, int firstResult) {
        return findClienteEntities(false, maxResults, firstResult);
    }

    private List<Cliente> findClienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cliente.class));
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

    public Cliente findCliente(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cliente.class, id);
        } finally {
            em.close();
        }
    }

    public int getClienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cliente> rt = cq.from(Cliente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public Cliente findUserByLoginAndSenha(String login, String senha) {
         try {
            String sql = "select u from Cliente u where u.user=:login and u.senha = :senha";
            Query q = getEntityManager().createQuery(sql);
            q.setParameter("login", login);
            q.setParameter("senha", senha);

            return (Cliente) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
       
    }
    
    public Cliente findUserById(Cliente id) {
         try {
            String sql = "select u from Cliente u where u.cli=:id";
            Query q = getEntityManager().createQuery(sql);
            q.setParameter("id", id);
            
            return (Cliente) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
       
    }
    
      public Cliente findUserBySenha(String senha) {
        try {
            String sql = "select s from Cliente s where  s.senha = :senha";
            Query q = getEntityManager().createQuery(sql);
            q.setParameter("senha", senha);

            return (Cliente) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
