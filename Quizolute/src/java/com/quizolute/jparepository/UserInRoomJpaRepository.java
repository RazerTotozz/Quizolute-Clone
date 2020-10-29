/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quizolute.jparepository;

import com.quizolute.jpa.UserInRoomJpaController;
import com.quizolute.model.Rooms;
import com.quizolute.model.UserInRoom;
import com.quizolute.model.Users;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

/**
 *
 * @author Administrator
 */
public class UserInRoomJpaRepository extends UserInRoomJpaController{
    
    public UserInRoomJpaRepository(UserTransaction utx, EntityManagerFactory emf) {
        super(utx, emf);
    }
    
    public UserInRoom getUserInRoomByRoomAndUser(int room_id,int user_id) {
        
        List<UserInRoom> userInRoomList = findUserInRoomEntities();
        for (UserInRoom userInRoom : userInRoomList) {
            if(userInRoom.getRoomId().getId() == room_id && userInRoom.getUserId().getId() == user_id){
                return userInRoom;
            }
        }
        return null;
    }
    
    public List<Integer> getAllIdByRoomId(int room_id){
        ArrayList<Integer> id = new ArrayList<>();
        
        List<UserInRoom> userInRoomList = findUserInRoomEntities();
        if (userInRoomList != null || !userInRoomList.isEmpty()){
            for (UserInRoom userInRoom : userInRoomList) {
                if(userInRoom.getRoomId().getId() == room_id){
                    id.add(userInRoom.getId());
                }
            }
            return id;
        } else{
            return null;
        }
    }
    
    
}
