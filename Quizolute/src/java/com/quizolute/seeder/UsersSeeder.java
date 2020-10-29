/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quizolute.seeder;

import com.quizolute.jpa.UsersJpaController;
import com.quizolute.jpa.exceptions.NonexistentEntityException;
import com.quizolute.jpa.exceptions.RollbackFailureException;
import com.quizolute.model.Users;
import com.quizolute.util.StringGenerator;
import java.util.Random;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Totozz
 */
public class UsersSeeder {

    private final UsersJpaController ujc;
    private static int id = 100;

    public UsersSeeder(UserTransaction utx,EntityManagerFactory emf) {
        this.ujc = new UsersJpaController(utx, emf);
    }

    public void doSeeder() throws Exception {
        Random randomer = new Random();
        int usernameLength = 10;
        int firstNameLength = 10;
        int lastNameLength = 10;
        String[] type = {"admin","user"};
        for (int i = 0; i < 2; i++) {
            Users fakeUser = new Users(
                    id++, 
                    StringGenerator.generateRandomString(usernameLength) , 
                    StringGenerator.generateRandomString(firstNameLength), 
                    StringGenerator.generateRandomString(lastNameLength), 
                    type[i], 
                    randomer.nextBoolean(), 
                    randomer.nextBoolean());
            this.ujc.create(fakeUser);
        }
    }
    
    public void refresh() throws NonexistentEntityException, RollbackFailureException, Exception {
        for (Users user : this.ujc.findUsersEntities()) {
            this.ujc.destroy(user.getId());
        }
    }
}
