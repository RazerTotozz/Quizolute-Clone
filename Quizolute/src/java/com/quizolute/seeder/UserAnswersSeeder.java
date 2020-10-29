/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quizolute.seeder;

import com.quizolute.jpa.ChoicesJpaController;
import com.quizolute.jpa.QuestionSetsJpaController;
import com.quizolute.jpa.QuestionsJpaController;
import com.quizolute.jpa.RoomsJpaController;
import com.quizolute.jpa.UserAnswersJpaController;
import com.quizolute.jpa.UsersJpaController;
import com.quizolute.jpa.exceptions.NonexistentEntityException;
import com.quizolute.jpa.exceptions.RollbackFailureException;
import com.quizolute.model.Answer;
import com.quizolute.model.Choices;
import com.quizolute.model.QuestionSets;
import com.quizolute.model.Questions;
import com.quizolute.model.Rooms;
import com.quizolute.model.Summary;
import com.quizolute.model.UserAnswers;
import com.quizolute.model.Users;
import com.quizolute.util.JavaClassConverter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Totozz
 */
public class UserAnswersSeeder {

    private UsersJpaController ujc;
    private UserAnswersJpaController uajc;
    private QuestionsJpaController qjc;
    private ChoicesJpaController cjc;
    private QuestionSetsJpaController qsjc;
    private RoomsJpaController rjc;

    private static int id = 100;

    public UserAnswersSeeder(UserTransaction utx, EntityManagerFactory emf) {
        this.ujc = new UsersJpaController(utx, emf);
        this.qjc = new QuestionsJpaController(utx, emf);
        this.cjc = new ChoicesJpaController(utx, emf);
        this.qsjc = new QuestionSetsJpaController(utx, emf);
        this.rjc = new RoomsJpaController(utx, emf);
        this.uajc = new UserAnswersJpaController(utx, emf);
    }

    public void doSeeder() throws Exception {
        Random randomer = new Random();

        List<Users> userList = this.ujc.findUsersEntities();
        List<Rooms> roomList = this.rjc.findRoomsEntities();

        for (Users user : userList) {
            for (Rooms room : roomList) {
                QuestionSets questionSet = room.getQuestionSetId();
                ArrayList<Answer> answerList = new ArrayList<>();
                int count = 0;
                List<Questions> questionList = questionSet.getQuestionsList();
                for (Questions question : questionList) {
                    ArrayList<Choices> answerChoiceList = new ArrayList<>();
                    if (count < 2 && count < questionList.size() - 1) {
                        for (Choices choice : question.getChoicesList()) {
                            if (choice.getIsCorrect()) {
                                answerChoiceList.add(choice);
                            }
                        }
                    } else {
                        for (Choices choice : question.getChoicesList()) {
                            if (!choice.getIsCorrect()) {
                                answerChoiceList.add(choice);
                                break;
                            }
                        }
                    }
                    answerList.add(new Answer(question, answerChoiceList));
                    count++;
                }
                Summary fakeSummary = new Summary(questionSet, answerList);
                UserAnswers fakeUserAnswer = new UserAnswers(
                        id,
                        JavaClassConverter.serialize(fakeSummary),
                        new java.sql.Date(new Date().getTime()));
                fakeUserAnswer.setRoomId(room);
                fakeUserAnswer.setUserId(user);
                fakeUserAnswer.setQuestionSetId(questionSet);
                this.uajc.create(fakeUserAnswer);
            }
        }

    }

    public void refresh() throws NonexistentEntityException, RollbackFailureException, Exception {
        for (UserAnswers userAnswer : this.uajc.findUserAnswersEntities()) {
            this.uajc.destroy(userAnswer.getId());
        }
    }

}
