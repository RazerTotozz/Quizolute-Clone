/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quizolute.jpa;

import com.quizolute.jpa.exceptions.IllegalOrphanException;
import com.quizolute.jpa.exceptions.NonexistentEntityException;
import com.quizolute.jpa.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.quizolute.model.QuestionSets;
import com.quizolute.model.Choices;
import com.quizolute.model.Questions;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author sittiwatlcp
 */
public class QuestionsJpaController implements Serializable {

    public QuestionsJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Questions questions) throws RollbackFailureException, Exception {
        if (questions.getChoicesList() == null) {
            questions.setChoicesList(new ArrayList<Choices>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            QuestionSets questionSetId = questions.getQuestionSetId();
            if (questionSetId != null) {
                questionSetId = em.getReference(questionSetId.getClass(), questionSetId.getId());
                questions.setQuestionSetId(questionSetId);
            }
            List<Choices> attachedChoicesList = new ArrayList<Choices>();
            for (Choices choicesListChoicesToAttach : questions.getChoicesList()) {
                choicesListChoicesToAttach = em.getReference(choicesListChoicesToAttach.getClass(), choicesListChoicesToAttach.getId());
                attachedChoicesList.add(choicesListChoicesToAttach);
            }
            questions.setChoicesList(attachedChoicesList);
            em.persist(questions);
            if (questionSetId != null) {
                questionSetId.getQuestionsList().add(questions);
                questionSetId = em.merge(questionSetId);
            }
            for (Choices choicesListChoices : questions.getChoicesList()) {
                Questions oldQuestionIdOfChoicesListChoices = choicesListChoices.getQuestionId();
                choicesListChoices.setQuestionId(questions);
                choicesListChoices = em.merge(choicesListChoices);
                if (oldQuestionIdOfChoicesListChoices != null) {
                    oldQuestionIdOfChoicesListChoices.getChoicesList().remove(choicesListChoices);
                    oldQuestionIdOfChoicesListChoices = em.merge(oldQuestionIdOfChoicesListChoices);
                }
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

    public void edit(Questions questions) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Questions persistentQuestions = em.find(Questions.class, questions.getId());
            QuestionSets questionSetIdOld = persistentQuestions.getQuestionSetId();
            QuestionSets questionSetIdNew = questions.getQuestionSetId();
            List<Choices> choicesListOld = persistentQuestions.getChoicesList();
            List<Choices> choicesListNew = questions.getChoicesList();
            List<String> illegalOrphanMessages = null;
            for (Choices choicesListOldChoices : choicesListOld) {
                if (!choicesListNew.contains(choicesListOldChoices)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Choices " + choicesListOldChoices + " since its questionId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (questionSetIdNew != null) {
                questionSetIdNew = em.getReference(questionSetIdNew.getClass(), questionSetIdNew.getId());
                questions.setQuestionSetId(questionSetIdNew);
            }
            List<Choices> attachedChoicesListNew = new ArrayList<Choices>();
            for (Choices choicesListNewChoicesToAttach : choicesListNew) {
                choicesListNewChoicesToAttach = em.getReference(choicesListNewChoicesToAttach.getClass(), choicesListNewChoicesToAttach.getId());
                attachedChoicesListNew.add(choicesListNewChoicesToAttach);
            }
            choicesListNew = attachedChoicesListNew;
            questions.setChoicesList(choicesListNew);
            questions = em.merge(questions);
            if (questionSetIdOld != null && !questionSetIdOld.equals(questionSetIdNew)) {
                questionSetIdOld.getQuestionsList().remove(questions);
                questionSetIdOld = em.merge(questionSetIdOld);
            }
            if (questionSetIdNew != null && !questionSetIdNew.equals(questionSetIdOld)) {
                questionSetIdNew.getQuestionsList().add(questions);
                questionSetIdNew = em.merge(questionSetIdNew);
            }
            for (Choices choicesListNewChoices : choicesListNew) {
                if (!choicesListOld.contains(choicesListNewChoices)) {
                    Questions oldQuestionIdOfChoicesListNewChoices = choicesListNewChoices.getQuestionId();
                    choicesListNewChoices.setQuestionId(questions);
                    choicesListNewChoices = em.merge(choicesListNewChoices);
                    if (oldQuestionIdOfChoicesListNewChoices != null && !oldQuestionIdOfChoicesListNewChoices.equals(questions)) {
                        oldQuestionIdOfChoicesListNewChoices.getChoicesList().remove(choicesListNewChoices);
                        oldQuestionIdOfChoicesListNewChoices = em.merge(oldQuestionIdOfChoicesListNewChoices);
                    }
                }
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
                Integer id = questions.getId();
                if (findQuestions(id) == null) {
                    throw new NonexistentEntityException("The questions with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Questions questions;
            try {
                questions = em.getReference(Questions.class, id);
                questions.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The questions with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Choices> choicesListOrphanCheck = questions.getChoicesList();
            for (Choices choicesListOrphanCheckChoices : choicesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Questions (" + questions + ") cannot be destroyed since the Choices " + choicesListOrphanCheckChoices + " in its choicesList field has a non-nullable questionId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            QuestionSets questionSetId = questions.getQuestionSetId();
            if (questionSetId != null) {
                questionSetId.getQuestionsList().remove(questions);
                questionSetId = em.merge(questionSetId);
            }
            em.remove(questions);
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

    public List<Questions> findQuestionsEntities() {
        return findQuestionsEntities(true, -1, -1);
    }

    public List<Questions> findQuestionsEntities(int maxResults, int firstResult) {
        return findQuestionsEntities(false, maxResults, firstResult);
    }

    private List<Questions> findQuestionsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Questions.class));
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

    public Questions findQuestions(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Questions.class, id);
        } finally {
            em.close();
        }
    }

    public int getQuestionsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Questions> rt = cq.from(Questions.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
