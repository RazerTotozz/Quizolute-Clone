/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quizolute.jparepository;

import com.quizolute.jpa.UsersJpaController;
import com.quizolute.jpa.exceptions.NonexistentEntityException;
import com.quizolute.jpa.exceptions.RollbackFailureException;
import com.quizolute.model.Users;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

/**
 *
 * @author sittiwatlcp
 */
public class UsersJpaRepository extends UsersJpaController {

    public UsersJpaRepository(UserTransaction utx, EntityManagerFactory emf) {
        super(utx, emf);
    }

    public Users getUserByUsername(String username) {
        EntityManager em = getEntityManager();
        try {
            Query qry = em.createNamedQuery("Users.findByUsername");
            qry.setParameter("username", username);
            List<Users> user = qry.getResultList();
            System.out.println(user.toString());
            if (!user.isEmpty()) {
                return ((Users) user.get(0));
            }
            return null;
        } finally {
            em.close();
        }
    }

    public boolean insertUser(Users user) {
        try {
            this.create(user);
        } catch (Exception ex) {
            Logger.getLogger(UsersJpaRepository.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    
    public boolean onlineUser(Users user){
        user.setActive(true);
        try {
            this.edit(user);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(UsersJpaRepository.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(UsersJpaRepository.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(UsersJpaRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    
    public boolean offlineUser(Users user){
        user.setActive(false);
        try {
            this.edit(user);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(UsersJpaRepository.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(UsersJpaRepository.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(UsersJpaRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

}
