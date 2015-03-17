<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" href="<c:url value='/resources/styles.css'/>"
    type="text/css" />
<script type="text/javascript"
    src="<c:url value='/resources/jquery-1.4.4.min.js'/> "></script>
<title><fmt:message key="ltigdocstool"></fmt:message>
</title>
</head>
<body>
    
    <div class="navigation">
        <a href="<c:url value="/activity/viewActivity.html" />"><c:out value="${activity}" /></a>
        <c:out value=" ▶ " />
        <fmt:message key="activity.grading"></fmt:message>
    </div>
    
    <p><fmt:message key="grade.panelHelp"></fmt:message></p>
    
    <div class="top">
        <div class="docinfo">
            <p>
                <strong class="leftalign"><fmt:message key="activity.name"></fmt:message></strong>
                <c:out value="${activity}" />
            </p>
            <p>
                <strong class="leftalign"><fmt:message key="document.title"></fmt:message></strong>
                <a href="<c:out value="${doc.link}" />" target="_blank"><c:out value="${doc.title}" /></a>
            </p>
            <c:if test="${gradeType=='individual'}">
                <p>
                    <strong class="leftalign"><fmt:message key="document.author"></fmt:message></strong>
                    <c:out value="${doc.userId}" />
                </p>
            </c:if>
            <p>
                <strong class="leftalign"><fmt:message key="document.timeCreated"></fmt:message></strong>
                <c:out value="${doc.timeCreated}" />
            </p>
            <p>
                <strong class="leftalign"><fmt:message key="document.timeModified"></fmt:message></strong>
                <c:out value="${doc.timeModified}" />
            </p>
        </div>
        
        <div class="toolbar">
            <a href="#"><img class="icon" src="<c:url value='/resources/embed.png'/>" id="show" alt="embed"></img></a>
            <a href="<c:out value="${doc.link}" />" target="_blank"><img class="icon" src="<c:url value='/resources/link.png'/>" alt="open in new window"></img></a>
            <a href="https://docs.google.com/uc?export=downloadrevision=true&id=<c:out value="${doc.pdfId}" />" target="_blank"><img class="icon" src="<c:url value='/resources/download.png'/>" alt="download pdf"></img></a>
            <a href="https://docs.google.com/leaf?id=<c:out value="${doc.pdfId}" />" target="_blank"><img class="icon" src="<c:url value='/resources/pdf.png'/>" alt="view pdf"></img></a>
            <a href="#"><img class="icon" src="<c:url value='/resources/history.png'/>" alt="history"></img></a>
        
            <c:if test="${gradeType=='individual'}">
                <form:form method="post" commandName="newGrade" cssClass="gradeform">
                    <fieldset>
                        <legend>
                            <fmt:message key="grade.gradeForm"></fmt:message>
                        </legend>
                        <div class="formrow">
                            <label for="grade"><fmt:message key="grade.grade"></fmt:message></label>
                            <form:input cssClass="formitem" path="grade" size="4"/>
                            <form:errors path="grade" cssClass="error" />
                        </div>
                        <form:hidden path="userId"/>
                        <form:hidden path="gradeType" />
                        <div class="formrow submit">
                            <c:if test='${ not empty update}'>
                                <input type="submit"
                                    value="<fmt:message key="grade.update"></fmt:message>"></input>
                                <input type="submit"
                                    value="<fmt:message key="grade.delete"></fmt:message>" name="delete" class="deletegrade"></input>
                            </c:if>
                            <c:if test='${ (not empty delete) or ((empty success) and (empty update))}'>
                                <input type="submit"
                                    value=<fmt:message key="grade.submit"></fmt:message>></input>
                            </c:if>
                        </div>
                    </fieldset>
                </form:form>
            </c:if>
            <c:if test="${gradeType=='common'}">
                <form:form method="post" commandName="newGrade" cssClass="gradeform">
                    <fieldset>
                        <legend>
                            <fmt:message key="grade.gradeForm"></fmt:message>
                        </legend>
                        <div class="formrow">
                            <label for="grade"><fmt:message key="grade.grade"></fmt:message></label>
                            <form:input cssClass="formitem" path="grade" size="4"/>
                            <form:errors path="grade" cssClass="error" />
                        </div>
                        <form:hidden path="gradeType" />
                        <div class="formrow submit">
                            <c:if test='${ not empty update}'>
                                <input type="submit"
                                    value="<fmt:message key="grade.update"></fmt:message>" name="submitCommon"></input>
                                <input type="submit"
                                    value="<fmt:message key="grade.delete"></fmt:message>" name="deleteCommon" class="deletegrade"></input>
                            </c:if>
                            <c:if test='${ (not empty delete) or ((empty success) and (empty update))}'>
                                <input type="submit"
                                    value="<fmt:message key="grade.submit"></fmt:message>" name="submitCommon"></input>
                            </c:if>
                        </div>
                    </fieldset>
                </form:form>
            </c:if>
        </div>
    </div>
    
    <c:if test='${ (not empty success) and (empty delete)}'>
        <div class="success">
            <fmt:message key="grade.submitSuccess"></fmt:message>
        </div>
    </c:if>
    <c:if test='${ not empty delete}'>
        <div class="success">
            <fmt:message key="grade.deleteSuccess"></fmt:message>
        </div>
    </c:if>
    <c:if test='${ not empty warning}'> 
        <div class="warning">
            <fmt:message key="grade.submitWarning"></fmt:message>
        </div>
    </c:if>

    <div id="gdocsframe" style="display:none;"><iframe src="<c:out value="${doc.link}" />"></iframe></div>
    
    <script type="text/javascript">
    $("#show").click(function () {
        var src = $("#show").attr('src');
        if ($("#show").hasClass('hideembed')) {
            $("#show").removeClass('hideembed');
            $("#show").attr('src', src.replace('hide', 'embed'));
            $("#gdocsframe").hide("slow");
        } else {
            $("#show").addClass('hideembed');
            $("#show").attr('src', src.replace('embed', 'hide'));
            $("#gdocsframe").show("slow");
        }
    });
    </script>

    <div class="footer">
        <p><a href="<c:url value="/activity/viewActivity.html" />"><fmt:message key="activity.backToActivity"></fmt:message></a></p>
    </div>

    <div class="centerfooter">
        <img class="footerimage" alt="docs4learning logo" src="<c:url value='/resources/docs4learning.png'/>"></img>
        <fmt:message key="activity.footer"></fmt:message>
    </div>
</body>
</html>