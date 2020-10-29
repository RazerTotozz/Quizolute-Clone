<%-- 
    Document   : Login
    Created on : Oct 31, 2019, 4:58:33 AM
    Author     : sittiwatlcp
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/pages/includes/header.jsp" %>
<h1>Login</h1>
<form method="POST" action="/Quizolute/Login">
    <input type="text" name="username" /><br/>
    <input type="password" name="password" /><br/>
    <button type="submit">Login</button>
</form>
<%@include file="/pages/includes/footer.jsp" %>