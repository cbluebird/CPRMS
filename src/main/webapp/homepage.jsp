<%--
  Created by IntelliJ IDEA.
  User: crk
  Date: 2024/5/28
  Time: 15:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="team.sugarsmile.cprms.model.Admin"%>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.LocalDate" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Homepage</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/homepage.css">
</head>
<body>
<h1>Welcome to the 管理员系统</h1>
<div class="sidebar">
    <a href="${pageContext.request.contextPath}/homepage.jsp">Home</a>
    <jsp:useBean id="admin" scope="session" type="team.sugarsmile.cprms.model.Admin"/>
    <c:if test="${admin.adminType.value eq 2}">
        <a href="${pageContext.request.contextPath}/admin/department/list?pageNum=1&pageSize=10">部门管理</a>
    </c:if>
    <c:if test="${admin.adminType.value eq 2}">
        <a href="${pageContext.request.contextPath}/admin/departmentAdmin/list?pageNum=1&pageSize=10">部门管理员</a>
    </c:if>
</div>
<div class="content">
    <div class="container">
        <%
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.now();
            admin = (Admin) session.getAttribute("admin");
        %>
        <h2>Admin Information</h2>
        <p><strong>Hello! 管理员</strong> <%= admin != null ? admin.getName() : "Unknown" %></p>
        <p><strong>今天是:</strong> <%= date.format(formatter) %></p>
    </div>

    <div class="footer">
        &copy; 2024 Your Company Name. All rights reserved.
    </div>
</div>
</body>
</html>
