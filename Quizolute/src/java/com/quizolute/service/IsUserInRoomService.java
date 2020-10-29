/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quizolute.service;

import com.google.gson.JsonObject;
import com.quizolute.jparepository.UserInRoomJpaRepository;
import com.quizolute.model.UserInRoom;
import com.quizolute.model.Users;
import java.io.IOException;
import java.io.PrintWriter;
import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

/**
 *
 * @author Administrator
 */
public class IsUserInRoomService extends HttpServlet {

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
        PrintWriter responseWriter = response.getWriter();
        JsonObject responseData = new JsonObject();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        
        if (request.getParameter("user_in_room_id") == null || request.getParameter("user_in_room_id").isEmpty()) {
            response.setStatus(500);
        } else {
            int userInRoom_id = Integer.valueOf(request.getParameter("user_in_room_id"));
            UserInRoomJpaRepository uirjr = new UserInRoomJpaRepository(utx, emf);
            UserInRoom userInRoom = uirjr.findUserInRoom(userInRoom_id);
            if(userInRoom == null){
                responseData.addProperty("is_in_room", Boolean.FALSE);
            }else{
                responseData.addProperty("is_in_room", Boolean.TRUE);
            }
            response.setStatus(200);
            responseWriter.print(responseData.toString());
            responseWriter.flush();
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
