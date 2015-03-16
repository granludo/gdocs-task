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
<script type="text/javascript"
    src="<c:url value='/resources/grayout.js'/> "></script>
<title><fmt:message key="ltigdocstool"></fmt:message>
</title>
</head>
<body>

    <p><fmt:message key="activity.createHelp"></fmt:message></p>

    <form:form method="post" commandName="newActivity">
        <fieldset>
            <legend>
                <fmt:message key="activity.activityForm"></fmt:message>
            </legend>
            <div class="formrow">
                <label for="activityType"><fmt:message key="activity.activityType"></fmt:message>
                </label>
                <form:errors path="activityType" cssClass="error" />
            </div>
            <div class="radiobuttonrow">
                <form:radiobutton path="activityType" value="personal" /><fmt:message key="activity.personal"></fmt:message>
            </div>
            <div class="radiobuttonrow">
                <form:radiobutton path="activityType" value="collective" /><fmt:message key="activity.collective"></fmt:message>
            </div>
            <div class="formrow submit">
                <input type="submit"
                    value=<fmt:message key="activity.create"></fmt:message>></input>
            </div>
        </fieldset>
    </form:form>

<!--
    <c:if test='${ not empty success}'>
        <div class="success">
            <fmt:message key="activity.createSuccess"></fmt:message>
        </div>
         <p><a href="<c:url value="/activity/viewActivity.html" />"><fmt:message key="activity.backToActivity"></fmt:message></a></p>
    </c:if>
-->

    <div class="centerfooter">
        <img class="footerimage" alt="docs4learning logo" src="<c:url value='/resources/docs4learning.png'/>"></img>
        <fmt:message key="activity.footer"></fmt:message>
    </div>
</body>
</html>