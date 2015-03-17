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
    <script type="text/javascript" src="<c:url value='/resources/jquery-1.4.4.min.js'/> "></script>
    <script type="text/javascript" src="<c:url value='/resources/grayout.js'/> "></script>
    <title><fmt:message key="ltigdocstool"></fmt:message></title>
</head>
<body>
        <img class="header" alt="docs4learning logo" src="<c:url value='/resources/docs4learning.png'/>"></img>
    
    <p><fmt:message key="approval.message"></fmt:message></p>
    
    <div id="wait" class="hiddenwait"><img src="<c:url value='/resources/wait_spinning.gif'/>" /></div>
    
    <p><a target="_blank" href="${approvalLink}" onclick="grayOut(true);checkToken('${userId}');"><fmt:message key="approval.link"></fmt:message></a></p>
    
</body>
</html>