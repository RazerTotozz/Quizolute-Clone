/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quizolute.jpa;

import com.quizolute.jpa.exceptions.NonexistentEntityException;
import com.quizolute.jpa.exceptions.RollbackFailureException;
import com.quizolute.model.Choices;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.quizolute.model.Questions;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author sittiwatlcp
 */
public class ChoicesJpaController implements Serializable {

    public ChoicesJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Choices choices) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Questions questionId = choices.getQuestionId();
            if (questionId != null) {
                questionId = em.getReference(questionId.getClass(), questionId.getId());
                choices.setQuestionId(questionId);
            }
            em.persist(choices);
            if (questionId != null) {
                questionId.getChoicesList().add(choices);
                questionId = em.merge(questionId);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Choices choices) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Choices persistentChoices = em.find(Choices.class, choices.getId());
            Questions questionIdOld = persistentChoices.getQuestionId();
            Questions questionIdNew = choices.getQuestionId();
            if (questionIdNew != null) {
                questionIdNew = em.getReference(questionIdNew.getClass(), questionIdNew.getId());
                choices.setQuestionId(questionIdNew);
            }
            choices = em.merge(choices);
            if (questionIdOld != null && !questionIdOld.equals(questionIdNew)) {
                questionIdOld.getChoicesList().remove(choices);
                questionIdOld = em.merge(questionIdOld);
            }
            if (questionIdNew != null && !questionIdNew.equals(questionIdOld)) {
                questionIdNew.getChoicesList().add(choices);
                questionIdNew = em.merge(questionIdNew);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = choices.getId();
                if (findChoices(id) == null) {
                    throw new NonexistentEntityException("The choices with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Choices choices;
            try {
                choices = em.getReference(Choices.class, id);
                choices.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The choices with id " + id + " no longer exists.", enfe);
            }
            Questions questionId = choices.getQuestionId();
            if (questionId != null) {
                questionId.getChoicesList().remove(choices);
                questionId = em.merge(questionId);
            }
            em.remove(choices);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Choices> findChoicesEntities() {
        return findChoicesEntities(true, -1, -1);
    }

    public List<Choices> findChoicesEntities(int maxResults, int firstResult) {
        return findChoicesEntities(false, maxResults, firstResult);
    }

    private List<Choices> findChoicesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Choices.class));
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

    public Choices findChoices(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Choices.class, id);
        } finally {
            em.close();
        }
    }

    public int getChoicesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Choices> rt = cq.from(Choices.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
