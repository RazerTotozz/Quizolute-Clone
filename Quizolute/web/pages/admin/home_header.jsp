<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../includes/header.jsp" %>
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
            <li><a href="/Quizolute/AdminPanel?title=default">Panel</a><i class="ion-ios-circle-filled color"></i></li>
            <li><a href="/Quizolute/EditProfile">Edit Profile</a> </li>
            <li><a href="/Quizolute/Logout">Log Out</a></li>
        </ul>
    </nav>
</div>
<div class="top-bar">
    <h1>admin panel</h1>
</div>
    <div class="container main-container">
        <div class="row">
            
