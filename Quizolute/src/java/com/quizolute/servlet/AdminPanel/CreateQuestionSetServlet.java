/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quizolute.servlet.AdminPanel;

import com.quizolute.jparepository.ChoicesJpaRepository;
import com.quizolute.jparepository.QuestionSetsJpaRepository;
import com.quizolute.jparepository.QuestionsJpaRepository;
import com.quizolute.model.Choices;
import com.quizolute.model.QuestionSets;
import com.quizolute.model.Questions;
import com.quizolute.model.UserAnswers;
import com.quizolute.model.Users;
import com.quizolute.util.StringGenerator;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Date;
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
public class CreateQuestionSetServlet extends HttpServlet {

    @PersistenceUnit(unitName = "QuizolutePU")
    EntityManagerFactory emf;

    @Resource
    UserTransaction utx;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        Users user = (Users) session.getAttribute("user");
        ChoicesJpaRepository cjpa = new ChoicesJpaRepository(utx, emf);
        QuestionsJpaRepository qjpa = new QuestionsJpaRepository(utx, emf);
        QuestionSetsJpaRepository qsjpa = new QuestionSetsJpaRepository(utx, emf);

        ArrayList<Questions> questionsList = new ArrayList<>();
        int questionsCount = new Integer(request.getParameter("questions-count"));
        boolean isPublic = (request.getParameter("privacy").equals("Public"));
        System.out.println("Public : " + isPublic);

        QuestionSets questionSet = new QuestionSets(Integer.SIZE, request.getParameter("title"), isPublic, new java.sql.Date(new Date().getTime()), new java.sql.Date(new Date().getTime()));
        questionSet.setOwnerId(user);

        int questionLastIndex = qjpa.getLastId();

        for (int i = 1; i <= questionsCount; i++) {
            ArrayList<Choices> choiceList = new ArrayList<>();

            String questionName = request.getParameter(String.format("question%d-name", i));
            double questionMaxScore = new Double(request.getParameter(String.format("question%d-maxScore", i)));
            Questions question = new Questions(questionLastIndex++, questionName, questionMaxScore);
            System.out.println("_Question : " + i + " | " + questionName);
            int count = new Integer(request.getParameter(String.format("question%d-count", i)));
            int choiceLastIndex = cjpa.getLastId();

            for (int j = 1; j <= count; j++) {
                String choiceName = request.getParameter(String.format("question%d-choice%d", i, j));
                double choiceScore = questionMaxScore;
                boolean choiceCorrect = (request.getParameter(String.format("question%d-isCorrect%d", i, j)) != null);
                Choices choice = new Choices(choiceLastIndex++, choiceName, choiceScore, choiceCorrect);
                choiceList.add(choice);
                try {
                    cjpa.create(choice);
                } catch (IllegalArgumentException e) {
                    System.out.println(e.toString());
                }
                System.out.println("__Choice " + j + " : " + choiceName + " (" + choiceScore + ") " + " isCorrect : " + choiceCorrect);
            }
            question.setChoicesList(choiceList);
            try {
                qjpa.create(question);
            } catch (IllegalArgumentException e) {
                System.out.println(e.toString());
            }
            questionsList.add(question);
            System.out.println("_Question Max Score : " + questionMaxScore);
            System.out.println("===========================");
        }

        questionSet.setQuestionsList(questionsList);
        try {
            qsjpa.create(questionSet);
        } catch (IllegalArgumentException e) {
            System.out.println(e.toString());
        }
        response.sendRedirect("/Quizolute/AdminPanel/ManageQuiz");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getServletContext().getRequestDispatcher("/pages/admin/createquiz.jsp").forward(request, response);
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
            Logger.getLogger(CreateQuestionSetServlet.class.getName()).log(Level.SEVERE, null, ex);
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
