/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quizolute.seeder;

import com.quizolute.jpa.ChoicesJpaController;
import com.quizolute.jpa.QuestionsJpaController;
import com.quizolute.jpa.exceptions.RollbackFailureException;
import com.quizolute.model.Choices;
import com.quizolute.model.Questions;
import com.quizolute.util.StringGenerator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Totozz
 */
public class ChoicesSeeder {

    private final QuestionsJpaController qjc;
    private final ChoicesJpaController cjc;

    private static int id = 100;

    public ChoicesSeeder(UserTransaction utx, EntityManagerFactory emf) {
        this.qjc = new QuestionsJpaController(utx, emf);
        this.cjc = new ChoicesJpaController(utx, emf);
    }

    public void doSeeder() throws Exception {
        Random randomer = new Random();
        int nameLength = 10;
        //1 question have 4 choice with 1,2 correct ans
        List<Questions> questionList = this.qjc.findQuestionsEntities();
        for (Questions question : questionList) {
            ArrayList<Boolean> isCorrect = new ArrayList<>(3);
            isCorrect.add(Boolean.TRUE);
            isCorrect.add(Boolean.FALSE);
            isCorrect.add(Boolean.FALSE);
            isCorrect.add(Boolean.FALSE);
            double randomDouble = randomer.nextDouble() * 100;
            while (randomDouble < 10.0 || randomDouble > 30.0) {
                randomDouble = randomer.nextDouble() * 100;
            }
            randomDouble = (double) Math.round(randomDouble * 100) / 100;

//            isCorrect.add(randomer.nextBoolean());
            Collections.shuffle(isCorrect);

            for (int i = 0; i < 4; i++) {
                Choices fakeChoice = new Choices(id, StringGenerator.generateRandomString(nameLength), randomDouble, isCorrect.get(i));
                fakeChoice.setQuestionId(question);
                this.cjc.create(fakeChoice);
            }
        }
    }
    public void refresh() throws RollbackFailureException, Exception{
        for (Choices choice : this.cjc.findChoicesEntities()) {
            this.cjc.destroy(choice.getId());
        }
    }
}
