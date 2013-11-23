/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.auadottonizaidem.dao;

import br.auadottonizaidem.dao.exceptions.IllegalOrphanException;
import br.auadottonizaidem.dao.exceptions.NonexistentEntityException;
import br.auadottonizaidem.entity.PontoReferencia;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.auadottonizaidem.entity.RotaPercurso;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Aparecida
 */
public class PontoReferenciaJpaController implements Serializable {

    public PontoReferenciaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PontoReferencia pontoReferencia) {
        if (pontoReferencia.getRotaPercursoList() == null) {
            pontoReferencia.setRotaPercursoList(new ArrayList<RotaPercurso>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<RotaPercurso> attachedRotaPercursoList = new ArrayList<RotaPercurso>();
            for (RotaPercurso rotaPercursoListRotaPercursoToAttach : pontoReferencia.getRotaPercursoList()) {
                rotaPercursoListRotaPercursoToAttach = em.getReference(rotaPercursoListRotaPercursoToAttach.getClass(), rotaPercursoListRotaPercursoToAttach.getRotaPercursoPK());
                attachedRotaPercursoList.add(rotaPercursoListRotaPercursoToAttach);
            }
            pontoReferencia.setRotaPercursoList(attachedRotaPercursoList);
            em.persist(pontoReferencia);
            for (RotaPercurso rotaPercursoListRotaPercurso : pontoReferencia.getRotaPercursoList()) {
                PontoReferencia oldPontoReferenciaOfRotaPercursoListRotaPercurso = rotaPercursoListRotaPercurso.getPontoReferencia();
                rotaPercursoListRotaPercurso.setPontoReferencia(pontoReferencia);
                rotaPercursoListRotaPercurso = em.merge(rotaPercursoListRotaPercurso);
                if (oldPontoReferenciaOfRotaPercursoListRotaPercurso != null) {
                    oldPontoReferenciaOfRotaPercursoListRotaPercurso.getRotaPercursoList().remove(rotaPercursoListRotaPercurso);
                    oldPontoReferenciaOfRotaPercursoListRotaPercurso = em.merge(oldPontoReferenciaOfRotaPercursoListRotaPercurso);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PontoReferencia pontoReferencia) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PontoReferencia persistentPontoReferencia = em.find(PontoReferencia.class, pontoReferencia.getIdPontoReferencia());
            List<RotaPercurso> rotaPercursoListOld = persistentPontoReferencia.getRotaPercursoList();
            List<RotaPercurso> rotaPercursoListNew = pontoReferencia.getRotaPercursoList();
            List<String> illegalOrphanMessages = null;
            for (RotaPercurso rotaPercursoListOldRotaPercurso : rotaPercursoListOld) {
                if (!rotaPercursoListNew.contains(rotaPercursoListOldRotaPercurso)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RotaPercurso " + rotaPercursoListOldRotaPercurso + " since its pontoReferencia field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<RotaPercurso> attachedRotaPercursoListNew = new ArrayList<RotaPercurso>();
            for (RotaPercurso rotaPercursoListNewRotaPercursoToAttach : rotaPercursoListNew) {
                rotaPercursoListNewRotaPercursoToAttach = em.getReference(rotaPercursoListNewRotaPercursoToAttach.getClass(), rotaPercursoListNewRotaPercursoToAttach.getRotaPercursoPK());
                attachedRotaPercursoListNew.add(rotaPercursoListNewRotaPercursoToAttach);
            }
            rotaPercursoListNew = attachedRotaPercursoListNew;
            pontoReferencia.setRotaPercursoList(rotaPercursoListNew);
            pontoReferencia = em.merge(pontoReferencia);
            for (RotaPercurso rotaPercursoListNewRotaPercurso : rotaPercursoListNew) {
                if (!rotaPercursoListOld.contains(rotaPercursoListNewRotaPercurso)) {
                    PontoReferencia oldPontoReferenciaOfRotaPercursoListNewRotaPercurso = rotaPercursoListNewRotaPercurso.getPontoReferencia();
                    rotaPercursoListNewRotaPercurso.setPontoReferencia(pontoReferencia);
                    rotaPercursoListNewRotaPercurso = em.merge(rotaPercursoListNewRotaPercurso);
                    if (oldPontoReferenciaOfRotaPercursoListNewRotaPercurso != null && !oldPontoReferenciaOfRotaPercursoListNewRotaPercurso.equals(pontoReferencia)) {
                        oldPontoReferenciaOfRotaPercursoListNewRotaPercurso.getRotaPercursoList().remove(rotaPercursoListNewRotaPercurso);
                        oldPontoReferenciaOfRotaPercursoListNewRotaPercurso = em.merge(oldPontoReferenciaOfRotaPercursoListNewRotaPercurso);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pontoReferencia.getIdPontoReferencia();
                if (findPontoReferencia(id) == null) {
                    throw new NonexistentEntityException("The pontoReferencia with id " + id + " no longer exists.");
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
            PontoReferencia pontoReferencia;
            try {
                pontoReferencia = em.getReference(PontoReferencia.class, id);
                pontoReferencia.getIdPontoReferencia();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pontoReferencia with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<RotaPercurso> rotaPercursoListOrphanCheck = pontoReferencia.getRotaPercursoList();
            for (RotaPercurso rotaPercursoListOrphanCheckRotaPercurso : rotaPercursoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This PontoReferencia (" + pontoReferencia + ") cannot be destroyed since the RotaPercurso " + rotaPercursoListOrphanCheckRotaPercurso + " in its rotaPercursoList field has a non-nullable pontoReferencia field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(pontoReferencia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PontoReferencia> findPontoReferenciaEntities() {
        return findPontoReferenciaEntities(true, -1, -1);
    }

    public List<PontoReferencia> findPontoReferenciaEntities(int maxResults, int firstResult) {
        return findPontoReferenciaEntities(false, maxResults, firstResult);
    }

    private List<PontoReferencia> findPontoReferenciaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PontoReferencia.class));
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

    public PontoReferencia findPontoReferencia(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PontoReferencia.class, id);
        } finally {
            em.close();
        }
    }

    public int getPontoReferenciaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PontoReferencia> rt = cq.from(PontoReferencia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
