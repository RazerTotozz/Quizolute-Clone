/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quizolute.servlet.AdminPanel;

import com.quizolute.jpa.exceptions.NonexistentEntityException;
import com.quizolute.jpa.exceptions.RollbackFailureException;
import com.quizolute.jparepository.QuestionSetsJpaRepository;
import com.quizolute.jparepository.RoomsJpaRepository;
import com.quizolute.jparepository.UserAnswersJpaRepository;
import com.quizolute.jparepository.UserInRoomJpaRepository;
import com.quizolute.jparepository.UsersJpaRepository;
import com.quizolute.model.QuestionSets;
import com.quizolute.model.Rooms;
import com.quizolute.model.UserInRoom;
import com.quizolute.model.Users;
import java.io.IOException;
import java.io.PrintWriter;
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
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

/**
 *
 * @author ThisPz
 */
public class EditRoomServlet extends HttpServlet {
    @PersistenceUnit(unitName = "QuizolutePU")
    EntityManagerFactory emf;
    
    @Resource
    UserTransaction utx;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, NonexistentEntityException, RollbackFailureException, Exception {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        Users user = (Users) session.getAttribute("user");
        //For Edit Room
        if(request.getParameter("newRoomName") != null && request.getParameter("newQuestionSet") != null){
            String newRoomName = request.getParameter("newRoomName");
            
            String questionid = request.getParameter("newQuestionSet");
            int qid = Integer.valueOf(questionid);
            QuestionSetsJpaRepository qsj = new QuestionSetsJpaRepository(utx, emf);
            QuestionSets questionSet = qsj.findQuestionSets(qid);
            
            String room = request.getParameter("room");
            int roomId = Integer.valueOf(room);
            RoomsJpaRepository rjr = new RoomsJpaRepository(utx, emf);
            Rooms existRoom = rjr.findRooms(roomId);
            existRoom.setName(newRoomName);
            existRoom.setQuestionSetId(questionSet);
            rjr.edit(existRoom);
            UsersJpaRepository ujr = new UsersJpaRepository(utx, emf);
//          Update user's Room 
            user = ujr.findUsers(user.getId());
            session.setAttribute("user", user);
//            Update room in session
            List<Rooms> userRoom = user.getRoomsList();
            session.setAttribute("allRoom", userRoom);
            response.sendRedirect("/Quizolute/AdminPanel/ManageRoom");
        }
        
        
        
    }

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
        try {
            HttpSession session = request.getSession(false);
            Users user = (Users) session.getAttribute("user");
            
            //For Delete Room
        if(request.getParameter("delete") != null){
            System.out.println("SSS1");
            String delId = request.getParameter("delete");
            int deleteId = Integer.valueOf(delId);
            RoomsJpaRepository rjr = new RoomsJpaRepository(utx, emf);
            Rooms room = rjr.findRooms(deleteId);
            if(room.getOwnerId().getId() == user.getId()){
                List<UserInRoom> usersInRoom = room.getUserInRoomList();
                UserInRoomJpaRepository uij = new UserInRoomJpaRepository(utx, emf);
                UserAnswersJpaRepository uaj = new UserAnswersJpaRepository(utx, emf);
                List<Integer> list = uij.getAllIdByRoomId(deleteId);
                for (Integer integer : list) {
                    uij.destroy(integer);
                }
                
                list = uaj.getAllUserAnswerIdByRoomId(deleteId);
                for (Integer integer : list) {
                    uaj.destroy(integer);
                }
                rjr.destroy(deleteId);
                request.setAttribute("message", "Delele Complete");
                
                UsersJpaRepository ujr = new UsersJpaRepository(utx, emf);
    //          Update user's Room 
                user = ujr.findUsers(user.getId());
                session.setAttribute("user", user);
    //            Update room in session
                List<Rooms> allRoom = user.getRoomsList();
                session.setAttribute("allRoom", allRoom);
                response.sendRedirect("/Quizolute/AdminPanel/ManageRoom");
                return;
            }
            getServletContext().getRequestDispatcher("/pages/admin/manageroom.jsp").forward(request, response);
        }
        } catch (RollbackFailureException ex) {
            Logger.getLogger(EditRoomServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(EditRoomServlet.class.getName()).log(Level.SEVERE, null, ex);
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
        try {
            processRequest(request, response);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(EditRoomServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(EditRoomServlet.class.getName()).log(Level.SEVERE, null, ex);
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
