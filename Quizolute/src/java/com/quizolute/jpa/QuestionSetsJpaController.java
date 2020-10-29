/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quizolute.jpa;

import com.quizolute.jpa.exceptions.IllegalOrphanException;
import com.quizolute.jpa.exceptions.NonexistentEntityException;
import com.quizolute.jpa.exceptions.RollbackFailureException;
import com.quizolute.model.QuestionSets;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.quizolute.model.Users;
import com.quizolute.model.Rooms;
import java.util.ArrayList;
import java.util.List;
import com.quizolute.model.Questions;
import com.quizolute.model.UserAnswers;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author sittiwatlcp
 */
public class QuestionSetsJpaController implements Serializable {

    public QuestionSetsJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(QuestionSets questionSets) throws RollbackFailureException, Exception {
        if (questionSets.getRoomsList() == null) {
            questionSets.setRoomsList(new ArrayList<Rooms>());
        }
        if (questionSets.getQuestionsList() == null) {
            questionSets.setQuestionsList(new ArrayList<Questions>());
        }
        if (questionSets.getUserAnswersList() == null) {
            questionSets.setUserAnswersList(new ArrayList<UserAnswers>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Users ownerId = questionSets.getOwnerId();
            if (ownerId != null) {
                ownerId = em.getReference(ownerId.getClass(), ownerId.getId());
                questionSets.setOwnerId(ownerId);
            }
            List<Rooms> attachedRoomsList = new ArrayList<Rooms>();
            for (Rooms roomsListRoomsToAttach : questionSets.getRoomsList()) {
                roomsListRoomsToAttach = em.getReference(roomsListRoomsToAttach.getClass(), roomsListRoomsToAttach.getId());
                attachedRoomsList.add(roomsListRoomsToAttach);
            }
            questionSets.setRoomsList(attachedRoomsList);
            List<Questions> attachedQuestionsList = new ArrayList<Questions>();
            for (Questions questionsListQuestionsToAttach : questionSets.getQuestionsList()) {
                questionsListQuestionsToAttach = em.getReference(questionsListQuestionsToAttach.getClass(), questionsListQuestionsToAttach.getId());
                attachedQuestionsList.add(questionsListQuestionsToAttach);
            }
            questionSets.setQuestionsList(attachedQuestionsList);
            List<UserAnswers> attachedUserAnswersList = new ArrayList<UserAnswers>();
            for (UserAnswers userAnswersListUserAnswersToAttach : questionSets.getUserAnswersList()) {
                userAnswersListUserAnswersToAttach = em.getReference(userAnswersListUserAnswersToAttach.getClass(), userAnswersListUserAnswersToAttach.getId());
                attachedUserAnswersList.add(userAnswersListUserAnswersToAttach);
            }
            questionSets.setUserAnswersList(attachedUserAnswersList);
            em.persist(questionSets);
            if (ownerId != null) {
                ownerId.getQuestionSetsList().add(questionSets);
                ownerId = em.merge(ownerId);
            }
            for (Rooms roomsListRooms : questionSets.getRoomsList()) {
                QuestionSets oldQuestionSetIdOfRoomsListRooms = roomsListRooms.getQuestionSetId();
                roomsListRooms.setQuestionSetId(questionSets);
                roomsListRooms = em.merge(roomsListRooms);
                if (oldQuestionSetIdOfRoomsListRooms != null) {
                    oldQuestionSetIdOfRoomsListRooms.getRoomsList().remove(roomsListRooms);
                    oldQuestionSetIdOfRoomsListRooms = em.merge(oldQuestionSetIdOfRoomsListRooms);
                }
            }
            for (Questions questionsListQuestions : questionSets.getQuestionsList()) {
                QuestionSets oldQuestionSetIdOfQuestionsListQuestions = questionsListQuestions.getQuestionSetId();
                questionsListQuestions.setQuestionSetId(questionSets);
                questionsListQuestions = em.merge(questionsListQuestions);
                if (oldQuestionSetIdOfQuestionsListQuestions != null) {
                    oldQuestionSetIdOfQuestionsListQuestions.getQuestionsList().remove(questionsListQuestions);
                    oldQuestionSetIdOfQuestionsListQuestions = em.merge(oldQuestionSetIdOfQuestionsListQuestions);
                }
            }
            for (UserAnswers userAnswersListUserAnswers : questionSets.getUserAnswersList()) {
                QuestionSets oldQuestionSetIdOfUserAnswersListUserAnswers = userAnswersListUserAnswers.getQuestionSetId();
                userAnswersListUserAnswers.setQuestionSetId(questionSets);
                userAnswersListUserAnswers = em.merge(userAnswersListUserAnswers);
                if (oldQuestionSetIdOfUserAnswersListUserAnswers != null) {
                    oldQuestionSetIdOfUserAnswersListUserAnswers.getUserAnswersList().remove(userAnswersListUserAnswers);
                    oldQuestionSetIdOfUserAnswersListUserAnswers = em.merge(oldQuestionSetIdOfUserAnswersListUserAnswers);
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

    public void edit(QuestionSets questionSets) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            QuestionSets persistentQuestionSets = em.find(QuestionSets.class, questionSets.getId());
            Users ownerIdOld = persistentQuestionSets.getOwnerId();
            Users ownerIdNew = questionSets.getOwnerId();
            List<Rooms> roomsListOld = persistentQuestionSets.getRoomsList();
            List<Rooms> roomsListNew = questionSets.getRoomsList();
            List<Questions> questionsListOld = persistentQuestionSets.getQuestionsList();
            List<Questions> questionsListNew = questionSets.getQuestionsList();
            List<UserAnswers> userAnswersListOld = persistentQuestionSets.getUserAnswersList();
            List<UserAnswers> userAnswersListNew = questionSets.getUserAnswersList();
            List<String> illegalOrphanMessages = null;
            for (Rooms roomsListOldRooms : roomsListOld) {
                if (!roomsListNew.contains(roomsListOldRooms)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Rooms " + roomsListOldRooms + " since its questionSetId field is not nullable.");
                }
            }
            for (Questions questionsListOldQuestions : questionsListOld) {
                if (!questionsListNew.contains(questionsListOldQuestions)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Questions " + questionsListOldQuestions + " since its questionSetId field is not nullable.");
                }
            }
            for (UserAnswers userAnswersListOldUserAnswers : userAnswersListOld) {
                if (!userAnswersListNew.contains(userAnswersListOldUserAnswers)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain UserAnswers " + userAnswersListOldUserAnswers + " since its questionSetId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (ownerIdNew != null) {
                ownerIdNew = em.getReference(ownerIdNew.getClass(), ownerIdNew.getId());
                questionSets.setOwnerId(ownerIdNew);
            }
            List<Rooms> attachedRoomsListNew = new ArrayList<Rooms>();
            for (Rooms roomsListNewRoomsToAttach : roomsListNew) {
                roomsListNewRoomsToAttach = em.getReference(roomsListNewRoomsToAttach.getClass(), roomsListNewRoomsToAttach.getId());
                attachedRoomsListNew.add(roomsListNewRoomsToAttach);
            }
            roomsListNew = attachedRoomsListNew;
            questionSets.setRoomsList(roomsListNew);
            List<Questions> attachedQuestionsListNew = new ArrayList<Questions>();
            for (Questions questionsListNewQuestionsToAttach : questionsListNew) {
                questionsListNewQuestionsToAttach = em.getReference(questionsListNewQuestionsToAttach.getClass(), questionsListNewQuestionsToAttach.getId());
                attachedQuestionsListNew.add(questionsListNewQuestionsToAttach);
            }
            questionsListNew = attachedQuestionsListNew;
            questionSets.setQuestionsList(questionsListNew);
            List<UserAnswers> attachedUserAnswersListNew = new ArrayList<UserAnswers>();
            for (UserAnswers userAnswersListNewUserAnswersToAttach : userAnswersListNew) {
                userAnswersListNewUserAnswersToAttach = em.getReference(userAnswersListNewUserAnswersToAttach.getClass(), userAnswersListNewUserAnswersToAttach.getId());
                attachedUserAnswersListNew.add(userAnswersListNewUserAnswersToAttach);
            }
            userAnswersListNew = attachedUserAnswersListNew;
            questionSets.setUserAnswersList(userAnswersListNew);
            questionSets = em.merge(questionSets);
            if (ownerIdOld != null && !ownerIdOld.equals(ownerIdNew)) {
                ownerIdOld.getQuestionSetsList().remove(questionSets);
                ownerIdOld = em.merge(ownerIdOld);
            }
            if (ownerIdNew != null && !ownerIdNew.equals(ownerIdOld)) {
                ownerIdNew.getQuestionSetsList().add(questionSets);
                ownerIdNew = em.merge(ownerIdNew);
            }
            for (Rooms roomsListNewRooms : roomsListNew) {
                if (!roomsListOld.contains(roomsListNewRooms)) {
                    QuestionSets oldQuestionSetIdOfRoomsListNewRooms = roomsListNewRooms.getQuestionSetId();
                    roomsListNewRooms.setQuestionSetId(questionSets);
                    roomsListNewRooms = em.merge(roomsListNewRooms);
                    if (oldQuestionSetIdOfRoomsListNewRooms != null && !oldQuestionSetIdOfRoomsListNewRooms.equals(questionSets)) {
                        oldQuestionSetIdOfRoomsListNewRooms.getRoomsList().remove(roomsListNewRooms);
                        oldQuestionSetIdOfRoomsListNewRooms = em.merge(oldQuestionSetIdOfRoomsListNewRooms);
                    }
                }
            }
            for (Questions questionsListNewQuestions : questionsListNew) {
                if (!questionsListOld.contains(questionsListNewQuestions)) {
                    QuestionSets oldQuestionSetIdOfQuestionsListNewQuestions = questionsListNewQuestions.getQuestionSetId();
                    questionsListNewQuestions.setQuestionSetId(questionSets);
                    questionsListNewQuestions = em.merge(questionsListNewQuestions);
                    if (oldQuestionSetIdOfQuestionsListNewQuestions != null && !oldQuestionSetIdOfQuestionsListNewQuestions.equals(questionSets)) {
                        oldQuestionSetIdOfQuestionsListNewQuestions.getQuestionsList().remove(questionsListNewQuestions);
                        oldQuestionSetIdOfQuestionsListNewQuestions = em.merge(oldQuestionSetIdOfQuestionsListNewQuestions);
                    }
                }
            }
            for (UserAnswers userAnswersListNewUserAnswers : userAnswersListNew) {
                if (!userAnswersListOld.contains(userAnswersListNewUserAnswers)) {
                    QuestionSets oldQuestionSetIdOfUserAnswersListNewUserAnswers = userAnswersListNewUserAnswers.getQuestionSetId();
                    userAnswersListNewUserAnswers.setQuestionSetId(questionSets);
                    userAnswersListNewUserAnswers = em.merge(userAnswersListNewUserAnswers);
                    if (oldQuestionSetIdOfUserAnswersListNewUserAnswers != null && !oldQuestionSetIdOfUserAnswersListNewUserAnswers.equals(questionSets)) {
                        oldQuestionSetIdOfUserAnswersListNewUserAnswers.getUserAnswersList().remove(userAnswersListNewUserAnswers);
                        oldQuestionSetIdOfUserAnswersListNewUserAnswers = em.merge(oldQuestionSetIdOfUserAnswersListNewUserAnswers);
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
                Integer id = questionSets.getId();
                if (findQuestionSets(id) == null) {
                    throw new NonexistentEntityException("The questionSets with id " + id + " no longer exists.");
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
            QuestionSets questionSets;
            try {
                questionSets = em.getReference(QuestionSets.class, id);
                questionSets.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The questionSets with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Rooms> roomsListOrphanCheck = questionSets.getRoomsList();
            for (Rooms roomsListOrphanCheckRooms : roomsListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This QuestionSets (" + questionSets + ") cannot be destroyed since the Rooms " + roomsListOrphanCheckRooms + " in its roomsList field has a non-nullable questionSetId field.");
            }
            List<Questions> questionsListOrphanCheck = questionSets.getQuestionsList();
            for (Questions questionsListOrphanCheckQuestions : questionsListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This QuestionSets (" + questionSets + ") cannot be destroyed since the Questions " + questionsListOrphanCheckQuestions + " in its questionsList field has a non-nullable questionSetId field.");
            }
            List<UserAnswers> userAnswersListOrphanCheck = questionSets.getUserAnswersList();
            for (UserAnswers userAnswersListOrphanCheckUserAnswers : userAnswersListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This QuestionSets (" + questionSets + ") cannot be destroyed since the UserAnswers " + userAnswersListOrphanCheckUserAnswers + " in its userAnswersList field has a non-nullable questionSetId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Users ownerId = questionSets.getOwnerId();
            if (ownerId != null) {
                ownerId.getQuestionSetsList().remove(questionSets);
                ownerId = em.merge(ownerId);
            }
            em.remove(questionSets);
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

    public List<QuestionSets> findQuestionSetsEntities() {
        return findQuestionSetsEntities(true, -1, -1);
    }

    public List<QuestionSets> findQuestionSetsEntities(int maxResults, int firstResult) {
        return findQuestionSetsEntities(false, maxResults, firstResult);
    }

    private List<QuestionSets> findQuestionSetsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(QuestionSets.class));
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

    public QuestionSets findQuestionSets(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(QuestionSets.class, id);
        } finally {
            em.close();
        }
    }

    public int getQuestionSetsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<QuestionSets> rt = cq.from(QuestionSets.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
