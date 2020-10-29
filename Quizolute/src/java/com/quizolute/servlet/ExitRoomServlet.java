/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quizolute.servlet;

import com.quizolute.jpa.exceptions.RollbackFailureException;
import com.quizolute.jparepository.UserInRoomJpaRepository;
import com.quizolute.model.UserInRoom;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

/**
 *
 * @author ThisPz
 */
public class ExitRoomServlet extends HttpServlet {

    
    @PersistenceUnit(unitName = "QuizolutePU")
    EntityManagerFactory emf;

    @Resource
    UserTransaction utx;
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if(request.getParameter("user_in_room_id") == null || request.getParameter("user_in_room_id").isEmpty()){
            response.sendRedirect("Home?color=red&message=Invalid_user_in_room_id");
        }else{
            int userInRoom_id = Integer.valueOf(request.getParameter("user_in_room_id"));
            UserInRoomJpaRepository uirjc = new UserInRoomJpaRepository(utx, emf);
            UserInRoom userInRoom = uirjc.findUserInRoom(userInRoom_id);
            if(userInRoom == null){
                response.sendRedirect("Home");
            }else{
                try {
                    uirjc.destroy(userInRoom_id);
                } catch (RollbackFailureException ex) {
                    Logger.getLogger(ExitRoomServlet.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(ExitRoomServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                response.sendRedirect("Home");
            }
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
