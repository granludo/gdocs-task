<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <link rel="stylesheet" href="<c:url value='/resources/styles.css'/>" type="text/css" />
  <title><fmt:message key="org.registerOrg"></fmt:message></title>
</head>
<body>

    <img class="header" alt="docs4learning logo" src="<c:url value='/resources/docs4learning.png'/>"></img>
<h1><fmt:message key="org.registerOrg"></fmt:message></h1>

<p><fmt:message key="org.registerOrgHelp"></fmt:message></p>

<form:form method="post" commandName="newOrg">
    <fieldset>
    <legend><fmt:message key="org.registerForm"></fmt:message></legend>
    <div class="formrow">
        <label for="orgId"><fmt:message key="org.orgId"></fmt:message></label>
        <form:input cssClass="formitem formurl" path="orgId" />
        <form:errors path="orgId" cssClass="error" />
    </div>
    <div class="formrow">
        <label for="password"><fmt:message key="org.password"></fmt:message></label>
        <form:password cssClass="formitem" path="password" />
        <form:errors path="password" cssClass="error"/>
    </div>
    <div class="formrow">
        <label for="passwordConfirm"><fmt:message key="org.passwordConfirm"></fmt:message></label>
        <form:password cssClass="formitem" path="passwordConfirm" />
        <form:errors path="passwordConfirm" cssClass="error"/>
    </div>
    
    <div class="formrow submit"><input type="submit" value=<fmt:message key="org.register"></fmt:message> ></input></div>
    </fieldset>
</form:form>

<c:if test='${ not empty success}'> 
    <div class="success"><fmt:message key="org.newOrgSuccess"></fmt:message></div>
</c:if>

<a href="<c:url value='/'/>"><fmt:message key="backHome"></fmt:message></a>
</body>
</html>