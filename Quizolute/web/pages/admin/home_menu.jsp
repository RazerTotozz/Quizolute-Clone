<%-- 
    Document   : home_menu
    Created on : Nov 25, 2019, 4:04:48 AM
    Author     : sittiwatlcp
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div class="col-lg-4 col-md-12 service-box">
    <div class="list-group" style="margin:0 10%;">
        ${requestScope['javax.servlet.forward.request_uri']}
        <a href="/Quizolute/AdminPanel/ManageQuiz" class="list-group-item list-group-item-action ${requestScope['javax.servlet.forward.request_uri'] == "/Quizolute/AdminPanel/ManageQuiz" ? 'active' : ""}">Manage Quiz</a>
        <a href="/Quizolute/AdminPanel/ManageRoom" class="list-group-item list-group-item-action ${requestScope['javax.servlet.forward.request_uri'] ==  "/Quizolute/AdminPanel/ManageRoom" ? 'active' : ""}">Manage Room</a>
    </div>
</div>