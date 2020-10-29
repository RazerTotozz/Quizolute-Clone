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
import com.quizolute.model.Rooms;
import com.quizolute.model.UserInRoom;
import com.quizolute.model.Users;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author sittiwatlcp
 */
public class UserInRoomJpaController implements Serializable {

    public UserInRoomJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UserInRoom userInRoom) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Rooms roomId = userInRoom.getRoomId();
            if (roomId != null) {
                roomId = em.getReference(roomId.getClass(), roomId.getId());
                userInRoom.setRoomId(roomId);
            }
            Users userId = userInRoom.getUserId();
            if (userId != null) {
                userId = em.getReference(userId.getClass(), userId.getId());
                userInRoom.setUserId(userId);
            }
            em.persist(userInRoom);
            if (roomId != null) {
                roomId.getUserInRoomList().add(userInRoom);
                roomId = em.merge(roomId);
            }
            if (userId != null) {
                userId.getUserInRoomList().add(userInRoom);
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

    public void edit(UserInRoom userInRoom) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            UserInRoom persistentUserInRoom = em.find(UserInRoom.class, userInRoom.getId());
            Rooms roomIdOld = persistentUserInRoom.getRoomId();
            Rooms roomIdNew = userInRoom.getRoomId();
            Users userIdOld = persistentUserInRoom.getUserId();
            Users userIdNew = userInRoom.getUserId();
            if (roomIdNew != null) {
                roomIdNew = em.getReference(roomIdNew.getClass(), roomIdNew.getId());
                userInRoom.setRoomId(roomIdNew);
            }
            if (userIdNew != null) {
                userIdNew = em.getReference(userIdNew.getClass(), userIdNew.getId());
                userInRoom.setUserId(userIdNew);
            }
            userInRoom = em.merge(userInRoom);
            if (roomIdOld != null && !roomIdOld.equals(roomIdNew)) {
                roomIdOld.getUserInRoomList().remove(userInRoom);
                roomIdOld = em.merge(roomIdOld);
            }
            if (roomIdNew != null && !roomIdNew.equals(roomIdOld)) {
                roomIdNew.getUserInRoomList().add(userInRoom);
                roomIdNew = em.merge(roomIdNew);
            }
            if (userIdOld != null && !userIdOld.equals(userIdNew)) {
                userIdOld.getUserInRoomList().remove(userInRoom);
                userIdOld = em.merge(userIdOld);
            }
            if (userIdNew != null && !userIdNew.equals(userIdOld)) {
                userIdNew.getUserInRoomList().add(userInRoom);
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
                Integer id = userInRoom.getId();
                if (findUserInRoom(id) == null) {
                    throw new NonexistentEntityException("The userInRoom with id " + id + " no longer exists.");
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
            UserInRoom userInRoom;
            try {
                userInRoom = em.getReference(UserInRoom.class, id);
                userInRoom.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The userInRoom with id " + id + " no longer exists.", enfe);
            }
            Rooms roomId = userInRoom.getRoomId();
            if (roomId != null) {
                roomId.getUserInRoomList().remove(userInRoom);
                roomId = em.merge(roomId);
            }
            Users userId = userInRoom.getUserId();
            if (userId != null) {
                userId.getUserInRoomList().remove(userInRoom);
                userId = em.merge(userId);
            }
            em.remove(userInRoom);
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

    public List<UserInRoom> findUserInRoomEntities() {
        return findUserInRoomEntities(true, -1, -1);
    }

    public List<UserInRoom> findUserInRoomEntities(int maxResults, int firstResult) {
        return findUserInRoomEntities(false, maxResults, firstResult);
    }

    private List<UserInRoom> findUserInRoomEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UserInRoom.class));
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

    public UserInRoom findUserInRoom(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UserInRoom.class, id);
        } finally {
            em.close();
        }
    }

    public int getUserInRoomCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UserInRoom> rt = cq.from(UserInRoom.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
