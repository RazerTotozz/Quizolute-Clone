/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quizolute.servlet;

import com.quizolute.jparepository.UsersJpaRepository;
import com.quizolute.model.Users;
import com.quizolute.util.GlobalConfig;
import com.quizolute.util.LDAPGetData;
import com.quizolute.util.LDAPLogin;
import java.io.IOException;
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
 * @author sittiwatlcp
 */
public class LoginServlet extends HttpServlet {

    @PersistenceUnit(unitName = "QuizolutePU")
    EntityManagerFactory emf;

    @Resource
    UserTransaction utx;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/index.jsp").forward(request, response);
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
        response.setContentType("text/html;charset=UTF-8");

        GlobalConfig cfg = new GlobalConfig();

        boolean login = false;
        LDAPGetData ldata = null;

        if (cfg.devMode) {
            login = true;
        } else {
            try {
                ldata = new LDAPGetData(request.getParameter("username"));
                login = true;
            } catch (Error | IllegalArgumentException e) {
                request.setAttribute("message", "Username or Password not correct.");
                request.setAttribute("color", "red");
                System.out.println("Username or Password not correct.");
                request.getRequestDispatcher("/index.jsp").forward(request, response);
                return;
            }
        }

        if (login) {
            System.out.println("Loged in!");
            String username = request.getParameter("username");
          
            //Check if user in DB
            UsersJpaRepository ujpa = new UsersJpaRepository(utx, emf);
            Users user = ujpa.getUserByUsername(username);

            if (user == null) {
                //Do something when newly user has login
                Users newUser;
                if (cfg.devMode) {
                    System.out.println("DEV");
                    newUser = new Users(Integer.SIZE, "61130500108", "SITTIWAT", "JIRAJAROENYINGSUK", "user", true, false);
                } else {
                    newUser = new Users(Integer.SIZE, ldata.getUsername(), ldata.getFirstName(), ldata.getLastName(), "user", true, false);
                }

                ujpa.insertUser(newUser);
                user = ujpa.getUserByUsername(newUser.getUsername());

            }
            ujpa.onlineUser(user);
            HttpSession session = request.getSession(true);
            session.setAttribute("user", user);
            response.setHeader("Refresh", "2; URL=/Quizolute/Home");

            request.getRequestDispatcher("/pages/login_landing.jsp").forward(request, response);
        } else {
            request.setAttribute("message", "Username or Password not correct.");
            request.setAttribute("color", "red");
            System.out.println("Username or Password not correct.");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
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
