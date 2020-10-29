<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="pages/includes/header.jsp" %>
<div class="container-fluid">
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
            <form action="Login" method="post">
                <div class="row justify-content-center">
                    <div class="col-12">
                        <div class ="mx-auto col-lg-2 col-md-4 col-8 pt-5 mb-2">
                            <input type="text" name="username" placeholder="Username" class="login-container form-control text-center" />
                            <input type="password" name="password" placeholder="Password" class="login-container form-control  text-center" />
                            <div style="color:${color};">${message}</div>
                        </div>
                        <div class="mx-auto col-lg-2 col-md-4 col-8">
                            <button class="yellow-button text-white uppercase" style="padding:10px 50px;" type="submit" >LOG IN</button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </section>
</div>
<%@include file="pages/includes/footer.jsp" %>