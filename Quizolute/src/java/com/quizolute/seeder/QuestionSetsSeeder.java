/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quizolute.seeder;

import com.quizolute.jpa.QuestionSetsJpaController;
import com.quizolute.jpa.exceptions.NonexistentEntityException;
import com.quizolute.jpa.exceptions.RollbackFailureException;
import com.quizolute.model.QuestionSets;
import com.quizolute.util.StringGenerator;
import java.util.Date;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Totozz
 */
public class QuestionSetsSeeder {

    private final QuestionSetsJpaController qsjc;

    private static int id = 100;

    public QuestionSetsSeeder(UserTransaction utx, EntityManagerFactory emf) {
        this.qsjc = new QuestionSetsJpaController(utx, emf);
    }

    public void doSeeder() throws Exception {
        int nameLength = 10;
        boolean[] isPublic = {true, false};
        for (int i = 0; i < 2; i++) {
            QuestionSets fakeQuestionSet = new QuestionSets(id++, StringGenerator.generateRandomString(nameLength), isPublic[i], new java.sql.Date(new Date().getTime()), new java.sql.Date(new Date().getTime()));
            this.qsjc.create(fakeQuestionSet);
        }
    }
    
    public void refresh() throws NonexistentEntityException, RollbackFailureException, Exception {
        for (QuestionSets questionSet : this.qsjc.findQuestionSetsEntities()) {
            this.qsjc.destroy(questionSet.getId());
        }
    }
}
