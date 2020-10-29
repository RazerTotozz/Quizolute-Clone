/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quizolute.jparepository;

import com.quizolute.jpa.ChoicesJpaController;
import com.quizolute.model.Choices;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

/**
 *
 * @author xLCP
 */

public class ChoicesJpaRepository extends ChoicesJpaController {
    
    public ChoicesJpaRepository(UserTransaction utx, EntityManagerFactory emf) {
        super(utx, emf);
    }
    
    
    public int getLastId(){
        EntityManager em = getEntityManager();
        try{
            Query qry = em.createNativeQuery("SELECT * FROM `choices` ORDER BY ID DESC LIMIT 1", Choices.class);
            return ((Choices) qry.getSingleResult()).getId();
        } finally{
            em.close();
        }
    }
}
