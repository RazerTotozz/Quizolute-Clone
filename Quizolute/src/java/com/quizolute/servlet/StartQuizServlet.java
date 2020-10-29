/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quizolute.servlet;

import com.google.gson.JsonObject;
import com.quizolute.jpa.UserInRoomJpaController;
import com.quizolute.jpa.exceptions.NonexistentEntityException;
import com.quizolute.jpa.exceptions.RollbackFailureException;
import com.quizolute.jparepository.RoomsJpaRepository;
import com.quizolute.model.Rooms;
import com.quizolute.model.UserInRoom;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
 * @author Administrator
 */
public class StartQuizServlet extends HttpServlet {

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
        if (request.getParameter("room_id") != null || !request.getParameter("room_id").isEmpty()) {
            int room_id = Integer.valueOf(request.getParameter("room_id"));

            UserInRoomJpaController uirjc = new UserInRoomJpaController(utx, emf);
            RoomsJpaRepository rjr = new RoomsJpaRepository(utx, emf);
            Rooms room = rjr.findRooms(room_id);
            room.setIsActive(true);
            ArrayList<UserInRoom> emptyList = new ArrayList<>();
            room.getUserInRoomList().forEach((userInRoom) -> {
                try {
                    uirjc.destroy(userInRoom.getId());
                } catch (RollbackFailureException ex) {
                    Logger.getLogger(StartQuizServlet.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(StartQuizServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            room.setUserInRoomList(emptyList);
            try {
                rjr.edit(room);
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(StartQuizServlet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RollbackFailureException ex) {
                Logger.getLogger(StartQuizServlet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(StartQuizServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            emf.getCache().evictAll();
            room = rjr.findRooms(room.getId());
            request.setAttribute("room", room);
            request.setAttribute("questionset", room.getQuestionSetId());
            request.getRequestDispatcher("/pages/admin/startquiz.jsp").forward(request, response);

        } else {
            response.sendRedirect("Quizolute/AdminPanel?title=manageroom");
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

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter responseWriter = response.getWriter();
        JsonObject responseData = new JsonObject();

        if (request.getParameter("room_id") == null || request.getParameter("status") == null
                || request.getParameter("room_id").isEmpty() || request.getParameter("status").isEmpty()) {

            response.setStatus(500);
            responseData.addProperty("error", "Invalid request data.");
            responseWriter.print(responseData.toString());
            responseWriter.flush();

        } else {
            if (request.getParameter("status").equalsIgnoreCase("true")
                    || request.getParameter("status").equalsIgnoreCase("false")) {
                boolean room_status = Boolean.valueOf(request.getParameter("status"));
                int room_id = Integer.valueOf(request.getParameter("room_id"));
                RoomsJpaRepository rjr = new RoomsJpaRepository(utx, emf);
                Rooms room = rjr.findRooms(room_id);
                if (room == null) {
                    response.setStatus(500);
                    responseData.addProperty("error", "Room not found");
                    responseWriter.print(responseData.toString());
                    responseWriter.flush();
                } else {
                    room.setIsActive(room_status);
                    try {
                        rjr.edit(room);
                    } catch (NonexistentEntityException ex) {
                        Logger.getLogger(StartQuizServlet.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (RollbackFailureException ex) {
                        Logger.getLogger(StartQuizServlet.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (Exception ex) {
                        Logger.getLogger(StartQuizServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    response.setStatus(200);
                    responseData.addProperty("is_active", room.getIsActive());
                    responseWriter.print(responseData.toString());
                    responseWriter.flush();
                }
            } else {
                response.setStatus(500);
                responseData.addProperty("error", "status must be true or false only.");
                responseWriter.print(responseData.toString());
                responseWriter.flush();
            }
        }
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
