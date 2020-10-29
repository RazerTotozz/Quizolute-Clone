package com.quizolute.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.quizolute.jparepository.RoomsJpaRepository;
import com.quizolute.jparepository.UserInRoomJpaRepository;
import com.quizolute.jparepository.UsersJpaRepository;
import com.quizolute.model.Rooms;
import com.quizolute.model.UserInRoom;
import com.quizolute.model.Users;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

/**
 *
 * @author ThisPz
 */
public class JoinRoomServlet extends HttpServlet {

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

        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");
        System.out.println("Current User : " + user);
        request.setAttribute("user", user);
        request.getRequestDispatcher("/pages/home.jsp").forward(request, response);
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
        request.setCharacterEncoding("UTF-8");
        String invitationCode = request.getParameter("invitationCode").trim();
        String message = "", color = "";
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");
        if (invitationCode.length() != 6) {
            message = "Code is invalid!";
            color = "red";
        } else {
            emf.getCache().evictAll();
            RoomsJpaRepository rjpa = new RoomsJpaRepository(utx, emf);
            Rooms room = rjpa.getRoomByInvitationCode(invitationCode);
            if (room != null) {
                if (room.getIsActive()) {
                    UserInRoomJpaRepository uirjc = new UserInRoomJpaRepository(utx, emf);

                    UserInRoom userInRoom = uirjc.getUserInRoomByRoomAndUser(room.getId(), user.getId());
                    if (userInRoom == null) {
                        userInRoom = new UserInRoom();
                        userInRoom.setUserId(user);
                        userInRoom.setRoomId(room);
                        try {
                            uirjc.create(userInRoom);
                        } catch (Exception ex) {
                            Logger.getLogger(JoinRoomServlet.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    UsersJpaRepository ujc = new UsersJpaRepository(utx, emf);
                    request.setAttribute("user", ujc.findUsers(user.getId()));
                    session.setAttribute("room", room);
                    request.setAttribute("userinroom", userInRoom);
                    request.getRequestDispatcher("/pages/waiting.jsp").forward(request, response);
                    return;
                } else {
                    message = "This room is not open yet.";
                    color = "red";
                }
            } else {
                message = "Room not found!";
                color = "red";
            }
            request.setAttribute("message", message);
            request.setAttribute("color", color);
            request.getRequestDispatcher("/pages/home.jsp").forward(request, response);
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
