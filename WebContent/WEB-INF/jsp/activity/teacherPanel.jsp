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
    <div class="navigation">
        <c:out value="${activity}" />
        <c:out value=" ▶ " />
        <fmt:message key="activity.panelHelp"></fmt:message>
    </div>
    
    <c:choose>
        <c:when test="${empty documents}">
            <p><strong><fmt:message key="submission.emptyList"></fmt:message></strong></p>
        </c:when>
        <c:otherwise>
            <c:choose>
                <c:when test="${personal}">
                    <table id="submissions" cellpadding="0">
                        <thead>
                            <tr>
                                <th><fmt:message key="submission.docTitle"></fmt:message></th>
                                <th><fmt:message key="submission.link"></fmt:message></th>
                                <th><fmt:message key="submission.grade"></fmt:message></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="doc" items="${documents}" varStatus="rowCounter">
                                <c:choose>
                                    <c:when test="${rowCounter.count % 2 == 0}">
                                        <c:set var="rowStyle" scope="page" value="odd"/>
                                    </c:when>
                                <c:otherwise>
                                    <c:set var="rowStyle" scope="page" value="even"/>
                                </c:otherwise>
                                </c:choose>
                                <tr class="${rowStyle}">
                                    <td class="doctitle"><c:out value="${doc.title}"></c:out></td>
                                    <td><a href="${doc.link}" target="_blank"><fmt:message key="submission.link"></fmt:message></a></td>
                                    <c:choose>
                                        <c:when test="${doc.submitted}">
                                            <c:choose>
                                                <c:when test="${doc.graded}">
                                                    <td><a class="update" href="<c:url value='/activity/gradePanel.html'>
                                                                    <c:param name="docId" value="${doc.id}"/>
                                                                    <c:param name="gradeType" value="individual"/>
                                                                 </c:url>
                                                                ">
                                                            <fmt:message key="submission.gradeUpdate"></fmt:message>
                                                    </a></td>
                                                </c:when>
                                                <c:otherwise>
                                                    <td><a href="<c:url value='/activity/gradePanel.html'>
                                                                    <c:param name="docId" value="${doc.id}"/>
                                                                    <c:param name="gradeType" value="individual"/>
                                                                 </c:url>
                                                                ">
                                                            <fmt:message key="submission.grade"></fmt:message>
                                                    </a></td>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:when>
                                        <c:otherwise>
                                                    <td><fmt:message key="submission.notSubmitted"></fmt:message></td>
                                                </c:otherwise>
                                    </c:choose>
                                </tr>
                            </c:forEach>
                      </tbody>
                    </table>
                </c:when>
                <c:otherwise>
                    <div class="notification">
                        <p>
                            <fmt:message key="document.common"></fmt:message>
                        </p>
                    </div>
                    <div class="docinfo">
                        <p>
                            <strong class="leftalign"><fmt:message key="activity.name"></fmt:message></strong>
                            <c:out value="${activity}" />
                        </p>
                        <p>
                            <strong class="leftalign"><fmt:message key="document.title"></fmt:message></strong>
                            <a href="<c:out value="${documents[0].link}" />" target="_blank"><c:out value="${documents[0].title}" /></a>
                        </p>
                        <p>
                            <strong class="leftalign"><fmt:message key="document.timeCreated"></fmt:message></strong>
                            <c:out value="${documents[0].timeCreated}" />
                        </p>
                        <p>
                            <strong class="leftalign"><fmt:message key="document.timeModified"></fmt:message></strong>
                            <c:out value="${documents[0].timeModified}" />
                        </p>
                    </div>
                    <table id="submissions" cellpadding="0">
                        <thead>
                            <tr>
                                <th colspan="2" class="tableHeaderLink"><input class="expand" id='sign' type='button' value=' [+] ' onclick="expand_table();"><fmt:message key="activity.userList"></fmt:message></th>
                            </tr>
                        </thead>
                        <tbody id="expandable" style="display:none">
                            <c:forEach var="doc" items="${documents}" varStatus="rowCounter">
                                <c:if test="${rowCounter.last}">
                                    <c:set var="totalUsers" value="${rowCounter.count}" scope="page"></c:set>
                                </c:if>
                            </c:forEach>
                            <tr>
                                <td class="even">"${documents[0].userId}"</td>
                                <c:choose>
                                        <c:when test="${documents[0].graded}">
                                            <td class="even"><a class="update" href="<c:url value='/activity/gradePanel.html'>
                                                            <c:param name="docId" value="${documents[0].id}"/>
                                                            <c:param name="gradeType" value="individual"/>
                                                         </c:url>
                                                        ">
                                                    <fmt:message key="submission.gradeUpdate"></fmt:message>
                                            </a></td>
                                        </c:when>
                                        <c:otherwise>
                                            <td class="even"><a href="<c:url value='/activity/gradePanel.html'>
                                                            <c:param name="docId" value="${documents[0].id}"/>
                                                            <c:param name="gradeType" value="individual"/>
                                                         </c:url>
                                                        ">
                                                    <fmt:message key="submission.grade"></fmt:message>
                                            </a></td>
                                        </c:otherwise>
                                    </c:choose>
                                    
                                <c:forEach var="doc" items="${documents}" varStatus="rowCounter">
                                    <c:choose>
                                        <c:when test="${rowCounter.count % 2 == 0}">
                                            <c:set var="rowStyle" scope="page" value="odd"/>
                                        </c:when>
                                    <c:otherwise>
                                        <c:set var="rowStyle" scope="page" value="even"/>
                                    </c:otherwise>
                                    </c:choose>
                                    <c:if test="${rowCounter.count > 1}">
                                        <tr class="${rowStyle}">
                                            <td>"${doc.userId}"</td>
                                            <c:choose>
                                                <c:when test="${doc.graded}">
                                                    <td><a class="update" href="<c:url value='/activity/gradePanel.html'>
                                                                    <c:param name="docId" value="${doc.id}"/>
                                                                    <c:param name="gradeType" value="individual"/>
                                                                 </c:url>
                                                                ">
                                                            <fmt:message key="submission.gradeUpdate"></fmt:message>
                                                    </a></td>
                                                </c:when>
                                                <c:otherwise>
                                                    <td><a href="<c:url value='/activity/gradePanel.html'>
                                                                    <c:param name="docId" value="${doc.id}"/>
                                                                    <c:param name="gradeType" value="individual"/>
                                                                 </c:url>
                                                                ">
                                                            <fmt:message key="submission.grade"></fmt:message>
                                                    </a></td>
                                                </c:otherwise>
                                            </c:choose>
                                        </tr>
                                    </c:if>
                                </c:forEach>
                            </tr>
                        </tbody>
                    </table>
                    
                    <div class="right">
                        <a href="<c:url value='/activity/gradePanel.html'><c:param name="docId" value="${documents[0].id}"/><c:param name="gradeType" value="common"/></c:url>"><fmt:message key="grade.group"></fmt:message></a>
                    </div>
                </c:otherwise>
            </c:choose>
        </c:otherwise>
    </c:choose>
    
    <div class="right">
        <a href="<c:url value="/activity/defineProfessors.html" />"><fmt:message key="activity.manageProfessors"></fmt:message></a>
    </div>
    
<script type="text/javascript">

// set an event handler to handle when the expander link is clicked
function expand_table() {
    // get a reference to the expandable div
    var expandable = document.getElementById('expandable');
    // display it if it isn't visible, or hide it 
    if (expandable.style.display == 'none') {
        expandable.style.display = 'table-row-group';
        document.getElementById('sign').value = " [-] ";
    } else {
        expandable.style.display = 'none';
        document.getElementById('sign').value = " [+] ";
    }
};
</script>

    <div class="centerfooter">
        <img class="footerimage" alt="docs4learning logo" src="<c:url value='/resources/docs4learning.png'/>"></img>
        <fmt:message key="activity.footer"></fmt:message>
    </div>
</body>

</html>