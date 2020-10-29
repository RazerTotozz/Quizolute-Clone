<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="includes/header.jsp" %>
<script>
    var check = function() {
    if (document.getElementById('inputPassword').value ===
      document.getElementById('inputPassword2').value) {
        
      document.getElementById('message').innerHTML = '';;
    } else {
      document.getElementById('message').style.color = 'red';
      document.getElementById('message').innerHTML = 'Password does not match.';
    }
  }
</script>
<div class="container-fluid">
    <header class="box-header">
        <div class="box-logo">
            <a href="index.html"><img src="img/logo.png" width="80" alt="Logo"></a>
        </div>
        <a class="box-primary-nav-trigger" href="#0">
            <span class="box-menu-text">Menu</span><span class="box-menu-icon"></span>
        </a>
    </header>
    <nav>
        <ul class="box-primary-nav">
            <li class="box-label">Welcome ${user.firstName}</li>
                <c:choose>
                    <c:when test="${user.type == 'admin'}">
                    <li><a href="/Quizolute/AdminPanel?title=default">Panel</a></li>
                    </c:when>
                    <c:otherwise>
                    <li><a href="/Quizolute/JoinRoom">Join Room</a> </li>
                    </c:otherwise>
                </c:choose>
            <li><a href="/Quizolute/EditProfile">Edit Profile</a><i class="ion-ios-circle-filled color"></i></li>
            <li><a href="/Quizolute/Logout">Log Out</a></li>
        </ul>
    </nav>
    <div class="top-bar">
        <h1>Edit Profile</h1>
    </div>
    <div class="container main-container">
        <div class="row">
            <div class="col-12 service-box">
                <form method="post" action="EditProfile">
                <h3>Change Password</h3>
                <hr/>
                <div class="form-group row pt-2">
                    <label for="inputPassword" class="col-sm-2 col-form-label ">Password</label>
                    <div class="col-sm-10">
                        <input type="password" class="form-control" id="inputPassword" name="password" placeholder="Password" onkeyup='check();'>
                    </div>
                    <label for="inputPassword2" class="col-sm-2 col-form-label pt-4">Confirm Password</label>
                    <div class="col-sm-10 pt-4">
                        <input type="password" class="form-control" id="inputPassword2" name="confirmpassword" placeholder="Confirm Password" onkeyup='check();'>
                    </div>
                </div>
                <div id="message" style="color:${color}"></div>
                <input type="submit" class="create-button" value="Save" />
                </form>
            </div>
        </div>
    </div>

    <%@include file="includes/footer.jsp" %>