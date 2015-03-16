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
            <c:if test='${personal}'>
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
            <c:if test='${personal}'>
                <c:choose>
                    <c:when test="${success}">
                        <div class="success"><fmt:message key="document.submitSuccess"></fmt:message></div>
                    </c:when>
                    <c:otherwise>    
                        <c:if test='${not submitted}'> 
                            <form:form method="post" commandName="newSubmit">
                                <fieldset>
                                <legend><fmt:message key="document.submitForm"></fmt:message></legend>
                                    <form:hidden cssClass="formitem" path="docId" />
                                    <div class="formrow submit"><input type="submit" value=<fmt:message key="document.submit"></fmt:message> ></input></div>
                                </fieldset>
                            </form:form>
                        </c:if>
                    </c:otherwise>
                </c:choose>
            </c:if>
        </div>
    </div>
    
    
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

    <div class="centerfooter">
        <img class="footerimage" alt="docs4learning logo" src="<c:url value='/resources/docs4learning.png'/>"></img>
        <fmt:message key="activity.footer"></fmt:message>
    </div>
</body>
</html>