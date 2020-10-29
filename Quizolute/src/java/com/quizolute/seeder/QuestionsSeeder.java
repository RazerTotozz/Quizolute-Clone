/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quizolute.seeder;

import com.quizolute.jpa.QuestionSetsJpaController;
import com.quizolute.jpa.QuestionsJpaController;
import com.quizolute.jpa.exceptions.NonexistentEntityException;
import com.quizolute.jpa.exceptions.RollbackFailureException;
import com.quizolute.model.QuestionSets;
import com.quizolute.model.Questions;
import com.quizolute.util.StringGenerator;
import java.util.List;
import java.util.Random;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Totozz
 */
public class QuestionsSeeder {

    private final QuestionsJpaController qjc;
    private final QuestionSetsJpaController qsjc;

    private static int id = 0;

    public QuestionsSeeder(UserTransaction utx, EntityManagerFactory emf) {
        this.qjc = new QuestionsJpaController(utx, emf);
        this.qsjc = new QuestionSetsJpaController(utx, emf);
    }

    public void doSeeder() throws Exception {
        Random randomer = new Random();
        int nameLength = 10;
        List<QuestionSets> questionSetList = this.qsjc.findQuestionSetsEntities();
        for (int questionSetIndex = 0; questionSetIndex < questionSetList.size(); questionSetIndex++) {
            for (int i = 0; i < 3; i++) {
                double randomDouble = randomer.nextDouble()*100;
                while(randomDouble < 20.0 || randomDouble > 50.0){
                    randomDouble = randomer.nextDouble()*100;
                }
                randomDouble = (double) Math.round(randomDouble*100)/100;
                Questions fakeQuestion = new Questions(id++, StringGenerator.generateRandomString(nameLength), randomDouble);
                fakeQuestion.setQuestionSetId(questionSetList.get(questionSetIndex));
                System.out.println(fakeQuestion.getMaxScore());
                this.qjc.create(fakeQuestion);
            }
        }
    }
    
    public void refresh() throws NonexistentEntityException, RollbackFailureException, Exception {
        for (Questions question : this.qjc.findQuestionsEntities()) {
            this.qjc.destroy(question.getId());
        }
    }
}
