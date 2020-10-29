/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quizolute.jparepository;

import com.quizolute.jpa.QuestionsJpaController;
import com.quizolute.model.Questions;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

/**
 *
 * @author ThisPz
 */


public class QuestionsJpaRepository extends QuestionsJpaController{
    
    public QuestionsJpaRepository(UserTransaction utx, EntityManagerFactory emf) {
        super(utx, emf);
    }
    
    public int getLastId(){
        EntityManager em = getEntityManager();
        try{
            Query qry = em.createNativeQuery("SELECT * FROM `questions` ORDER BY ID DESC LIMIT 1", Questions.class);
            return ((Questions) qry.getSingleResult()).getId();
        } finally{
            em.close();
        }
    }
}
