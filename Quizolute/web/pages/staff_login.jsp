<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="includes/header.jsp" %>
<style>
    body {
        background-color: black;
    }
</style>
<div class="container-fluid">
    <section class="box-intro">
        <div class="table-cell">
            <h1 class="box-headline letters rotate-2">
                <span class="box-words-wrapper text-white">
                    <b class="is-visible">Quizolute.</b>
                    <b>Online Quiz.</b>
                    <b>&nbsp;Easy exam.</b>
                </span>
            </h1>
            <h5>Welcome staff!</h5>
            <form action="StaffLogin" method="post">
                <div class="row justify-content-center">
                    <div class="col-12">
                        <div class ="mx-auto col-lg-2 col-md-4 col-8 pt-5 mb-2">
                            <input type="text" name="username" placeholder="Username" class="form-control mb-2 text-center" />
                            <input type="password" name="password" placeholder="Password" class="form-control  text-center" />
                            <div style="color:${color};">${message}</div>
                        </div>
                        <div class="mx-auto col-lg-2 col-md-4 col-8 mb-2">
                            <button class="btn btn-warning text-white uppercase" style="font-size: 1.2em;" type="submit" >log in</button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </section>
</div>
<%@include file="includes/footer.jsp" %>