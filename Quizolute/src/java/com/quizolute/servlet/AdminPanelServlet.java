/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quizolute.servlet;

import com.quizolute.jpa.QuestionSetsJpaController;
import com.quizolute.jparepository.QuestionSetsJpaRepository;
import com.quizolute.jparepository.RoomsJpaRepository;
import com.quizolute.jparepository.UsersJpaRepository;
import com.quizolute.model.QuestionSets;
import com.quizolute.model.Rooms;
import com.quizolute.model.Users;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
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
public class AdminPanelServlet extends HttpServlet {

    @PersistenceUnit(unitName = "QuizolutePU")
    EntityManagerFactory emf;

    @Resource
    UserTransaction utx;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        emf.getCache().evictAll();
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");
        if (request.getParameter("title") != null) {
            System.out.println("This is title : " + request.getParameter("title"));
            getServletContext().getRequestDispatcher("/pages/admin/home.jsp").forward(request, response);

        } else if (request.getParameter("startroomid") != null) {
            RoomsJpaRepository rjr = new RoomsJpaRepository(utx, emf);
            if (!request.getParameter("startroomid").isEmpty()) {
                Rooms room = rjr.findRooms(Integer.valueOf(request.getParameter("startroomid")));
                if (room != null) {
                    response.sendRedirect("StartQuiz?room_id=" + room.getId());
                } else {
                    response.sendRedirect("Quizolute/AdminPanel?title=manageroom");
                }
            } else {
                response.sendRedirect("Quizolute/AdminPanel?title=manageroom");
            }
        } else {

            getServletContext().getRequestDispatcher("/pages/admin/home.jsp").forward(request, response);
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
        processRequest(request, response);
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
        processRequest(request, response);
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
