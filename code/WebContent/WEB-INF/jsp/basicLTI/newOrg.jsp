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
    <title><fmt:message key="ltigdocstool"></fmt:message></title>
</head>
<body>
    <img class="header" alt="docs4learning logo" src="<c:url value='/resources/docs4learning.png'/>"></img>
    
    <div class="warning"><fmt:message key="launch.unexistantOrganization"></fmt:message></div>
    <p><fmt:message key="launch.unexistantOrganization.todo"></fmt:message></p>
    <div><a target="_blank" href="<c:url value="/organization/newOrganization.html" />"><fmt:message key="org.registerOrg"></fmt:message></a></div>
</body>
</html>