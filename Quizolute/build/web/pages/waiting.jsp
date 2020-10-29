<%-- 
    Document   : waiting
    Created on : Nov 5, 2019, 10:15:23 AM
    Author     : ThisPz
--%>

<%@include file="/pages/includes/header.jsp" %>
<div class="container-fluid">
    <section class="box-intro">
        <div class="table-cell">
            <h1 class="box-headline letters rotate-2">
                <span class="box-words-wrapper">
                    <b class="is-visible">Quizolute</b>
                    <b>Waiting...</b>
                </span>
            </h1>
            <h5>You are in the Room. Waiting for start...</h5>
            <h2>You're waiting in ${room.name}.</h2>
            <a href="/Quizolute/ExitRoom?user_in_room_id=${userinroom.id}">
                <button type="button" class="delete-button mt-4">Exit Room</button>
            </a>
    </section>
</div>

<script>
    var interval = 1000 * 5;
    function waitForRoomActive() {
        $.ajax({
            url: "api/UserWaiting?room_id=" +${room.id},
            complete: function (data) {
                setTimeout(waitForRoomActive, interval);
            }
        }).done(function (data, textStatus, xhr) {
            if (xhr.status === 200) {
                console.log(data);
                if (data.questionset_id !== 0) {
                    var url = "Exam?title=manageroom&room_id=" +${room.id};
                    $(location).attr('href', url);
                }

            } else {
                console.log("Quiz is not start yet.");
            }
        });
    }

    function stillInThisRoom() {
        $.ajax({
            url: "api/IsUserInRoom?user_in_room_id=" +${userinroom.id},
            complete: function (data) {
                setTimeout(stillInThisRoom, interval);
            }
        }).done(function (data, textStatus, xhr) {
            if (xhr.status === 200) {
                console.log(data);
                if (!data.is_in_room) {
                    var url = "Home?color=red&message=You_has_been_kicked";
                    $(location).attr('href', url);
                }

            } else {
                var url = "Home?color=red&message=Invalid_room_id";
                $(location).attr('href', url);
            }
        });

    }

    $(document).ready(function () {
        setTimeout(waitForRoomActive(), interval);
        setTimeout(stillInThisRoom(), interval);
    });
</script>
<%@include file="/pages/includes/footer.jsp" %>