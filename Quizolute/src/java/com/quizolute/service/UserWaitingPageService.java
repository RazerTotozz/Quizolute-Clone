/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quizolute.service;

import com.google.gson.JsonObject;
import com.quizolute.jparepository.RoomsJpaRepository;
import com.quizolute.model.Rooms;
import java.io.IOException;
import java.io.PrintWriter;
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
 * @author Administrator
 */
public class UserWaitingPageService extends HttpServlet {

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
        RoomsJpaRepository rjr = new RoomsJpaRepository(utx, emf);
        int room_id = Integer.valueOf(request.getParameter("room_id"));
        
        emf.getCache().evictAll();
        Rooms room = rjr.findRooms(room_id);
        
        PrintWriter responseWriter = response.getWriter();
        JsonObject responseData = new JsonObject();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        
        if(!room.getIsActive()){
            responseData.addProperty("questionset_id", room.getQuestionSetId().getId());
            responseWriter.print(responseData.toString());
            response.setStatus(200);
        }else{
            response.setStatus(500);
        }
        responseWriter.flush();
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
