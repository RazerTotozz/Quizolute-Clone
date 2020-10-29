/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quizolute.jparepository;

import com.quizolute.jpa.UserAnswersJpaController;
import com.quizolute.jpa.UserInRoomJpaController;
import com.quizolute.model.Rooms;
import com.quizolute.model.UserAnswers;
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
public class UserAnswersJpaRepository extends UserAnswersJpaController {

    public UserAnswersJpaRepository(UserTransaction utx, EntityManagerFactory emf) {
        super(utx, emf);
    }

    public List<UserAnswers> getUserAnswersListByRoomIdQuestionSetId(int room_id, int questionSet_id) {

        List<UserAnswers> userAnswersList = this.findUserAnswersEntities();
        ArrayList<UserAnswers> result = new ArrayList<>();
        if (userAnswersList != null || !userAnswersList.isEmpty()) {
            for (UserAnswers userAnswers : userAnswersList) {
                if (userAnswers.getRoomId().getId() == room_id && userAnswers.getQuestionSetId().getId() == questionSet_id) {
                    result.add(userAnswers);
                }
            }
            return result;
        } else {
            return null;
        }
    }
    
    public List<Integer> getAllUserAnswerIdByRoomId(int room_id){
        List<UserAnswers> userAnswersList = this.findUserAnswersEntities();
        ArrayList<Integer> id = new ArrayList<>();
        if (userAnswersList != null || !userAnswersList.isEmpty()){
            for (UserAnswers userAnswers : userAnswersList) {
                if(userAnswers.getRoomId().getId() == room_id){
                    id.add(userAnswers.getId());
                }
            }
            return id;
        } else{
            return null;
        }
        
    }

}
