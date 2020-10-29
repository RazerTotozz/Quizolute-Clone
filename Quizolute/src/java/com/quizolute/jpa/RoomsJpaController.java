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
import com.quizolute.model.Rooms;
import com.quizolute.model.Users;
import com.quizolute.model.UserInRoom;
import java.util.ArrayList;
import java.util.List;
import com.quizolute.model.UserAnswers;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author sittiwatlcp
 */
public class RoomsJpaController implements Serializable {

    public RoomsJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Rooms rooms) throws RollbackFailureException, Exception {
        if (rooms.getUserInRoomList() == null) {
            rooms.setUserInRoomList(new ArrayList<UserInRoom>());
        }
        if (rooms.getUserAnswersList() == null) {
            rooms.setUserAnswersList(new ArrayList<UserAnswers>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            QuestionSets questionSetId = rooms.getQuestionSetId();
            if (questionSetId != null) {
                questionSetId = em.getReference(questionSetId.getClass(), questionSetId.getId());
                rooms.setQuestionSetId(questionSetId);
            }
            Users ownerId = rooms.getOwnerId();
            if (ownerId != null) {
                ownerId = em.getReference(ownerId.getClass(), ownerId.getId());
                rooms.setOwnerId(ownerId);
            }
            List<UserInRoom> attachedUserInRoomList = new ArrayList<UserInRoom>();
            for (UserInRoom userInRoomListUserInRoomToAttach : rooms.getUserInRoomList()) {
                userInRoomListUserInRoomToAttach = em.getReference(userInRoomListUserInRoomToAttach.getClass(), userInRoomListUserInRoomToAttach.getId());
                attachedUserInRoomList.add(userInRoomListUserInRoomToAttach);
            }
            rooms.setUserInRoomList(attachedUserInRoomList);
            List<UserAnswers> attachedUserAnswersList = new ArrayList<UserAnswers>();
            for (UserAnswers userAnswersListUserAnswersToAttach : rooms.getUserAnswersList()) {
                userAnswersListUserAnswersToAttach = em.getReference(userAnswersListUserAnswersToAttach.getClass(), userAnswersListUserAnswersToAttach.getId());
                attachedUserAnswersList.add(userAnswersListUserAnswersToAttach);
            }
            rooms.setUserAnswersList(attachedUserAnswersList);
            em.persist(rooms);
            if (questionSetId != null) {
                questionSetId.getRoomsList().add(rooms);
                questionSetId = em.merge(questionSetId);
            }
            if (ownerId != null) {
                ownerId.getRoomsList().add(rooms);
                ownerId = em.merge(ownerId);
            }
            for (UserInRoom userInRoomListUserInRoom : rooms.getUserInRoomList()) {
                Rooms oldRoomIdOfUserInRoomListUserInRoom = userInRoomListUserInRoom.getRoomId();
                userInRoomListUserInRoom.setRoomId(rooms);
                userInRoomListUserInRoom = em.merge(userInRoomListUserInRoom);
                if (oldRoomIdOfUserInRoomListUserInRoom != null) {
                    oldRoomIdOfUserInRoomListUserInRoom.getUserInRoomList().remove(userInRoomListUserInRoom);
                    oldRoomIdOfUserInRoomListUserInRoom = em.merge(oldRoomIdOfUserInRoomListUserInRoom);
                }
            }
            for (UserAnswers userAnswersListUserAnswers : rooms.getUserAnswersList()) {
                Rooms oldRoomIdOfUserAnswersListUserAnswers = userAnswersListUserAnswers.getRoomId();
                userAnswersListUserAnswers.setRoomId(rooms);
                userAnswersListUserAnswers = em.merge(userAnswersListUserAnswers);
                if (oldRoomIdOfUserAnswersListUserAnswers != null) {
                    oldRoomIdOfUserAnswersListUserAnswers.getUserAnswersList().remove(userAnswersListUserAnswers);
                    oldRoomIdOfUserAnswersListUserAnswers = em.merge(oldRoomIdOfUserAnswersListUserAnswers);
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

    public void edit(Rooms rooms) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Rooms persistentRooms = em.find(Rooms.class, rooms.getId());
            QuestionSets questionSetIdOld = persistentRooms.getQuestionSetId();
            QuestionSets questionSetIdNew = rooms.getQuestionSetId();
            Users ownerIdOld = persistentRooms.getOwnerId();
            Users ownerIdNew = rooms.getOwnerId();
            List<UserInRoom> userInRoomListOld = persistentRooms.getUserInRoomList();
            List<UserInRoom> userInRoomListNew = rooms.getUserInRoomList();
            List<UserAnswers> userAnswersListOld = persistentRooms.getUserAnswersList();
            List<UserAnswers> userAnswersListNew = rooms.getUserAnswersList();
            List<String> illegalOrphanMessages = null;
            for (UserInRoom userInRoomListOldUserInRoom : userInRoomListOld) {
                if (!userInRoomListNew.contains(userInRoomListOldUserInRoom)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain UserInRoom " + userInRoomListOldUserInRoom + " since its roomId field is not nullable.");
                }
            }
            for (UserAnswers userAnswersListOldUserAnswers : userAnswersListOld) {
                if (!userAnswersListNew.contains(userAnswersListOldUserAnswers)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain UserAnswers " + userAnswersListOldUserAnswers + " since its roomId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (questionSetIdNew != null) {
                questionSetIdNew = em.getReference(questionSetIdNew.getClass(), questionSetIdNew.getId());
                rooms.setQuestionSetId(questionSetIdNew);
            }
            if (ownerIdNew != null) {
                ownerIdNew = em.getReference(ownerIdNew.getClass(), ownerIdNew.getId());
                rooms.setOwnerId(ownerIdNew);
            }
            List<UserInRoom> attachedUserInRoomListNew = new ArrayList<UserInRoom>();
            for (UserInRoom userInRoomListNewUserInRoomToAttach : userInRoomListNew) {
                userInRoomListNewUserInRoomToAttach = em.getReference(userInRoomListNewUserInRoomToAttach.getClass(), userInRoomListNewUserInRoomToAttach.getId());
                attachedUserInRoomListNew.add(userInRoomListNewUserInRoomToAttach);
            }
            userInRoomListNew = attachedUserInRoomListNew;
            rooms.setUserInRoomList(userInRoomListNew);
            List<UserAnswers> attachedUserAnswersListNew = new ArrayList<UserAnswers>();
            for (UserAnswers userAnswersListNewUserAnswersToAttach : userAnswersListNew) {
                userAnswersListNewUserAnswersToAttach = em.getReference(userAnswersListNewUserAnswersToAttach.getClass(), userAnswersListNewUserAnswersToAttach.getId());
                attachedUserAnswersListNew.add(userAnswersListNewUserAnswersToAttach);
            }
            userAnswersListNew = attachedUserAnswersListNew;
            rooms.setUserAnswersList(userAnswersListNew);
            rooms = em.merge(rooms);
            if (questionSetIdOld != null && !questionSetIdOld.equals(questionSetIdNew)) {
                questionSetIdOld.getRoomsList().remove(rooms);
                questionSetIdOld = em.merge(questionSetIdOld);
            }
            if (questionSetIdNew != null && !questionSetIdNew.equals(questionSetIdOld)) {
                questionSetIdNew.getRoomsList().add(rooms);
                questionSetIdNew = em.merge(questionSetIdNew);
            }
            if (ownerIdOld != null && !ownerIdOld.equals(ownerIdNew)) {
                ownerIdOld.getRoomsList().remove(rooms);
                ownerIdOld = em.merge(ownerIdOld);
            }
            if (ownerIdNew != null && !ownerIdNew.equals(ownerIdOld)) {
                ownerIdNew.getRoomsList().add(rooms);
                ownerIdNew = em.merge(ownerIdNew);
            }
            for (UserInRoom userInRoomListNewUserInRoom : userInRoomListNew) {
                if (!userInRoomListOld.contains(userInRoomListNewUserInRoom)) {
                    Rooms oldRoomIdOfUserInRoomListNewUserInRoom = userInRoomListNewUserInRoom.getRoomId();
                    userInRoomListNewUserInRoom.setRoomId(rooms);
                    userInRoomListNewUserInRoom = em.merge(userInRoomListNewUserInRoom);
                    if (oldRoomIdOfUserInRoomListNewUserInRoom != null && !oldRoomIdOfUserInRoomListNewUserInRoom.equals(rooms)) {
                        oldRoomIdOfUserInRoomListNewUserInRoom.getUserInRoomList().remove(userInRoomListNewUserInRoom);
                        oldRoomIdOfUserInRoomListNewUserInRoom = em.merge(oldRoomIdOfUserInRoomListNewUserInRoom);
                    }
                }
            }
            for (UserAnswers userAnswersListNewUserAnswers : userAnswersListNew) {
                if (!userAnswersListOld.contains(userAnswersListNewUserAnswers)) {
                    Rooms oldRoomIdOfUserAnswersListNewUserAnswers = userAnswersListNewUserAnswers.getRoomId();
                    userAnswersListNewUserAnswers.setRoomId(rooms);
                    userAnswersListNewUserAnswers = em.merge(userAnswersListNewUserAnswers);
                    if (oldRoomIdOfUserAnswersListNewUserAnswers != null && !oldRoomIdOfUserAnswersListNewUserAnswers.equals(rooms)) {
                        oldRoomIdOfUserAnswersListNewUserAnswers.getUserAnswersList().remove(userAnswersListNewUserAnswers);
                        oldRoomIdOfUserAnswersListNewUserAnswers = em.merge(oldRoomIdOfUserAnswersListNewUserAnswers);
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
                Integer id = rooms.getId();
                if (findRooms(id) == null) {
                    throw new NonexistentEntityException("The rooms with id " + id + " no longer exists.");
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
            Rooms rooms;
            try {
                rooms = em.getReference(Rooms.class, id);
                rooms.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The rooms with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<UserInRoom> userInRoomListOrphanCheck = rooms.getUserInRoomList();
            for (UserInRoom userInRoomListOrphanCheckUserInRoom : userInRoomListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Rooms (" + rooms + ") cannot be destroyed since the UserInRoom " + userInRoomListOrphanCheckUserInRoom + " in its userInRoomList field has a non-nullable roomId field.");
            }
            List<UserAnswers> userAnswersListOrphanCheck = rooms.getUserAnswersList();
            for (UserAnswers userAnswersListOrphanCheckUserAnswers : userAnswersListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Rooms (" + rooms + ") cannot be destroyed since the UserAnswers " + userAnswersListOrphanCheckUserAnswers + " in its userAnswersList field has a non-nullable roomId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            QuestionSets questionSetId = rooms.getQuestionSetId();
            if (questionSetId != null) {
                questionSetId.getRoomsList().remove(rooms);
                questionSetId = em.merge(questionSetId);
            }
            Users ownerId = rooms.getOwnerId();
            if (ownerId != null) {
                ownerId.getRoomsList().remove(rooms);
                ownerId = em.merge(ownerId);
            }
            em.remove(rooms);
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

    public List<Rooms> findRoomsEntities() {
        return findRoomsEntities(true, -1, -1);
    }

    public List<Rooms> findRoomsEntities(int maxResults, int firstResult) {
        return findRoomsEntities(false, maxResults, firstResult);
    }

    private List<Rooms> findRoomsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Rooms.class));
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

    public Rooms findRooms(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Rooms.class, id);
        } finally {
            em.close();
        }
    }

    public int getRoomsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Rooms> rt = cq.from(Rooms.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
