/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.auadottonizaidem.dao;

import br.auadottonizaidem.dao.exceptions.IllegalOrphanException;
import br.auadottonizaidem.dao.exceptions.NonexistentEntityException;
import br.auadottonizaidem.entity.Localidade;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.auadottonizaidem.entity.Rota;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Aparecida
 */
public class LocalidadeJpaController implements Serializable {

    public LocalidadeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Localidade localidade) {
        if (localidade.getRotaList() == null) {
            localidade.setRotaList(new ArrayList<Rota>());
        }
        if (localidade.getRotaList1() == null) {
            localidade.setRotaList1(new ArrayList<Rota>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Rota> attachedRotaList = new ArrayList<Rota>();
            for (Rota rotaListRotaToAttach : localidade.getRotaList()) {
                rotaListRotaToAttach = em.getReference(rotaListRotaToAttach.getClass(), rotaListRotaToAttach.getIdRota());
                attachedRotaList.add(rotaListRotaToAttach);
            }
            localidade.setRotaList(attachedRotaList);
            List<Rota> attachedRotaList1 = new ArrayList<Rota>();
            for (Rota rotaList1RotaToAttach : localidade.getRotaList1()) {
                rotaList1RotaToAttach = em.getReference(rotaList1RotaToAttach.getClass(), rotaList1RotaToAttach.getIdRota());
                attachedRotaList1.add(rotaList1RotaToAttach);
            }
            localidade.setRotaList1(attachedRotaList1);
            em.persist(localidade);
            for (Rota rotaListRota : localidade.getRotaList()) {
                Localidade oldLocOrigemOfRotaListRota = rotaListRota.getLocOrigem();
                rotaListRota.setLocOrigem(localidade);
                rotaListRota = em.merge(rotaListRota);
                if (oldLocOrigemOfRotaListRota != null) {
                    oldLocOrigemOfRotaListRota.getRotaList().remove(rotaListRota);
                    oldLocOrigemOfRotaListRota = em.merge(oldLocOrigemOfRotaListRota);
                }
            }
            for (Rota rotaList1Rota : localidade.getRotaList1()) {
                Localidade oldLocDestinoOfRotaList1Rota = rotaList1Rota.getLocDestino();
                rotaList1Rota.setLocDestino(localidade);
                rotaList1Rota = em.merge(rotaList1Rota);
                if (oldLocDestinoOfRotaList1Rota != null) {
                    oldLocDestinoOfRotaList1Rota.getRotaList1().remove(rotaList1Rota);
                    oldLocDestinoOfRotaList1Rota = em.merge(oldLocDestinoOfRotaList1Rota);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Localidade localidade) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Localidade persistentLocalidade = em.find(Localidade.class, localidade.getIdLocalizacao());
            List<Rota> rotaListOld = persistentLocalidade.getRotaList();
            List<Rota> rotaListNew = localidade.getRotaList();
            List<Rota> rotaList1Old = persistentLocalidade.getRotaList1();
            List<Rota> rotaList1New = localidade.getRotaList1();
            List<String> illegalOrphanMessages = null;
            for (Rota rotaListOldRota : rotaListOld) {
                if (!rotaListNew.contains(rotaListOldRota)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Rota " + rotaListOldRota + " since its locOrigem field is not nullable.");
                }
            }
            for (Rota rotaList1OldRota : rotaList1Old) {
                if (!rotaList1New.contains(rotaList1OldRota)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Rota " + rotaList1OldRota + " since its locDestino field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Rota> attachedRotaListNew = new ArrayList<Rota>();
            for (Rota rotaListNewRotaToAttach : rotaListNew) {
                rotaListNewRotaToAttach = em.getReference(rotaListNewRotaToAttach.getClass(), rotaListNewRotaToAttach.getIdRota());
                attachedRotaListNew.add(rotaListNewRotaToAttach);
            }
            rotaListNew = attachedRotaListNew;
            localidade.setRotaList(rotaListNew);
            List<Rota> attachedRotaList1New = new ArrayList<Rota>();
            for (Rota rotaList1NewRotaToAttach : rotaList1New) {
                rotaList1NewRotaToAttach = em.getReference(rotaList1NewRotaToAttach.getClass(), rotaList1NewRotaToAttach.getIdRota());
                attachedRotaList1New.add(rotaList1NewRotaToAttach);
            }
            rotaList1New = attachedRotaList1New;
            localidade.setRotaList1(rotaList1New);
            localidade = em.merge(localidade);
            for (Rota rotaListNewRota : rotaListNew) {
                if (!rotaListOld.contains(rotaListNewRota)) {
                    Localidade oldLocOrigemOfRotaListNewRota = rotaListNewRota.getLocOrigem();
                    rotaListNewRota.setLocOrigem(localidade);
                    rotaListNewRota = em.merge(rotaListNewRota);
                    if (oldLocOrigemOfRotaListNewRota != null && !oldLocOrigemOfRotaListNewRota.equals(localidade)) {
                        oldLocOrigemOfRotaListNewRota.getRotaList().remove(rotaListNewRota);
                        oldLocOrigemOfRotaListNewRota = em.merge(oldLocOrigemOfRotaListNewRota);
                    }
                }
            }
            for (Rota rotaList1NewRota : rotaList1New) {
                if (!rotaList1Old.contains(rotaList1NewRota)) {
                    Localidade oldLocDestinoOfRotaList1NewRota = rotaList1NewRota.getLocDestino();
                    rotaList1NewRota.setLocDestino(localidade);
                    rotaList1NewRota = em.merge(rotaList1NewRota);
                    if (oldLocDestinoOfRotaList1NewRota != null && !oldLocDestinoOfRotaList1NewRota.equals(localidade)) {
                        oldLocDestinoOfRotaList1NewRota.getRotaList1().remove(rotaList1NewRota);
                        oldLocDestinoOfRotaList1NewRota = em.merge(oldLocDestinoOfRotaList1NewRota);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = localidade.getIdLocalizacao();
                if (findLocalidade(id) == null) {
                    throw new NonexistentEntityException("The localidade with id " + id + " no longer exists.");
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
            Localidade localidade;
            try {
                localidade = em.getReference(Localidade.class, id);
                localidade.getIdLocalizacao();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The localidade with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Rota> rotaListOrphanCheck = localidade.getRotaList();
            for (Rota rotaListOrphanCheckRota : rotaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Localidade (" + localidade + ") cannot be destroyed since the Rota " + rotaListOrphanCheckRota + " in its rotaList field has a non-nullable locOrigem field.");
            }
            List<Rota> rotaList1OrphanCheck = localidade.getRotaList1();
            for (Rota rotaList1OrphanCheckRota : rotaList1OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Localidade (" + localidade + ") cannot be destroyed since the Rota " + rotaList1OrphanCheckRota + " in its rotaList1 field has a non-nullable locDestino field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(localidade);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Localidade> findLocalidadeEntities() {
        return findLocalidadeEntities(true, -1, -1);
    }

    public List<Localidade> findLocalidadeEntities(int maxResults, int firstResult) {
        return findLocalidadeEntities(false, maxResults, firstResult);
    }

    private List<Localidade> findLocalidadeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Localidade.class));
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

    public Localidade findLocalidade(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Localidade.class, id);
        } finally {
            em.close();
        }
    }

    public int getLocalidadeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Localidade> rt = cq.from(Localidade.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
