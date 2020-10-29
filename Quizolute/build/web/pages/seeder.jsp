<%-- 
    Document   : Seeder
    Created on : Oct 23, 2019, 4:01:05 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Quizolute - Migration</title>
    </head>
    <body>
        <h1>Migration Page</h1>
        ${resultMessage}
        <hr>
        <form action="Seeder" method="POST">
            <input type="hidden" name="run" value="1">
            <p>run seeder will run only seeder</p>
            <p style="color:red">*use when there is no data in db</p>
            <input type="submit" value="Run Seeder">
        </form>
        <hr>
        <form action="Seeder" method="POST">
            <input type="hidden" name="refresh" value="1">
            <p>refresh will delete all data in db then do a seeder.</p>
            <input type="submit" value="Refresh">
        </form>
        <hr>
        <form action="Seeder" method="POST">
            <input type="hidden" name="deletescore" value="1">
            <p>delete all score data in DB.</p>
            <p style="color:red">*this is not refresh just delete</p>
            <input type="submit" value="Delete Score">
        </form>
        <form action="Seeder" method="POST">
            <input type="hidden" name="createscore" value="1">
            <p>create fake score for all exist rooms</p>
            <input type="submit" value="Create Fake Score">
        </form>
    </body>
</html>
