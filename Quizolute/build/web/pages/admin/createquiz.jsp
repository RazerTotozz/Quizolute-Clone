<%-- 
    Document   : createquiz
    Created on : Nov 10, 2019, 1:13:18 AM
    Author     : ThisPz
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%--<%@include file="../includes/header.jsp" %>--%>
<jsp:include page="home_header.jsp" />
<div class="container pt-3">
    <div class="col-12">
        <h3>Create a quiz</h3>
        <form method="POST">
            <h2 class="form-group">
                <label class="little">1. Name this quiz</label>
                <input type="text" name="title" placeholder="Quiz name" class="text-box" required />
            </h2>
            <div class="form-group">
                <label class="little">2. Set privacy</label>
                <select class="form-control" id="exampleFormControlSelect1" name="privacy">
                    <option>Public</option>
                    <option>Private</option>
                </select>
            </div>
            <hr/>
            <input hidden id="questions-count" name="questions-count" value="0"/>
            <script src="/Quizolute/pages/admin/createquiz.js?v=25"></script>

            <div class="form-group" id="question-content">

            </div>
            <div class="card">
                <div class="card-body text-center" style="background-color: #dadada87;padding: 80px 0px 80px;">
                    <a class="create-button" onclick="addQuestion()">+ Add Question</a>
                </div>
            </div>
            <div class="pt-3 pb-3">
                <div class="text-center" >
                    <button class="create-button m-auto" type="submit" >Save</button>
                </div>
            </div>
        </form>
    </div>
</div>
<jsp:include page="home_footer.jsp" />