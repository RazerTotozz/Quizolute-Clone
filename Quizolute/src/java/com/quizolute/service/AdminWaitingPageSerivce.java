/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quizolute.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.quizolute.jpa.UserAnswersJpaController;
import com.quizolute.jpa.UserInRoomJpaController;
import com.quizolute.jpa.exceptions.RollbackFailureException;
import com.quizolute.jparepository.RoomsJpaRepository;
import com.quizolute.jparepository.UserAnswersJpaRepository;
import com.quizolute.model.Rooms;
import com.quizolute.model.UserAnswers;
import com.quizolute.model.UserInRoom;
import com.quizolute.model.Users;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * @author Totozz
 */
public class AdminWaitingPageSerivce extends HttpServlet {

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
        emf.getCache().evictAll();

        JsonObject responseData = new JsonObject();
        PrintWriter responseWriter = response.getWriter();
        if (request.getParameter("room_id") == null || request.getParameter("room_id").isEmpty()
                || request.getParameter("questionset_id") == null || request.getParameter("questionset_id").isEmpty()) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            responseData.addProperty("error", "invalid parameter");
            responseWriter.println(responseData.toString());
        } else {
            int room_id = Integer.valueOf(request.getParameter("room_id"));
            int questionSet_id = Integer.valueOf(request.getParameter("questionset_id"));
            List<UserInRoom> result = getUsersInRoom(room_id);

            if (result != null) {

                responseData.addProperty("student_count", result.size());

                UserAnswersJpaRepository uajr = new UserAnswersJpaRepository(utx, emf);
                List<UserAnswers> userAnswerList = uajr.getUserAnswersListByRoomIdQuestionSetId(room_id, questionSet_id);
                JsonArray users = new JsonArray();
                if (userAnswerList == null || userAnswerList.isEmpty()) {

                    result.forEach((t) -> {
                        JsonObject jo = new JsonObject();
                        jo.addProperty("user_in_room_id", t.getId());
                        jo.addProperty("name", t.getUserId().getFullName());
                        jo.addProperty("is_done", Boolean.FALSE);
                        users.add(jo);
                    });

                } else {

                    ArrayList<Users> userThatAnsweredList = new ArrayList<>();
                    userAnswerList.forEach((t) -> {
                        userThatAnsweredList.add(t.getUserId());
                    });

                    result.forEach((t) -> {
                        JsonObject jo = new JsonObject();
                        jo.addProperty("user_in_room_id", t.getId());
                        jo.addProperty("name", t.getUserId().getFullName());
                        if (userThatAnsweredList.contains(t.getUserId())) {
                            jo.addProperty("is_done", Boolean.TRUE);
                        } else {
                            jo.addProperty("is_done", Boolean.FALSE);
                        }
                        users.add(jo);
                    });

                }
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json");
                responseData.add("students", users);
                responseWriter.println(responseData.toString());
            } else {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json");
                responseData.addProperty("student_count", 0);
                responseData.add("students", null);
                responseWriter.println(responseData.toString());
            }

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
        int userInRoomId = Integer.valueOf(request.getParameter("user_in_room_id"));

        UserInRoomJpaController uirjc = new UserInRoomJpaController(utx, emf);
        UserInRoom userInRoom = uirjc.findUserInRoom(userInRoomId);
        PrintWriter responseWriter = response.getWriter();
        JsonObject responseData = new JsonObject();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        Rooms room = userInRoom.getRoomId();
        if (room.getIsActive()) {

            if (userInRoom != null) {
                JsonObject deletedUser = new JsonObject();

                deletedUser.addProperty("user_in_room_id", userInRoomId);
                deletedUser.addProperty("name", userInRoom.getUserId().getFullName());

                responseData.add("deleted_user", deletedUser);

                try {
                    uirjc.destroy(userInRoomId);
                } catch (RollbackFailureException ex) {
                    Logger.getLogger(AdminWaitingPageSerivce.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(AdminWaitingPageSerivce.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else {
                responseData.add("deleted_user", null);
            }
            responseWriter.println(responseData.toString());
        } else {
            response.setStatus(500);
        }
        responseWriter.flush();
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

    private List<UserInRoom> getUsersInRoom(int room_id) {
        emf.getCache().evictAll();
        RoomsJpaRepository rjr = new RoomsJpaRepository(utx, emf);
        Rooms room = rjr.findRooms(room_id);
        if (room != null) {
            List<UserInRoom> userInRoomList = room.getUserInRoomList();
            if (userInRoomList.isEmpty()) {
                return null;
            } else {
                return userInRoomList;
            }
        } else {
            return null;
        }
    }
}
