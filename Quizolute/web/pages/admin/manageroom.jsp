<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="home_header.jsp" />
<jsp:include page="home_menu.jsp" />
<div class="col-lg-8 col-md-12">
    <!--    Room shows here.-->
    <!--${param.title}-->
    ${message}
    <button type="button" class="create-button" data-toggle="modal" data-target="#createNewRoom">Create Room</button>
    <div class="modal fade" id="createNewRoom" tabindex="-1" role="dialog" aria-labelledby="createNewRoomLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <form method="post" action="CreateRoom">
                    <div class="modal-header">
                        <h5 class="modal-title" id="createNewRoomLabel">Create New Room</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <div class="form-group row">
                            <label for="roomname" class="col-sm-2 col-form-label">Name</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" id="roomname" name="roomname" placeholder="Enter name here" required>
                            </div>
                            <div class="col-sm-10">
                                <!-- Show dropdown question list here -->
                                <div class="form-group">
                                    <label for="selectQuestionSet" class="col-sm-2 col-form-label">Question Set</label>
                                    <select class="col-form-label" id="selectQuestionSet" name="selectQuestionSet">
                                        <c:forEach items="${questionSet}" var = "question">
                                            <option value ="${question.id}">${question.name}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="grey-button" data-dismiss="modal">Close</button>
                            <input type="submit" class="blue-button" value="Save changes"/>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <div>
        <c:forEach items="${allRoom}" var="room">
            <div class="pt-4">
                <div class="card">
                    <div class="card-header">
                        ${room.id} : ${room.name}
                    </div>
                    <div class="card-body">
                        <h5 class="card-title">Invitation Code : ${room.invitationCode}</h5>
                        <p class="card-text">Question Set : ${room.questionSetId.name}</p>
                        <c:choose>
                            <c:when test="${empty room.userAnswersList}">
                                <a href="/Quizolute/AdminPanel?startroomid=${room.id}" type="button" class="create-button mr-2">Start Quiz</a>
                            </c:when>
                            <c:when test="${not empty room.userAnswersList}">
                                <!--data-toggle="modal" data-target="#summaryModal"-->
                                <a type="button" class="yellow-button mr-2" onClick="exportSummary(${room.id})" >Export Summary</a>
                            </c:when>
                        </c:choose>
                        <a type="button" data-toggle="modal" data-target="#editRoom${room.id}" class="blue-button mr-2" style="color:white;">Edit Room</a>
                        <a type="button" href="/Quizolute/AdminPanel/EditRoom?delete=${room.id}" class="delete-button">Delete Room</a>
                    </div>
                </div>
            </div>
            <form method="post" action="EditRoom">
                <input hidden name="room" value="${room.id}" />
                <div class="modal fade" id="editRoom${room.id}" tabindex="-1" role="dialog" aria-labelledby="editRoomLabel${room.id}" aria-hidden="true">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="editRoomLabel${room.id}">Edit Room ID : ${room.id}</h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <div class="form-group">
                                    <label for="room-detail${room.id}" class="col-form-label">Room Name :</label>
                                    <input type="text" class="form-control" id="room-detail${room.id}" value="${room.name}" name="newRoomName"/>
                                </div>
                                <div class="form-group">
                                    <label for="selectQuestionSet${room.id}" class="col-sm-2 col-form-label">Question Set</label>
                                    <select class="col-form-label" id="selectQuestionSet${room.id}" name="newQuestionSet">
                                        <c:forEach items="${questionSet}" var = "question">
                                            <option value ="${question.id}" ${room.questionSetId.id == question.id ? "selected" : ""}>${question.name}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                                <button type="submit" class="btn btn-primary">Save changes</button>
                            </div>
                        </div>
                    </div>
                </div>
            </form>
        </c:forEach>
    </div>

    <div class="modal fade" id="summaryModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="summaryModalLabel">Export Summary Success!</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <h4 id="summaryModalBody"></h4>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>

    <script>
        function exportSummary(id) {
            $.ajax({
                url: "ExportSummary?room_id=" + id,
                success: function (data) {
                    $("#summaryModalLabel").text("Export Summary Success!");
                    $("#summaryModal").modal('show');
                    window.location.replace("/" + data.url);
                }
            }).fail(function (data) {
                $("#summaryModalLabel").text("Export Summary Error");
                $("#summaryModalBody").text(data.message);
                $("#summaryModal").modal('show');
            });
        }
    </script>

</div>
<jsp:include page="home_footer.jsp" />