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
        <fmt:message key="activity.manageProfessors"></fmt:message>
    </div>
    
    <c:if test='${ not empty newActivity}'>
        <div><fmt:message key="professor.addFirst"></fmt:message></div>
    </c:if>
    
    <form:form method="post" commandName="newProfessor">
        <fieldset>
            <legend><fmt:message key="activity.addNewProfessor"></fmt:message></legend>
            <div class="formrow">
                <label for="name"><fmt:message key="professor.name"></fmt:message></label>
                <form:input cssClass="formitem" path="name" />
                <form:errors path="name" cssClass="error" />
            </div>
            <div class="formrow">
                <label for="lastname"><fmt:message key="professor.lastname"></fmt:message></label>
                <form:input cssClass="formitem" path="lastname" />
                <form:errors path="lastname" cssClass="error"/>
            </div>
            <div class="formrow">
                <label for="email"><fmt:message key="professor.email"></fmt:message></label>
                <form:input cssClass="formitem" path="email" />
                <form:errors path="email" cssClass="error"/>
            </div>

            <div class="formrow submit"><input type="submit" value=<fmt:message key="professor.add"></fmt:message> ></input></div>
        </fieldset>
    </form:form>
    
    <c:if test='${ not empty success}'>
        <div class="success">
            <fmt:message key="professor.addSuccess"></fmt:message>
        </div>
    </c:if>
    <c:if test='${ not empty duplicate}'> 
        <div class="warning">
            <fmt:message key="professor.duplicateError"></fmt:message>
        </div>
    </c:if>
    <c:choose>
        <c:when test="${empty professors}">
            <p><strong><fmt:message key="professor.emptyList"></fmt:message></strong></p>
        </c:when>
        <c:otherwise>
            <table id="registeredProfessors" cellpadding="0">
                <thead>
                    <tr>
                        <th><fmt:message key="professor.regmail"></fmt:message></th>
                    </tr>
                </thead>
                
                <tbody>
                    <c:forEach var="professor" items="${professors}" varStatus="rowCounter">
                        <c:choose>
                            <c:when test="${rowCounter.count % 2 == 0}">
                                <c:set var="rowStyle" scope="page" value="odd"/>
                            </c:when>
                        <c:otherwise>
                            <c:set var="rowStyle" scope="page" value="even"/>
                        </c:otherwise>
                        </c:choose>
                            <tr class="${rowStyle}">
                                <td class="professoremail"><c:out value="${professor.email}"></c:out></td>
                                <td class="removebutton">
                                    <a href="<c:url value="/activity/removeProfessor.html?professorEmail=" />${professor.email}"><fmt:message key="professor.remove"></fmt:message></a>
                                </td>
                            </tr>
                    </c:forEach>
                </tbody>
            </table>
            <p><a href="<c:url value="/activity/viewActivity.html" />"><fmt:message key="activity.goToActivity"></fmt:message></a></p>
        </c:otherwise>
    </c:choose>

    <div class="centerfooter">
        <img class="footerimage" alt="docs4learning logo" src="<c:url value='/resources/docs4learning.png'/>"></img>
        <fmt:message key="activity.footer"></fmt:message>
    </div>
</body>
</html>