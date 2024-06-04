<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="team.sugarsmile.cprms.model.Admin" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.LocalDate" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Homepage</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin/home.css">
    <%
        String error = (String) request.getAttribute("error");
        if (error != null) {
    %>
    <script>
        window.onload = function () {
            alert("<%= error %>");
        };
    </script>
    <%
        }
    %>
</head>
<body>
<jsp:useBean id="admin" scope="session" type="team.sugarsmile.cprms.model.Admin"/>
<jsp:include page="sidebar.jsp"/>
<div class="content">
    <div class="container">
        <%
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.now();
            admin = (Admin) session.getAttribute("admin");
        %>
        <h1>Welcome to the 管理员系统</h1>
        <h2><strong>Hello! 管理员</strong> <%= admin != null ? admin.getName() : "Unknown" %>
        </h2>
        <h3><strong>今天是:</strong> <%= date.format(formatter) %>
        </h3>
    </div>
</div>
</body>
</html>
