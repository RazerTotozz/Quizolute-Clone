<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="home_header.jsp" />
<jsp:include page="home_menu.jsp" />
<div class="col-lg-8 col-md-12">
    <!--${settitle}-->
    <a class="create-button" href="CreateQuestionSet">Create Quiz</a>
    <c:forEach items="${questionSetList}" var="question">
        <div class="pt-4">
            <div class="card">
                <div class="card-header">
                    Question no. ${question.id} : ${question.name}
                </div>
                <div class="card-body">
                    <a href="/Quizolute/AdminPanel/EditQuestionSet?id=${question.id}" class="blue-button" style="color:white;">Edit Question</a>
                </div>
            </div>
        </div>
    </c:forEach>
</div>
<jsp:include page="home_footer.jsp" />

