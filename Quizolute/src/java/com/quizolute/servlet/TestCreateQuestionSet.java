/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quizolute.servlet;

import com.quizolute.jpa.QuestionSetsJpaController;
import com.quizolute.jparepository.QuestionSetsJpaRepository;
import com.quizolute.model.QuestionSets;
import com.quizolute.model.Users;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.transaction.UserTransaction;

/**
 *
 * @author ThisPz
 */

public class TestCreateQuestionSet {
//    @PersistenceUnit(unitName = "QuizolutePU")
//    static EntityManagerFactory emf;
//    
//    @Resource
//    static UserTransaction utx;
    
    public static void main(String[] args) throws Exception {
        
//        QuestionSetsJpaRepository qsj = new QuestionSetsJpaRepository(utx, emf);
//        QuestionSetsJpaController qjc = new QuestionSetsJpaController(utx, emf);
//        
//        
////        Users user = new Users(23);
//        List<QuestionSets> qs;
////        qs = qsj.getAllQuestionSets(user);
//        qs = qjc.findQuestionSetsEntities();
//        System.out.println(qs);

//        QuestionSetsJpaController qsjc = new QuestionSetsJpaController(utx, emf);
//            QuestionSets newQuestion = new QuestionSets(
//                    null, 
//                    "Test Title", 
//                    true, 
//                    new java.sql.Date(new Date().getTime()), 
//                    new java.sql.Date(new Date().getTime()));
//            qsjc.create(newQuestion);
//        qsjc.
    }
}
