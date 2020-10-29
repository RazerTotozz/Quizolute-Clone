<%-- 
    Document   : exam
    Created on : Nov 5, 2019, 3:15:08 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@include file="/pages/includes/header.jsp" %>

<div id="wrapper">
    <!-- Sidebar -->
    <ul class="sidebar navbar-nav">
        <li class="nav-item active">
            <p class="nav-link">
                <span>
                    ${questionSet.name}
                </span>
            </p>
        </li>
        <c:forEach var="question" items="${questionSet.questionsList}">
            <li class="nav-item">
                <a class="nav-link" href="#question${question.id}">
                    <i class="fas fa-fw"></i>
                    ${question.name}</a>
            </li>
        </c:forEach>
    </ul>
    <div id="content-wrapper">

        <div class="container-fluid">

            <c:forEach var="question" items="${questionSet.questionsList}">
                <div class="row justify-content-md-center">
                    <div class="col-xl-5 col-sm-10 mb-3">
                        <div class="card o-hidden h-100">
                            <div class="card-body">
                                <div class="card-body-icon">
                                    <i class="fas fa-fw fa-comments"></i>
                                </div>
                                <div class="card-title mr-5" id="question${question.id}">
                                    <h3 class="question" id="${question.id}">${question.name}</h3>
                                </div>
                                <div class="btn-group-toggle" data-toggle="buttons">
                                    <c:forEach var="choice" items="${question.choicesList}">
                                        <label class="card-text btn btn-block btn-outline-dark mb-1 answer" id="${choice.id}">
                                            <input type="radio" name="options" autocomplete="off"> ${choice.name}
                                        </label><br>
                                    </c:forEach>
                                </div>

                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>

        </div>
        <!-- /.container-fluid -->
        <div class="row justify-content-md-center mt-5">
            <button type="button" class="btn btn-primary btn-lg" 
                    data-toggle="modal" data-target="#submitModal" 
                    id='submit-answer-btn' disabled>
                SUBMIT
            </button>
        </div>
    </div>
    <!-- /.content-wrapper -->
</div>
<!-- /#wrapper -->
<div class="modal fade" id="submitModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="submitModalLabel">Sumbit Confirm</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" data-dismiss="modal" 
                        id="modal-exam-sumbit">Submit</button>
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>

<a class="scroll-to-top rounded" href="#page-top">
    <i class="fas fa-angle-up ion-arrow-up-b"></i>
</a>

<script>
    $(document).on('click', 'button#modal-exam-sumbit', function (event) {
        var answers = $(".answer.active");
        var questions = $(".question");
        var question_answers = [];


        var message = "";
        var color = "red";


        $.map(answers, function (answer, i) {
            question_answers.push({"question_id": questions[i].id,
                "answer_id": answer.id
            });
        });

        $.ajax({
            type: "POST",
            url: "/Quizolute/Exam",
            data: JSON.stringify({"room_id": "${room.id}"
                , "questionset_id": "${questionSet.id}"
                , "question_answers": question_answers}),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            statusCode: {
                200: function(){
                    var message = "You finished the exam. Good Luck!";
                    var color = "green";
                    var url = "/Quizolute/Home?color=" + color + "&message=" + message;
                    $(location).attr('href', url);
                },
                500: function(data){
                    console.log("in 200");
                    var message = data.error;
                    var color = "red";
                    var url = "/Quizolute/Home?color=" + color + "&message=" + message;
                    $(location).attr('href', url);
                }
            }
        });
//         response data should be
//         {
//              room_id : id,
//              questionset_id : id,
//              question_answers : [
//                                    {
//                                      question_id: id,
//                                      answer_id: id
//                                    },
//                                    {
//                                      question_id: id,
//                                    answer_id: id
//                                    }
//                                  ]
//          }
    });

    $(".answer").click(function () {
        $(document).ready(function () {
            var numberOfQuestion = ${fn:length(questionSet.questionsList)};
            var answerCount = $(".answer.active").length;
            if (numberOfQuestion === answerCount) {
                $("#submit-answer-btn").prop('disabled', false);
            }
        });
    });
</script>

<%@include file="includes/footer.jsp" %>
