/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quizolute.servlet;

import com.quizolute.jpa.UserAnswersJpaController;
import com.quizolute.model.Answer;
import com.quizolute.model.Summary;
import com.quizolute.model.UserAnswers;
import com.quizolute.seeder.DatabaseSeederRunner;
import com.quizolute.util.JavaClassConverter;
import java.io.IOException;
import java.io.PrintWriter;
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
public class SeederServlet extends HttpServlet {

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
        if (false) {
            UserAnswersJpaController uajc = new UserAnswersJpaController(utx, emf);
            Summary summary = null;
            try {
                summary = (Summary) JavaClassConverter.deserialize(uajc.findUserAnswersEntities().get(uajc.findUserAnswersEntities().size()-1).getDataObj());
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(SeederServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("total score is " + summary.getTotalScore());
            for (Answer answer : summary.getAnswerList()) {
                System.out.println("\nquestion " + answer.getQuestion().getName() + " correct? = " + answer.getIsCorrect() + "\n"
                        + "answer choice = " + answer.getAnswerChoiceList().get(0) + " score is " + answer.getScore());
            }
        }
        forwardDispatcherToSeederPageWithResult(request, response, "Quizolut DB");
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
        DatabaseSeederRunner seeder = new DatabaseSeederRunner(emf, utx);
        if (request.getParameter("run") != null && !(request.getParameter("run").isEmpty()) && request.getParameter("run").equals("1")) {
            try {
                seeder.run();
                forwardDispatcherToSeederPageWithResult(request, response, "Done!");
            } catch (Exception ex) {
                forwardDispatcherToSeederPageWithResult(request, response, "Error Run Seeder");
                Logger.getLogger(SeederServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (request.getParameter("refresh") != null && !(request.getParameter("refresh").isEmpty()) && request.getParameter("refresh").equals("1")) {
            try {
                seeder.refresh();
                forwardDispatcherToSeederPageWithResult(request, response, "Done!");
            } catch (Exception ex) {
                forwardDispatcherToSeederPageWithResult(request, response, "Error Run Refresh");
                Logger.getLogger(SeederServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (request.getParameter("deletescore") != null && !(request.getParameter("deletescore").isEmpty()) && request.getParameter("deletescore").equals("1")) {
            try {
                seeder.deleteScore();
                forwardDispatcherToSeederPageWithResult(request, response, "Done!");
            } catch (Exception ex) {
                forwardDispatcherToSeederPageWithResult(request, response, "Error Run Refresh");
                Logger.getLogger(SeederServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (request.getParameter("createscore") != null && !(request.getParameter("createscore").isEmpty()) && request.getParameter("createscore").equals("1")) {
            try {
                seeder.makeScore();
                forwardDispatcherToSeederPageWithResult(request, response, "Done!");
            } catch (Exception ex) {
                forwardDispatcherToSeederPageWithResult(request, response, "Error Run Refresh");
                Logger.getLogger(SeederServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        response.sendRedirect("/Quizolute/Seeder");
    }

    private void forwardDispatcherToSeederPageWithResult(HttpServletRequest request, HttpServletResponse response, String result) throws ServletException, IOException {
        request.setAttribute("resultMessage", result);
        request.getRequestDispatcher("/pages/seeder.jsp").forward(request, response);
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
