/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quizolute.jpa;

import com.quizolute.jpa.exceptions.NonexistentEntityException;
import com.quizolute.jpa.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.quizolute.model.QuestionSets;
import com.quizolute.model.Rooms;
import com.quizolute.model.UserAnswers;
import com.quizolute.model.Users;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author sittiwatlcp
 */
public class UserAnswersJpaController implements Serializable {

    public UserAnswersJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UserAnswers userAnswers) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            QuestionSets questionSetId = userAnswers.getQuestionSetId();
            if (questionSetId != null) {
                questionSetId = em.getReference(questionSetId.getClass(), questionSetId.getId());
                userAnswers.setQuestionSetId(questionSetId);
            }
            Rooms roomId = userAnswers.getRoomId();
            if (roomId != null) {
                roomId = em.getReference(roomId.getClass(), roomId.getId());
                userAnswers.setRoomId(roomId);
            }
            Users userId = userAnswers.getUserId();
            if (userId != null) {
                userId = em.getReference(userId.getClass(), userId.getId());
                userAnswers.setUserId(userId);
            }
            em.persist(userAnswers);
            if (questionSetId != null) {
                questionSetId.getUserAnswersList().add(userAnswers);
                questionSetId = em.merge(questionSetId);
            }
            if (roomId != null) {
                roomId.getUserAnswersList().add(userAnswers);
                roomId = em.merge(roomId);
            }
            if (userId != null) {
                userId.getUserAnswersList().add(userAnswers);
                userId = em.merge(userId);
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

    public void edit(UserAnswers userAnswers) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            UserAnswers persistentUserAnswers = em.find(UserAnswers.class, userAnswers.getId());
            QuestionSets questionSetIdOld = persistentUserAnswers.getQuestionSetId();
            QuestionSets questionSetIdNew = userAnswers.getQuestionSetId();
            Rooms roomIdOld = persistentUserAnswers.getRoomId();
            Rooms roomIdNew = userAnswers.getRoomId();
            Users userIdOld = persistentUserAnswers.getUserId();
            Users userIdNew = userAnswers.getUserId();
            if (questionSetIdNew != null) {
                questionSetIdNew = em.getReference(questionSetIdNew.getClass(), questionSetIdNew.getId());
                userAnswers.setQuestionSetId(questionSetIdNew);
            }
            if (roomIdNew != null) {
                roomIdNew = em.getReference(roomIdNew.getClass(), roomIdNew.getId());
                userAnswers.setRoomId(roomIdNew);
            }
            if (userIdNew != null) {
                userIdNew = em.getReference(userIdNew.getClass(), userIdNew.getId());
                userAnswers.setUserId(userIdNew);
            }
            userAnswers = em.merge(userAnswers);
            if (questionSetIdOld != null && !questionSetIdOld.equals(questionSetIdNew)) {
                questionSetIdOld.getUserAnswersList().remove(userAnswers);
                questionSetIdOld = em.merge(questionSetIdOld);
            }
            if (questionSetIdNew != null && !questionSetIdNew.equals(questionSetIdOld)) {
                questionSetIdNew.getUserAnswersList().add(userAnswers);
                questionSetIdNew = em.merge(questionSetIdNew);
            }
            if (roomIdOld != null && !roomIdOld.equals(roomIdNew)) {
                roomIdOld.getUserAnswersList().remove(userAnswers);
                roomIdOld = em.merge(roomIdOld);
            }
            if (roomIdNew != null && !roomIdNew.equals(roomIdOld)) {
                roomIdNew.getUserAnswersList().add(userAnswers);
                roomIdNew = em.merge(roomIdNew);
            }
            if (userIdOld != null && !userIdOld.equals(userIdNew)) {
                userIdOld.getUserAnswersList().remove(userAnswers);
                userIdOld = em.merge(userIdOld);
            }
            if (userIdNew != null && !userIdNew.equals(userIdOld)) {
                userIdNew.getUserAnswersList().add(userAnswers);
                userIdNew = em.merge(userIdNew);
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
                Integer id = userAnswers.getId();
                if (findUserAnswers(id) == null) {
                    throw new NonexistentEntityException("The userAnswers with id " + id + " no longer exists.");
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
            UserAnswers userAnswers;
            try {
                userAnswers = em.getReference(UserAnswers.class, id);
                userAnswers.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The userAnswers with id " + id + " no longer exists.", enfe);
            }
            QuestionSets questionSetId = userAnswers.getQuestionSetId();
            if (questionSetId != null) {
                questionSetId.getUserAnswersList().remove(userAnswers);
                questionSetId = em.merge(questionSetId);
            }
            Rooms roomId = userAnswers.getRoomId();
            if (roomId != null) {
                roomId.getUserAnswersList().remove(userAnswers);
                roomId = em.merge(roomId);
            }
            Users userId = userAnswers.getUserId();
            if (userId != null) {
                userId.getUserAnswersList().remove(userAnswers);
                userId = em.merge(userId);
            }
            em.remove(userAnswers);
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

    public List<UserAnswers> findUserAnswersEntities() {
        return findUserAnswersEntities(true, -1, -1);
    }

    public List<UserAnswers> findUserAnswersEntities(int maxResults, int firstResult) {
        return findUserAnswersEntities(false, maxResults, firstResult);
    }

    private List<UserAnswers> findUserAnswersEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UserAnswers.class));
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

    public UserAnswers findUserAnswers(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UserAnswers.class, id);
        } finally {
            em.close();
        }
    }

    public int getUserAnswersCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UserAnswers> rt = cq.from(UserAnswers.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
