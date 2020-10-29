package com.quizolute.servlet;

import com.quizolute.jpa.exceptions.NonexistentEntityException;
import com.quizolute.jpa.exceptions.RollbackFailureException;
import com.quizolute.jparepository.UsersJpaRepository;
import com.quizolute.model.Users;
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
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

public class EditProfileServlet extends HttpServlet {
    
    @PersistenceUnit(unitName = "QuizolutePU")
    EntityManagerFactory emf;
    
    @Resource
    UserTransaction utx;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        getServletContext().getRequestDispatcher("/pages/editprofile.jsp").forward(request, response);
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if(request.getParameter("password") != null && request.getParameter("password2")!=null){
            String password = request.getParameter("password");
            String password2 = request.getParameter("password2");
            if(password != password2){
                request.setAttribute("message", "Password does not match.");
            } else{
                
                try {
                    HttpSession session = request.getSession();
                    Users user = (Users) session.getAttribute("user");
                    UsersJpaRepository ujr = new UsersJpaRepository(utx, emf);
                    user = ujr.findUsers(user.getId());
                    user.setPassword(password);
                    ujr.edit(user);
                    request.setAttribute("color", "green");
                    request.setAttribute("message", "Change Password Successful.");
                    getServletContext().getRequestDispatcher("/pages/editprofile.jsp").forward(request, response);
                    return;
                } catch (NonexistentEntityException ex) {
                    Logger.getLogger(EditProfileServlet.class.getName()).log(Level.SEVERE, null, ex);
                } catch (RollbackFailureException ex) {
                    Logger.getLogger(EditProfileServlet.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(EditProfileServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        request.setAttribute("color", "red");
        request.setAttribute("message", "Change Password Unsuccessful.");
        getServletContext().getRequestDispatcher("/pages/editprofile.jsp").forward(request, response);
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
