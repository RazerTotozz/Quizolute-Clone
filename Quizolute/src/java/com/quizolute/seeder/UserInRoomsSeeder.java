/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quizolute.seeder;

import com.quizolute.jpa.RoomsJpaController;
import com.quizolute.jpa.UserInRoomJpaController;
import com.quizolute.jpa.UsersJpaController;
import com.quizolute.jpa.exceptions.NonexistentEntityException;
import com.quizolute.jpa.exceptions.RollbackFailureException;
import com.quizolute.model.Rooms;
import com.quizolute.model.Users;
import com.quizolute.model.UserInRoom;
import java.util.List;
import java.util.Random;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Totozz
 */
public class UserInRoomsSeeder {

    private RoomsJpaController rjc;
    private UsersJpaController ujc;
    private UserInRoomJpaController uirjc;

    private static int id = 100;

    public UserInRoomsSeeder(UserTransaction utx, EntityManagerFactory emf) {
        this.rjc = new RoomsJpaController(utx, emf);
        this.ujc = new UsersJpaController(utx, emf);
        this.uirjc = new UserInRoomJpaController(utx, emf);
    }

    public void doSeeder() throws Exception {
        Random randomer = new Random();

        List<Users> userList = this.ujc.findUsersEntities();
        List<Rooms> roomsList = this.rjc.findRoomsEntities();

        for (Users user : userList) {
            int numberOfUserParticipateRoom = randomer.nextInt(roomsList.size()) == 0 ? 1 : randomer.nextInt(roomsList.size());
            for (int i = 0; i < numberOfUserParticipateRoom; i++) {
                UserInRoom uir = new UserInRoom();
                uir.setUserId(user);
                int randomIndex = randomer.nextInt(roomsList.size() - 1);
                Rooms randomRoom = roomsList.get(randomIndex);
                uir.setRoomId(randomRoom);
                this.uirjc.create(uir);
            }
        }
    }
    
    public void refresh() throws NonexistentEntityException, RollbackFailureException, Exception {
        for (UserInRoom userinroom : this.uirjc.findUserInRoomEntities()) {
            this.uirjc.destroy(userinroom.getId());
        }
    }
}
