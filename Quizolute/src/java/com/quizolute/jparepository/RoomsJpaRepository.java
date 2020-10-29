/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quizolute.jparepository;

import com.quizolute.jpa.RoomsJpaController;
import com.quizolute.model.Rooms;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

/**
 *
 * @author ThisPz
 */
public class RoomsJpaRepository extends RoomsJpaController{
    
    public RoomsJpaRepository(UserTransaction utx, EntityManagerFactory emf) {
        super(utx, emf);
    }
    
    public Rooms getRoomByInvitationCode(String invitationCode){
        EntityManager em = getEntityManager();
        try{
            Query qry = em.createNamedQuery("Rooms.findByInvitationCode");
            qry.setParameter("invitationCode", invitationCode);
            qry.setMaxResults(1);
            List<Rooms> room = qry.getResultList();
            if(!room.isEmpty()){
                return ((Rooms) room.get(0));
            }
            return null;
        } finally{
            em.close();
        }
    }
    
}
