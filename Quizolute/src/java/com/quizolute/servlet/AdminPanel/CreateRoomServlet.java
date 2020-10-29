package com.quizolute.servlet.AdminPanel;

import com.quizolute.jparepository.QuestionSetsJpaRepository;
import com.quizolute.jparepository.RoomsJpaRepository;
import com.quizolute.jparepository.UsersJpaRepository;
import com.quizolute.model.QuestionSets;
import com.quizolute.model.Rooms;
import com.quizolute.model.Users;
import com.quizolute.util.StringGenerator;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.Session;
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
public class CreateRoomServlet extends HttpServlet {
    @PersistenceUnit(unitName = "QuizolutePU")
    EntityManagerFactory emf;
    
    @Resource
    UserTransaction utx;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        request.setCharacterEncoding("UTF-8");
        int invitationCodeLength = 6;
        String roomName;
        int maxId = -1;
        HttpSession session = request.getSession(false);
        Users user = (Users) session.getAttribute("user");
        if(request.getParameter("roomname") != null){
            roomName = (String) request.getParameter("roomname");
            RoomsJpaRepository rjr = new RoomsJpaRepository(utx, emf);
            List<Rooms> existRoom = rjr.findRoomsEntities();
            for (Rooms rooms : existRoom) {
                if(maxId < rooms.getId()){
                    maxId = rooms.getId();
                }
            }
            Rooms newRoom = new Rooms(
                    ++maxId, 
                    roomName, 
                    StringGenerator.generateRandomString(invitationCodeLength).toLowerCase(), 
                    true, 
                    new java.sql.Date(new Date().getTime()));
//            Waiting for AdminUser
            newRoom.setOwnerId(user);
//            Waiting for question set id
            String questionid = request.getParameter("selectQuestionSet");
            int qid = Integer.valueOf(questionid);
            QuestionSetsJpaRepository qsj = new QuestionSetsJpaRepository(utx, emf);
            QuestionSets questionSet = qsj.findQuestionSets(qid);
            newRoom.setQuestionSetId(questionSet);
            rjr.create(newRoom);
            UsersJpaRepository ujr = new UsersJpaRepository(utx, emf);
//          Update user's Room 
            user = ujr.findUsers(user.getId());
            session.setAttribute("user", user);
//            Update room in session
            List<Rooms> room = user.getRoomsList();
            session.setAttribute("allRoom", room);
        }
        response.sendRedirect("/Quizolute/AdminPanel/ManageRoom");
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
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(CreateRoomServlet.class.getName()).log(Level.SEVERE, null, ex);
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
        } catch (Exception ex) {
            Logger.getLogger(CreateRoomServlet.class.getName()).log(Level.SEVERE, null, ex);
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
