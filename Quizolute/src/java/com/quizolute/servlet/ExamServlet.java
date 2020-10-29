/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quizolute.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.quizolute.jparepository.ChoicesJpaRepository;
import com.quizolute.jparepository.QuestionSetsJpaRepository;
import com.quizolute.jparepository.QuestionsJpaRepository;
import com.quizolute.jparepository.RoomsJpaRepository;
import com.quizolute.jparepository.UserAnswersJpaRepository;
import com.quizolute.model.Answer;
import com.quizolute.model.Choices;
import com.quizolute.model.QuestionSets;
import com.quizolute.model.Questions;
import com.quizolute.model.Rooms;
import com.quizolute.model.Summary;
import com.quizolute.model.UserAnswers;
import com.quizolute.model.Users;
import com.quizolute.util.JavaClassConverter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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
public class ExamServlet extends HttpServlet {

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
        String color = "red", message = "";
        QuestionSetsJpaRepository qsjc = new QuestionSetsJpaRepository(utx, emf);
        if (request.getParameter("room_id") != null && !request.getParameter("room_id").isEmpty()) {
            RoomsJpaRepository rjc = new RoomsJpaRepository(utx, emf);
            Rooms room = rjc.findRooms(Integer.valueOf(request.getParameter("room_id")));
            QuestionSets questionSet = room.getQuestionSetId();

            if (questionSet != null) {
                request.setAttribute("questionSet", questionSet);
                request.setAttribute("room", room);
                request.getRequestDispatcher("/pages/exam.jsp").forward(request, response);
            } else {
                message = "Questionset is not found";
                String url = "Home?color=" + color + "&message="+message;
                response.sendRedirect(url);
            }
        } else {
            message = "Parameter is wrong or Room is not found";
            String url = "Home?color=" + color + "&message=" + message;
            response.sendRedirect(url);
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
        StringBuilder sb = readJsonRequestBody(request);
        JsonObject jsonObjectRequest = new JsonParser().parse(sb.toString()).getAsJsonObject();

        PrintWriter responseWriter = response.getWriter();
        JsonObject responseData = new JsonObject();
        

        if (jsonObjectRequest.get("room_id") == null
                || jsonObjectRequest.get("room_id").getAsString().isEmpty()
                || jsonObjectRequest.get("questionset_id") == null
                || jsonObjectRequest.get("questionset_id").getAsString().isEmpty()
                || jsonObjectRequest.getAsJsonArray("question_answers") == null
                || jsonObjectRequest.getAsJsonArray("question_answers").size() == 0) {
            
            response.setStatus(500);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            responseData.addProperty("error", "invalid data.");
            responseWriter.print(responseData.toString());
            responseWriter.flush();
        } else {

            QuestionSetsJpaRepository qsjc = new QuestionSetsJpaRepository(utx, emf);
            ChoicesJpaRepository cjc = new ChoicesJpaRepository(utx, emf);
            QuestionsJpaRepository qjc = new QuestionsJpaRepository(utx, emf);
            RoomsJpaRepository rjc = new RoomsJpaRepository(utx, emf);
            UserAnswersJpaRepository uajc = new UserAnswersJpaRepository(utx, emf);

            int questionSet_id = jsonObjectRequest.get("questionset_id").getAsInt();
            QuestionSets questionSet = qsjc.findQuestionSets(questionSet_id);

            JsonArray question_answers = jsonObjectRequest.getAsJsonArray("question_answers");
            Iterator<JsonElement> iter = question_answers.iterator();

            ArrayList<Answer> answerList = new ArrayList<>();
            Summary summary = null;

            while (iter.hasNext()) {
                JsonObject question_answer = iter.next().getAsJsonObject();

                Questions question = qjc.findQuestions(question_answer.get("question_id").getAsInt());
                int choice_id = question_answer.get("answer_id").getAsInt();

                ArrayList<Choices> answerChoiceList = new ArrayList<>();
                answerChoiceList.add(cjc.findChoices(choice_id));
                answerList.add(new Answer(question, answerChoiceList));
            }

            summary = new Summary(questionSet, answerList);

            UserAnswers userAnswer = new UserAnswers(
                    1,
                    JavaClassConverter.serialize(summary),
                    new java.sql.Date(new Date().getTime()));
            userAnswer.setQuestionSetId(questionSet);
            userAnswer.setRoomId(rjc.findRooms(jsonObjectRequest.get("room_id").getAsInt()));
            userAnswer.setUserId((Users) request.getSession().getAttribute("user"));

            try {
                uajc.create(userAnswer);
            } catch (Exception ex) {
                Logger.getLogger(ExamServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            response.setStatus(200);
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

    private StringBuilder readJsonRequestBody(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException ex) {
            Logger.getLogger(ExamServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sb;
    }
}
