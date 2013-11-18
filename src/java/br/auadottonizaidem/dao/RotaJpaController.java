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
import br.auadottonizaidem.entity.Localidade;
import br.auadottonizaidem.entity.RotaPercurso;
import java.util.ArrayList;
import java.util.List;
import br.auadottonizaidem.entity.Entrega;
import br.auadottonizaidem.entity.Rota;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Aparecida
 */
public class RotaJpaController implements Serializable {

    public RotaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Rota rota) throws PreexistingEntityException, Exception {
        if (rota.getRotaPercursoList() == null) {
            rota.setRotaPercursoList(new ArrayList<RotaPercurso>());
        }
        if (rota.getEntregaList() == null) {
            rota.setEntregaList(new ArrayList<Entrega>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Localidade locOrigem = rota.getLocOrigem();
            if (locOrigem != null) {
                locOrigem = em.getReference(locOrigem.getClass(), locOrigem.getIdLocalizacao());
                rota.setLocOrigem(locOrigem);
            }
            Localidade locDestino = rota.getLocDestino();
            if (locDestino != null) {
                locDestino = em.getReference(locDestino.getClass(), locDestino.getIdLocalizacao());
                rota.setLocDestino(locDestino);
            }
            List<RotaPercurso> attachedRotaPercursoList = new ArrayList<RotaPercurso>();
            for (RotaPercurso rotaPercursoListRotaPercursoToAttach : rota.getRotaPercursoList()) {
                rotaPercursoListRotaPercursoToAttach = em.getReference(rotaPercursoListRotaPercursoToAttach.getClass(), rotaPercursoListRotaPercursoToAttach.getRotaPercursoPK());
                attachedRotaPercursoList.add(rotaPercursoListRotaPercursoToAttach);
            }
            rota.setRotaPercursoList(attachedRotaPercursoList);
            List<Entrega> attachedEntregaList = new ArrayList<Entrega>();
            for (Entrega entregaListEntregaToAttach : rota.getEntregaList()) {
                entregaListEntregaToAttach = em.getReference(entregaListEntregaToAttach.getClass(), entregaListEntregaToAttach.getIdEntrega());
                attachedEntregaList.add(entregaListEntregaToAttach);
            }
            rota.setEntregaList(attachedEntregaList);
            em.persist(rota);
            if (locOrigem != null) {
                locOrigem.getRotaList().add(rota);
                locOrigem = em.merge(locOrigem);
            }
            if (locDestino != null) {
                locDestino.getRotaList().add(rota);
                locDestino = em.merge(locDestino);
            }
            for (RotaPercurso rotaPercursoListRotaPercurso : rota.getRotaPercursoList()) {
                Rota oldRotaOfRotaPercursoListRotaPercurso = rotaPercursoListRotaPercurso.getRota();
                rotaPercursoListRotaPercurso.setRota(rota);
                rotaPercursoListRotaPercurso = em.merge(rotaPercursoListRotaPercurso);
                if (oldRotaOfRotaPercursoListRotaPercurso != null) {
                    oldRotaOfRotaPercursoListRotaPercurso.getRotaPercursoList().remove(rotaPercursoListRotaPercurso);
                    oldRotaOfRotaPercursoListRotaPercurso = em.merge(oldRotaOfRotaPercursoListRotaPercurso);
                }
            }
            for (Entrega entregaListEntrega : rota.getEntregaList()) {
                Rota oldIdRotaOfEntregaListEntrega = entregaListEntrega.getIdRota();
                entregaListEntrega.setIdRota(rota);
                entregaListEntrega = em.merge(entregaListEntrega);
                if (oldIdRotaOfEntregaListEntrega != null) {
                    oldIdRotaOfEntregaListEntrega.getEntregaList().remove(entregaListEntrega);
                    oldIdRotaOfEntregaListEntrega = em.merge(oldIdRotaOfEntregaListEntrega);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findRota(rota.getIdRota()) != null) {
                throw new PreexistingEntityException("Rota " + rota + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Rota rota) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Rota persistentRota = em.find(Rota.class, rota.getIdRota());
            Localidade locOrigemOld = persistentRota.getLocOrigem();
            Localidade locOrigemNew = rota.getLocOrigem();
            Localidade locDestinoOld = persistentRota.getLocDestino();
            Localidade locDestinoNew = rota.getLocDestino();
            List<RotaPercurso> rotaPercursoListOld = persistentRota.getRotaPercursoList();
            List<RotaPercurso> rotaPercursoListNew = rota.getRotaPercursoList();
            List<Entrega> entregaListOld = persistentRota.getEntregaList();
            List<Entrega> entregaListNew = rota.getEntregaList();
            List<String> illegalOrphanMessages = null;
            for (RotaPercurso rotaPercursoListOldRotaPercurso : rotaPercursoListOld) {
                if (!rotaPercursoListNew.contains(rotaPercursoListOldRotaPercurso)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RotaPercurso " + rotaPercursoListOldRotaPercurso + " since its rota field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (locOrigemNew != null) {
                locOrigemNew = em.getReference(locOrigemNew.getClass(), locOrigemNew.getIdLocalizacao());
                rota.setLocOrigem(locOrigemNew);
            }
            if (locDestinoNew != null) {
                locDestinoNew = em.getReference(locDestinoNew.getClass(), locDestinoNew.getIdLocalizacao());
                rota.setLocDestino(locDestinoNew);
            }
            List<RotaPercurso> attachedRotaPercursoListNew = new ArrayList<RotaPercurso>();
            for (RotaPercurso rotaPercursoListNewRotaPercursoToAttach : rotaPercursoListNew) {
                rotaPercursoListNewRotaPercursoToAttach = em.getReference(rotaPercursoListNewRotaPercursoToAttach.getClass(), rotaPercursoListNewRotaPercursoToAttach.getRotaPercursoPK());
                attachedRotaPercursoListNew.add(rotaPercursoListNewRotaPercursoToAttach);
            }
            rotaPercursoListNew = attachedRotaPercursoListNew;
            rota.setRotaPercursoList(rotaPercursoListNew);
            List<Entrega> attachedEntregaListNew = new ArrayList<Entrega>();
            for (Entrega entregaListNewEntregaToAttach : entregaListNew) {
                entregaListNewEntregaToAttach = em.getReference(entregaListNewEntregaToAttach.getClass(), entregaListNewEntregaToAttach.getIdEntrega());
                attachedEntregaListNew.add(entregaListNewEntregaToAttach);
            }
            entregaListNew = attachedEntregaListNew;
            rota.setEntregaList(entregaListNew);
            rota = em.merge(rota);
            if (locOrigemOld != null && !locOrigemOld.equals(locOrigemNew)) {
                locOrigemOld.getRotaList().remove(rota);
                locOrigemOld = em.merge(locOrigemOld);
            }
            if (locOrigemNew != null && !locOrigemNew.equals(locOrigemOld)) {
                locOrigemNew.getRotaList().add(rota);
                locOrigemNew = em.merge(locOrigemNew);
            }
            if (locDestinoOld != null && !locDestinoOld.equals(locDestinoNew)) {
                locDestinoOld.getRotaList().remove(rota);
                locDestinoOld = em.merge(locDestinoOld);
            }
            if (locDestinoNew != null && !locDestinoNew.equals(locDestinoOld)) {
                locDestinoNew.getRotaList().add(rota);
                locDestinoNew = em.merge(locDestinoNew);
            }
            for (RotaPercurso rotaPercursoListNewRotaPercurso : rotaPercursoListNew) {
                if (!rotaPercursoListOld.contains(rotaPercursoListNewRotaPercurso)) {
                    Rota oldRotaOfRotaPercursoListNewRotaPercurso = rotaPercursoListNewRotaPercurso.getRota();
                    rotaPercursoListNewRotaPercurso.setRota(rota);
                    rotaPercursoListNewRotaPercurso = em.merge(rotaPercursoListNewRotaPercurso);
                    if (oldRotaOfRotaPercursoListNewRotaPercurso != null && !oldRotaOfRotaPercursoListNewRotaPercurso.equals(rota)) {
                        oldRotaOfRotaPercursoListNewRotaPercurso.getRotaPercursoList().remove(rotaPercursoListNewRotaPercurso);
                        oldRotaOfRotaPercursoListNewRotaPercurso = em.merge(oldRotaOfRotaPercursoListNewRotaPercurso);
                    }
                }
            }
            for (Entrega entregaListOldEntrega : entregaListOld) {
                if (!entregaListNew.contains(entregaListOldEntrega)) {
                    entregaListOldEntrega.setIdRota(null);
                    entregaListOldEntrega = em.merge(entregaListOldEntrega);
                }
            }
            for (Entrega entregaListNewEntrega : entregaListNew) {
                if (!entregaListOld.contains(entregaListNewEntrega)) {
                    Rota oldIdRotaOfEntregaListNewEntrega = entregaListNewEntrega.getIdRota();
                    entregaListNewEntrega.setIdRota(rota);
                    entregaListNewEntrega = em.merge(entregaListNewEntrega);
                    if (oldIdRotaOfEntregaListNewEntrega != null && !oldIdRotaOfEntregaListNewEntrega.equals(rota)) {
                        oldIdRotaOfEntregaListNewEntrega.getEntregaList().remove(entregaListNewEntrega);
                        oldIdRotaOfEntregaListNewEntrega = em.merge(oldIdRotaOfEntregaListNewEntrega);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = rota.getIdRota();
                if (findRota(id) == null) {
                    throw new NonexistentEntityException("The rota with id " + id + " no longer exists.");
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
            Rota rota;
            try {
                rota = em.getReference(Rota.class, id);
                rota.getIdRota();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The rota with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<RotaPercurso> rotaPercursoListOrphanCheck = rota.getRotaPercursoList();
            for (RotaPercurso rotaPercursoListOrphanCheckRotaPercurso : rotaPercursoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Rota (" + rota + ") cannot be destroyed since the RotaPercurso " + rotaPercursoListOrphanCheckRotaPercurso + " in its rotaPercursoList field has a non-nullable rota field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Localidade locOrigem = rota.getLocOrigem();
            if (locOrigem != null) {
                locOrigem.getRotaList().remove(rota);
                locOrigem = em.merge(locOrigem);
            }
            Localidade locDestino = rota.getLocDestino();
            if (locDestino != null) {
                locDestino.getRotaList().remove(rota);
                locDestino = em.merge(locDestino);
            }
            List<Entrega> entregaList = rota.getEntregaList();
            for (Entrega entregaListEntrega : entregaList) {
                entregaListEntrega.setIdRota(null);
                entregaListEntrega = em.merge(entregaListEntrega);
            }
            em.remove(rota);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Rota> findRotaEntities() {
        return findRotaEntities(true, -1, -1);
    }

    public List<Rota> findRotaEntities(int maxResults, int firstResult) {
        return findRotaEntities(false, maxResults, firstResult);
    }

    private List<Rota> findRotaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Rota.class));
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

    public Rota findRota(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Rota.class, id);
        } finally {
            em.close();
        }
    }

    public int getRotaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Rota> rt = cq.from(Rota.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
