/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quizolute.jparepository;

import com.quizolute.jpa.QuestionSetsJpaController;
import com.quizolute.model.QuestionSets;
import com.quizolute.model.Users;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.transaction.UserTransaction;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ThisPz
 */


public class QuestionSetsJpaRepository extends QuestionSetsJpaController{
    
    public QuestionSetsJpaRepository(UserTransaction utx, EntityManagerFactory emf) {
        super(utx, emf);
    }
    
    public List<QuestionSets> getAllQuestionSets(Users user){
        if(user == null){
            return null;
        }
        
        EntityManager em = getEntityManager();
        try{
            Query qry = em.createNamedQuery("QuestionSets.findQuestionSetByIdAndPublic");
            String userid = (String.valueOf(user.getId()));
            qry.setParameter("public1", true);
            qry.setParameter("ownerId", user);
            List<QuestionSets> questionSet = qry.getResultList();
            if(!questionSet.isEmpty()){
                return questionSet;
            }
            return null;
        } finally{
            em.close();
        }
    }
}
