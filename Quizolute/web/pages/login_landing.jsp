<%-- 
    Document   : template
    Created on : Nov 2, 2019, 4:56:36 PM
    Author     : sittiwatlcp
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/pages/includes/header.jsp" %>
<div class="container-fluid">
    <section class="box-intro">
        <div class="table-cell">
            <h1 class="box-headline h1">
                <b class="is-visible">Welcome ${user.getFirstName()}!</b>
            </h1>
            <h5>Ready !?</h5>
        </div>
    </section>
</div>
<%@include file="/pages/includes/footer.jsp" %>
