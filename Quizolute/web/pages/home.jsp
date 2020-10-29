<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="includes/header.jsp" %>
        <div class="container-fluid">
            <header class="box-header">
                <div class="box-logo">
                    <a href="index.html"><img src="/Quizolute/dist/img/logo.png" width="80" alt="Logo"></a>
                </div>
                <a class="box-primary-nav-trigger" href="#0">
                    <span class="box-menu-text">Menu</span><span class="box-menu-icon"></span>
                </a>
            </header>
            <nav>
                <ul class="box-primary-nav">
                    <li class="box-label">Welcome ${user.firstName}</li>
                    <li><a href="/Quizolute/JoinRoom">Join Room</a> <i class="ion-ios-circle-filled color"></i></li>
                    <li><a href="/Quizolute/EditProfile">Edit Profile</a></li>
                    <li><a href="/Quizolute/Logout">Log Out</a></li>
                </ul>
            </nav>
            <section class="box-intro">
                <div class="table-cell">
                    <h1 class="box-headline letters rotate-2">
                        <span class="box-words-wrapper">
                            <b class="is-visible">Quizolute.</b>
                            <b>Online Quiz.</b>
                            <b>&nbsp;Easy exam.</b>
                        </span>
                    </h1>
                    <h5>Click-clack botta bing, botta boom</h5>
                    <form action="JoinRoom" method="post">
                        <div class="row justify-content-center">
                            <div class ="col-lg-4 col-md-8 col-12 pt-5">
                                <input type="text" name="invitationCode" minlength="6" maxlength="6" placeholder="Room Code" class="input-invitation-code" required/>
                                <div style="color:${empty param.color?color:param.color};">${empty param.message?message:param.message}</div>
                            </div>
                            <div class="col-12">
                                <input class="yellow-button mt-4" type="submit" value="Join Room" />
                            </div>
                        </div>
                    </form>
                </div>
            </section>
        </div>

<%@include file="includes/footer.jsp" %>