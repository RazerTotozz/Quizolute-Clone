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
import com.quizolute.model.Rooms;
import java.util.ArrayList;
import java.util.List;
import com.quizolute.model.UserInRoom;
import com.quizolute.model.UserAnswers;
import com.quizolute.model.QuestionSets;
import com.quizolute.model.Users;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author ThisPz
 */
public class UsersJpaController implements Serializable {

    public UsersJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Users users) throws RollbackFailureException, Exception {
        if (users.getRoomsList() == null) {
            users.setRoomsList(new ArrayList<Rooms>());
        }
        if (users.getUserInRoomList() == null) {
            users.setUserInRoomList(new ArrayList<UserInRoom>());
        }
        if (users.getUserAnswersList() == null) {
            users.setUserAnswersList(new ArrayList<UserAnswers>());
        }
        if (users.getQuestionSetsList() == null) {
            users.setQuestionSetsList(new ArrayList<QuestionSets>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Rooms> attachedRoomsList = new ArrayList<Rooms>();
            for (Rooms roomsListRoomsToAttach : users.getRoomsList()) {
                roomsListRoomsToAttach = em.getReference(roomsListRoomsToAttach.getClass(), roomsListRoomsToAttach.getId());
                attachedRoomsList.add(roomsListRoomsToAttach);
            }
            users.setRoomsList(attachedRoomsList);
            List<UserInRoom> attachedUserInRoomList = new ArrayList<UserInRoom>();
            for (UserInRoom userInRoomListUserInRoomToAttach : users.getUserInRoomList()) {
                userInRoomListUserInRoomToAttach = em.getReference(userInRoomListUserInRoomToAttach.getClass(), userInRoomListUserInRoomToAttach.getId());
                attachedUserInRoomList.add(userInRoomListUserInRoomToAttach);
            }
            users.setUserInRoomList(attachedUserInRoomList);
            List<UserAnswers> attachedUserAnswersList = new ArrayList<UserAnswers>();
            for (UserAnswers userAnswersListUserAnswersToAttach : users.getUserAnswersList()) {
                userAnswersListUserAnswersToAttach = em.getReference(userAnswersListUserAnswersToAttach.getClass(), userAnswersListUserAnswersToAttach.getId());
                attachedUserAnswersList.add(userAnswersListUserAnswersToAttach);
            }
            users.setUserAnswersList(attachedUserAnswersList);
            List<QuestionSets> attachedQuestionSetsList = new ArrayList<QuestionSets>();
            for (QuestionSets questionSetsListQuestionSetsToAttach : users.getQuestionSetsList()) {
                questionSetsListQuestionSetsToAttach = em.getReference(questionSetsListQuestionSetsToAttach.getClass(), questionSetsListQuestionSetsToAttach.getId());
                attachedQuestionSetsList.add(questionSetsListQuestionSetsToAttach);
            }
            users.setQuestionSetsList(attachedQuestionSetsList);
            em.persist(users);
            for (Rooms roomsListRooms : users.getRoomsList()) {
                Users oldOwnerIdOfRoomsListRooms = roomsListRooms.getOwnerId();
                roomsListRooms.setOwnerId(users);
                roomsListRooms = em.merge(roomsListRooms);
                if (oldOwnerIdOfRoomsListRooms != null) {
                    oldOwnerIdOfRoomsListRooms.getRoomsList().remove(roomsListRooms);
                    oldOwnerIdOfRoomsListRooms = em.merge(oldOwnerIdOfRoomsListRooms);
                }
            }
            for (UserInRoom userInRoomListUserInRoom : users.getUserInRoomList()) {
                Users oldUserIdOfUserInRoomListUserInRoom = userInRoomListUserInRoom.getUserId();
                userInRoomListUserInRoom.setUserId(users);
                userInRoomListUserInRoom = em.merge(userInRoomListUserInRoom);
                if (oldUserIdOfUserInRoomListUserInRoom != null) {
                    oldUserIdOfUserInRoomListUserInRoom.getUserInRoomList().remove(userInRoomListUserInRoom);
                    oldUserIdOfUserInRoomListUserInRoom = em.merge(oldUserIdOfUserInRoomListUserInRoom);
                }
            }
            for (UserAnswers userAnswersListUserAnswers : users.getUserAnswersList()) {
                Users oldUserIdOfUserAnswersListUserAnswers = userAnswersListUserAnswers.getUserId();
                userAnswersListUserAnswers.setUserId(users);
                userAnswersListUserAnswers = em.merge(userAnswersListUserAnswers);
                if (oldUserIdOfUserAnswersListUserAnswers != null) {
                    oldUserIdOfUserAnswersListUserAnswers.getUserAnswersList().remove(userAnswersListUserAnswers);
                    oldUserIdOfUserAnswersListUserAnswers = em.merge(oldUserIdOfUserAnswersListUserAnswers);
                }
            }
            for (QuestionSets questionSetsListQuestionSets : users.getQuestionSetsList()) {
                Users oldOwnerIdOfQuestionSetsListQuestionSets = questionSetsListQuestionSets.getOwnerId();
                questionSetsListQuestionSets.setOwnerId(users);
                questionSetsListQuestionSets = em.merge(questionSetsListQuestionSets);
                if (oldOwnerIdOfQuestionSetsListQuestionSets != null) {
                    oldOwnerIdOfQuestionSetsListQuestionSets.getQuestionSetsList().remove(questionSetsListQuestionSets);
                    oldOwnerIdOfQuestionSetsListQuestionSets = em.merge(oldOwnerIdOfQuestionSetsListQuestionSets);
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

    public void edit(Users users) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Users persistentUsers = em.find(Users.class, users.getId());
            List<Rooms> roomsListOld = persistentUsers.getRoomsList();
            List<Rooms> roomsListNew = users.getRoomsList();
            List<UserInRoom> userInRoomListOld = persistentUsers.getUserInRoomList();
            List<UserInRoom> userInRoomListNew = users.getUserInRoomList();
            List<UserAnswers> userAnswersListOld = persistentUsers.getUserAnswersList();
            List<UserAnswers> userAnswersListNew = users.getUserAnswersList();
            List<QuestionSets> questionSetsListOld = persistentUsers.getQuestionSetsList();
            List<QuestionSets> questionSetsListNew = users.getQuestionSetsList();
            List<String> illegalOrphanMessages = null;
            for (Rooms roomsListOldRooms : roomsListOld) {
                if (!roomsListNew.contains(roomsListOldRooms)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Rooms " + roomsListOldRooms + " since its ownerId field is not nullable.");
                }
            }
            for (UserInRoom userInRoomListOldUserInRoom : userInRoomListOld) {
                if (!userInRoomListNew.contains(userInRoomListOldUserInRoom)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain UserInRoom " + userInRoomListOldUserInRoom + " since its userId field is not nullable.");
                }
            }
            for (UserAnswers userAnswersListOldUserAnswers : userAnswersListOld) {
                if (!userAnswersListNew.contains(userAnswersListOldUserAnswers)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain UserAnswers " + userAnswersListOldUserAnswers + " since its userId field is not nullable.");
                }
            }
            for (QuestionSets questionSetsListOldQuestionSets : questionSetsListOld) {
                if (!questionSetsListNew.contains(questionSetsListOldQuestionSets)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain QuestionSets " + questionSetsListOldQuestionSets + " since its ownerId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Rooms> attachedRoomsListNew = new ArrayList<Rooms>();
            for (Rooms roomsListNewRoomsToAttach : roomsListNew) {
                roomsListNewRoomsToAttach = em.getReference(roomsListNewRoomsToAttach.getClass(), roomsListNewRoomsToAttach.getId());
                attachedRoomsListNew.add(roomsListNewRoomsToAttach);
            }
            roomsListNew = attachedRoomsListNew;
            users.setRoomsList(roomsListNew);
            List<UserInRoom> attachedUserInRoomListNew = new ArrayList<UserInRoom>();
            for (UserInRoom userInRoomListNewUserInRoomToAttach : userInRoomListNew) {
                userInRoomListNewUserInRoomToAttach = em.getReference(userInRoomListNewUserInRoomToAttach.getClass(), userInRoomListNewUserInRoomToAttach.getId());
                attachedUserInRoomListNew.add(userInRoomListNewUserInRoomToAttach);
            }
            userInRoomListNew = attachedUserInRoomListNew;
            users.setUserInRoomList(userInRoomListNew);
            List<UserAnswers> attachedUserAnswersListNew = new ArrayList<UserAnswers>();
            for (UserAnswers userAnswersListNewUserAnswersToAttach : userAnswersListNew) {
                userAnswersListNewUserAnswersToAttach = em.getReference(userAnswersListNewUserAnswersToAttach.getClass(), userAnswersListNewUserAnswersToAttach.getId());
                attachedUserAnswersListNew.add(userAnswersListNewUserAnswersToAttach);
            }
            userAnswersListNew = attachedUserAnswersListNew;
            users.setUserAnswersList(userAnswersListNew);
            List<QuestionSets> attachedQuestionSetsListNew = new ArrayList<QuestionSets>();
            for (QuestionSets questionSetsListNewQuestionSetsToAttach : questionSetsListNew) {
                questionSetsListNewQuestionSetsToAttach = em.getReference(questionSetsListNewQuestionSetsToAttach.getClass(), questionSetsListNewQuestionSetsToAttach.getId());
                attachedQuestionSetsListNew.add(questionSetsListNewQuestionSetsToAttach);
            }
            questionSetsListNew = attachedQuestionSetsListNew;
            users.setQuestionSetsList(questionSetsListNew);
            users = em.merge(users);
            for (Rooms roomsListNewRooms : roomsListNew) {
                if (!roomsListOld.contains(roomsListNewRooms)) {
                    Users oldOwnerIdOfRoomsListNewRooms = roomsListNewRooms.getOwnerId();
                    roomsListNewRooms.setOwnerId(users);
                    roomsListNewRooms = em.merge(roomsListNewRooms);
                    if (oldOwnerIdOfRoomsListNewRooms != null && !oldOwnerIdOfRoomsListNewRooms.equals(users)) {
                        oldOwnerIdOfRoomsListNewRooms.getRoomsList().remove(roomsListNewRooms);
                        oldOwnerIdOfRoomsListNewRooms = em.merge(oldOwnerIdOfRoomsListNewRooms);
                    }
                }
            }
            for (UserInRoom userInRoomListNewUserInRoom : userInRoomListNew) {
                if (!userInRoomListOld.contains(userInRoomListNewUserInRoom)) {
                    Users oldUserIdOfUserInRoomListNewUserInRoom = userInRoomListNewUserInRoom.getUserId();
                    userInRoomListNewUserInRoom.setUserId(users);
                    userInRoomListNewUserInRoom = em.merge(userInRoomListNewUserInRoom);
                    if (oldUserIdOfUserInRoomListNewUserInRoom != null && !oldUserIdOfUserInRoomListNewUserInRoom.equals(users)) {
                        oldUserIdOfUserInRoomListNewUserInRoom.getUserInRoomList().remove(userInRoomListNewUserInRoom);
                        oldUserIdOfUserInRoomListNewUserInRoom = em.merge(oldUserIdOfUserInRoomListNewUserInRoom);
                    }
                }
            }
            for (UserAnswers userAnswersListNewUserAnswers : userAnswersListNew) {
                if (!userAnswersListOld.contains(userAnswersListNewUserAnswers)) {
                    Users oldUserIdOfUserAnswersListNewUserAnswers = userAnswersListNewUserAnswers.getUserId();
                    userAnswersListNewUserAnswers.setUserId(users);
                    userAnswersListNewUserAnswers = em.merge(userAnswersListNewUserAnswers);
                    if (oldUserIdOfUserAnswersListNewUserAnswers != null && !oldUserIdOfUserAnswersListNewUserAnswers.equals(users)) {
                        oldUserIdOfUserAnswersListNewUserAnswers.getUserAnswersList().remove(userAnswersListNewUserAnswers);
                        oldUserIdOfUserAnswersListNewUserAnswers = em.merge(oldUserIdOfUserAnswersListNewUserAnswers);
                    }
                }
            }
            for (QuestionSets questionSetsListNewQuestionSets : questionSetsListNew) {
                if (!questionSetsListOld.contains(questionSetsListNewQuestionSets)) {
                    Users oldOwnerIdOfQuestionSetsListNewQuestionSets = questionSetsListNewQuestionSets.getOwnerId();
                    questionSetsListNewQuestionSets.setOwnerId(users);
                    questionSetsListNewQuestionSets = em.merge(questionSetsListNewQuestionSets);
                    if (oldOwnerIdOfQuestionSetsListNewQuestionSets != null && !oldOwnerIdOfQuestionSetsListNewQuestionSets.equals(users)) {
                        oldOwnerIdOfQuestionSetsListNewQuestionSets.getQuestionSetsList().remove(questionSetsListNewQuestionSets);
                        oldOwnerIdOfQuestionSetsListNewQuestionSets = em.merge(oldOwnerIdOfQuestionSetsListNewQuestionSets);
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
                Integer id = users.getId();
                if (findUsers(id) == null) {
                    throw new NonexistentEntityException("The users with id " + id + " no longer exists.");
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
            Users users;
            try {
                users = em.getReference(Users.class, id);
                users.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The users with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Rooms> roomsListOrphanCheck = users.getRoomsList();
            for (Rooms roomsListOrphanCheckRooms : roomsListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Users (" + users + ") cannot be destroyed since the Rooms " + roomsListOrphanCheckRooms + " in its roomsList field has a non-nullable ownerId field.");
            }
            List<UserInRoom> userInRoomListOrphanCheck = users.getUserInRoomList();
            for (UserInRoom userInRoomListOrphanCheckUserInRoom : userInRoomListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Users (" + users + ") cannot be destroyed since the UserInRoom " + userInRoomListOrphanCheckUserInRoom + " in its userInRoomList field has a non-nullable userId field.");
            }
            List<UserAnswers> userAnswersListOrphanCheck = users.getUserAnswersList();
            for (UserAnswers userAnswersListOrphanCheckUserAnswers : userAnswersListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Users (" + users + ") cannot be destroyed since the UserAnswers " + userAnswersListOrphanCheckUserAnswers + " in its userAnswersList field has a non-nullable userId field.");
            }
            List<QuestionSets> questionSetsListOrphanCheck = users.getQuestionSetsList();
            for (QuestionSets questionSetsListOrphanCheckQuestionSets : questionSetsListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Users (" + users + ") cannot be destroyed since the QuestionSets " + questionSetsListOrphanCheckQuestionSets + " in its questionSetsList field has a non-nullable ownerId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(users);
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

    public List<Users> findUsersEntities() {
        return findUsersEntities(true, -1, -1);
    }

    public List<Users> findUsersEntities(int maxResults, int firstResult) {
        return findUsersEntities(false, maxResults, firstResult);
    }

    private List<Users> findUsersEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Users.class));
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

    public Users findUsers(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Users.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsersCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Users> rt = cq.from(Users.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
