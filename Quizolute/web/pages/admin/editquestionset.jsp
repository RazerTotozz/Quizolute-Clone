<%-- 
    Document   : createquiz
    Created on : Nov 10, 2019, 1:13:18 AM
    Author     : ThisPz
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="tag" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="home_header.jsp" />
<div class="container pt-3">
    <div class="col-12">
        <h3>EDIT QUESTIONSET</h3>
        <form method="POST">
            <h2 class="form-group">
                <label class="little">1. Name this quiz</label>
                <input type="text" name="title" placeholder="Quiz name" class="text-box" value="${questionSetName}" required />
            </h2>
            <div class="form-group">
                <label class="little">2. Set privacy</label>
                <select class="form-control" id="exampleFormControlSelect1" name="privacy">
                    <option ${(data.isPublic)? "" : "selected"}>Public</option>
                    <option ${(data.isPublic)? "selected" : ""}>Private</option>
                </select>
            </div>
            <hr/>
            <input hidden id="questions-count" name="questions-count" value="${questionCount}"/>

            <div class="form-group" id="question-content">
                <tag:forEach items="${questionSetList}" var="i" varStatus="v">
                    <div class="card mt-2" id="question-${v.count}" >
                        <div class="card-header">Question #${v.count}</div>
                        <div class="card-body">
                            <h5 class="card-title"> 
                                <textarea placeholder="Write your question here" class="q1" name="question${v.count}-name" id="question${v.count}-name" required>${i.getName()}</textarea> 
                            </h5>
                            <hr/>
                            <div class="card-text">
                                <tag:forEach items="${i.getChoicesList()}" var="j" varStatus="b">
                                    <div class="custom-control custom-checkbox col-12" id="question${v.count}-choice${b.count}">
                                        <div class="row pb-3">
                                            <div class="col-md-2 col-12">
                                                <label class="tgl" for="question${v.count}-isCorrect${b.count}">
                                                    <tag:choose>
                                                        <tag:when test='${j.getIsCorrect()}'>
                                                            <input type="checkbox" id="question${v.count}-isCorrect${b.count}" name="question${v.count}-isCorrect${b.count}" checked>
                                                            <span data-on="Correct" data-off="Wrong">   </span>
                                                        </tag:when>
                                                        <tag:otherwise>
                                                            <input type="checkbox" id="question${v.count}-isCorrect${b.count}" name="question${v.count}-isCorrect${b.count}" >
                                                            <span data-on="Correct" data-off="Wrong">   </span>
                                                        </tag:otherwise>
                                                    </tag:choose>
                                                </label>
                                            </div>
                                            <div class="col-md-10 col-12">
                                                <input class="form-control" name="question${v.count}-choice${b.count}" type="text" placeholder="Choice ${b.count}" value="${j.getName()}" required/>
                                            </div>
                                        </div>
                                    </div>
                                </tag:forEach>
                            </div>
                            <input hidden id="question${v.count}-count" name="question${v.count}-count" value="${i.getChoicesList().size()}"/>
                            <hr/>
                            <label class="little">Overall Score</label>
                            <input type="number" class="text-box pull-right" id="question${v.count}-maxScore" name="question${v.count}-maxScore" value="${i.getMaxScore()}"/>
                        </div>
                    </div>    
                </tag:forEach>
            </div>
            <div class="pt-3 pb-3">
                <div class="text-center" >
                    <button class="create-button m-auto" type="submit" >Save</button>
                </div>
            </div>
        </form>
    </div>
</div>
<jsp:include page="home_footer.jsp" />