<%-- 
    Document   : template
    Created on : Nov 2, 2019, 4:56:36 PM
    Author     : sittiwatlcp
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/pages/includes/header.jsp" %>
<div>
    <h3>Hello ${user.getFullName()}</h3>
    <h3>id : ${user.getId()}</h3>
    <h3>std id : ${user.getUsername()}</h3>
    <h3>active status : ${user.getActive()}</h3>
    <h3>user type : ${user.getType()}</h3>
    <h3>disable status : ${user.getDisabled()}</h3>

</div>
<%@include file="/pages/includes/footer.jsp" %>
