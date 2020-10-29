/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quizolute.service;

import com.google.gson.JsonObject;
import com.quizolute.jpa.RoomsJpaController;
import com.quizolute.model.Answer;
import com.quizolute.model.Rooms;
import com.quizolute.model.Summary;
import com.quizolute.model.UserAnswers;
import com.quizolute.model.UserInRoom;
import com.quizolute.util.JavaClassConverter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
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
import javax.transaction.UserTransaction;

/**
 *
 * @author Totozz
 */
public class ExportSummaryService extends HttpServlet {

    @PersistenceUnit(unitName = "QuizolutePU")
    EntityManagerFactory emf;

    @Resource
    UserTransaction utx;

    private static final String SPACE_SEPARATOR = "-";

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

        JavaClassToCSV jtcsv = new JavaClassToCSV();
        RoomsJpaController rjc = new RoomsJpaController(utx, emf);

        PrintWriter responseWriter = response.getWriter();
        JsonObject responseData = new JsonObject();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        String message = "This room is not open yet.";
        String color = "red";
        response.setStatus(500);

        if (request.getParameter("room_id") != null || !request.getParameter("room_id").isEmpty()) {
            Rooms room = rjc.findRooms(Integer.valueOf(request.getParameter("room_id")));
            if (room == null) {
                response.setStatus(500);
                message = "Room not found";
                color = "red";
                responseData.addProperty("color", color);
                responseData.addProperty("message", message);
                responseWriter.print(responseData.toString());
            } else {
                List<UserAnswers> userAnswerList = room.getUserAnswersList();
                String resultTime = "";
                if (userAnswerList != null || !userAnswerList.isEmpty()) {
                    resultTime = userAnswerList.get(userAnswerList.size() - 1).getCreatedAt().toString().replaceAll(":", SPACE_SEPARATOR);
                } else {
                    resultTime = new Date().toString().replaceAll(":", SPACE_SEPARATOR);
                }
                String fileName;
                fileName = "Room " + room.getName() + " Summary-" + resultTime;
                fileName = fileName.replaceAll(" ", SPACE_SEPARATOR) + ".xls";
                jtcsv.writeToCSV(room, fileName);
                response.setStatus(200);
                PrintWriter pw = response.getWriter();
                pw.print("{ \"url\": \"" + fileName + "\" }");
            }
        } else {
            response.setStatus(500);
            message = "Invalid Parameter";
            color = "red";
            responseData.addProperty("color", color);
            responseData.addProperty("message", message);
            responseWriter.print(responseData.toString());
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

    class JavaClassToCSV {

        //European countries use ";" as 
        //CSV separator because "," is their digit separator
        private static final String CSV_SEPARATOR = ",";

        private void writeToCSV(Rooms room, String fileName) {

            BufferedWriter bw;
            FileOutputStream fos;
            File file;
            List<UserAnswers> userAnswerList = room.getUserAnswersList();
            try {
                file = new File("../docroot/" + fileName);
                System.out.println(file.getAbsolutePath());
                fos = new FileOutputStream(file);
                bw = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));

                ArrayList<Summary> summaryList = new ArrayList<>();
                for (UserAnswers userAnswer : userAnswerList) {
                    summaryList.add((Summary) JavaClassConverter.deserialize(userAnswer.getDataObj()));
                    
                }
                StringBuilder oneLine = new StringBuilder();
                    oneLine.append("ID");
                    oneLine.append(CSV_SEPARATOR);
                    oneLine.append("NAME");
                    oneLine.append(CSV_SEPARATOR);
                    oneLine.append("SCORE ("+ room.getQuestionSetId().getQuestionSetMaxScore() +")");
                    bw.write(oneLine.toString());
                    bw.newLine();
                    int index = 0;
                    for (UserInRoom userInRoomList : room.getUserInRoomList()) {
                        oneLine = new StringBuilder();
                        oneLine.append(userInRoomList.getUserId().getUsername());
                        oneLine.append(CSV_SEPARATOR);
                        oneLine.append(userInRoomList.getUserId().getFullName());
                        oneLine.append(CSV_SEPARATOR);
                        oneLine.append(summaryList.get(index++).getTotalScore());
                        bw.write(oneLine.toString());
                        bw.newLine();
                    }
                bw.flush();
                bw.close();

            } catch (FileNotFoundException ex) {
                Logger.getLogger(ExportSummaryService.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(ExportSummaryService.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ExportSummaryService.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ExportSummaryService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
