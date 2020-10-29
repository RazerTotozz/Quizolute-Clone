/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quizolute.seeder;

import com.quizolute.jpa.QuestionSetsJpaController;
import com.quizolute.jpa.RoomsJpaController;
import com.quizolute.jpa.UsersJpaController;
import com.quizolute.jpa.exceptions.NonexistentEntityException;
import com.quizolute.jpa.exceptions.RollbackFailureException;
import com.quizolute.model.QuestionSets;
import com.quizolute.model.Rooms;
import com.quizolute.model.Users;
import com.quizolute.util.StringGenerator;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Totozz
 */
public class RoomsSeeder {

    private final RoomsJpaController rjc;
    private final UsersJpaController ujc;
    private final QuestionSetsJpaController qsjc;

    private static int id = 100;

    public RoomsSeeder(UserTransaction utx, EntityManagerFactory emf) {
        this.rjc = new RoomsJpaController(utx, emf);
        this.ujc = new UsersJpaController(utx, emf);
        this.qsjc = new QuestionSetsJpaController(utx, emf);
    }

    public void doSeeder() throws Exception {
        int nameLength = 10;
        int invitationCodeLength = 6;

        List<QuestionSets> questionSetList = this.qsjc.findQuestionSetsEntities();
        List<Users> userList = this.ujc.findUsersEntities();
        
        for (int i = 0; i < 2; i++) {
            Rooms fakeRoom = new Rooms(
                    id++,
                    StringGenerator.generateRandomString(nameLength),
                    StringGenerator.generateRandomString(invitationCodeLength).toLowerCase(),
                    true,
                    new java.sql.Date(new Date().getTime()));

            fakeRoom.setOwnerId(userList.get(i));

            fakeRoom.setQuestionSetId(questionSetList.get(i));

            this.rjc.create(fakeRoom);
        }
    }
    
    public void refresh() throws NonexistentEntityException, RollbackFailureException, Exception{
        for (Rooms room : this.rjc.findRoomsEntities()) {
            this.rjc.destroy(room.getId());
        }
    }
}
