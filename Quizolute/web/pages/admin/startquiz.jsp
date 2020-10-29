<%-- 
    Document   : startquiz
    Created on : Nov 17, 2019, 10:30:44 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%--<%@include file="../includes/header.jsp" %>--%>
<jsp:include page="../includes/header.jsp"/>
<section class="box-intro">
    <div class="table-cell">
        <h5 class="center">Use any device to open</h5>
        <h4 class="box-headline letters rotate-2">
            <span class="box-words-wrapper">
                <b class="is-visible">QUIZOLUTE.COM</b>
            </span>
        </h4>
        <h1>Invitation Code : ${room.invitationCode}</h1>
        <button type="button" class="yellow-button mt-4" style="font-size:1.5em" id="start-end-test-btn" data-toggle="modal" data-target="#changeRoomStatusModal" >START</button>
        <h4 class="pt-3 center">Participant in room.</h4>

        <div class="container">
            <div class="row center" id="user-in-room-div">
            </div>
        </div>
    </div>
</section>


<div class="modal fade" id="changeRoomStatusModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="changeRoomStatusModalLabel" class="">Start Room Confirmation</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class ="modal-body">

            </div>
            <div class="modal-footer">
                <button type="button" class="create-button" data-dismiss="modal" onClick="startTest()" id='confimStart'>Confirm</button>
                <button type="button" class="grey-button" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>

<!--<div class="modal fade" id="KickUserModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="kickUserModalLabel">Kick user confirmation</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-warning" data-dismiss="modal" onClick="startTest()">Kick</button>
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>-->

<script>

    var interval = 1000 * 5;
    function appendStudent() {
        $.ajax({
            url: "api/UserInRooms?room_id=" +${room.id} + "&questionset_id=" +${questionset.id},
            complete: function (data) {
                setTimeout(appendStudent, interval);
            }
        }).done(function (data, textStatus, xhr) {
            if (xhr.status === 200) {
                console.log(data);
                if (data.studentcount !== 0) {
                    var students = data.students;
                    var showDiv = $("#user-in-room-div").empty();
                    if (students !== null) {
                        for (var i = 0; i < students.length; i++) {
                            if (students[i].is_done) {
                                showDiv.append("<span></h3><h3 style=\"display:inline\" class=student id=student" + students[i].user_in_room_id + ">" + students[i].name + "<h3 style=\"color:green;display:inline\"> DONE! </h3></span>");
                            } else {
                                showDiv.append("<div class='col-md-3 col-sm-4 col-12 player-box' type=button onClick=kickStudent(" + students[i].user_in_room_id + ") type=\"button\" data-toggle=\"modal\" data-target=\"#KickUserModal\" class=student id=student" + students[i].user_in_room_id + ">" + students[i].name + "</div>");
                            }
                        }
                    }
                }

            } else {
                console.log("api error");
            }
        });
    }

    function kickStudent(id) {
        var is_active = ${room.isActive};
        console.log(is_active);
        $.ajax({
            url: "api/UserInRooms",
            data: {user_in_room_id: id},
            method: "POST",
            statusCode: {
                200: function () {
                    alert("User has been kicked ( uir_id : " + id + " )");
                }
            }
        }).fail(function () {
            alert("Cannot kick user while started doing quiz");
        });

    }

    let count = false;
    function startTest() {
        $.ajax({
            url: "StartQuiz",
            data: {room_id: ${room.id}, status: count},
            method: "POST",
            statusCode: {
                200: function (data, textStatus, xhr) {
                    if (!data.is_active) {
                        $("#start-end-test-btn").text("End test");
                        $("#start-end-test-btn").attr('class', 'btn btn-warning');
                        $("#changeRoomStatusModalLabel").text('Close Room Confirmation');
                        $("#changeRoomStatusModalLabel").attr('style', '');
                        $('#confimStart').attr('class', 'btn btn-warning');
                    } else {
                        var url = "AdminPanel?title=manageroom";
                        $(location).attr('href', url);
                    }

                }
            }
        });
        count = !count;
    }


    $(document).ready(function () {
        setTimeout(appendStudent(), interval);
    });
</script>
<jsp:include page="../includes/footer.jsp"/>
<%--<%@include file="../pages/includes/footer.jsp" %>--%>
