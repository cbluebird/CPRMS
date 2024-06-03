<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/user/sidebar.css">
</head>
<body>
<div class="sidebar-container">
    <div class="header-container">
        <div class="sidebar-toggle" onclick="openSidebar()">&#9776;</div>
    </div>
    <div class="sidebar" id="sidebar">
        <a href="${pageContext.request.contextPath}/user/appointment/apply?type=public">社会公众进校预约</a>
        <a href="${pageContext.request.contextPath}/user/appointment/apply?type=official">公务进校预约</a>
        <a href="${pageContext.request.contextPath}/user/appointment.jsp">我的预约</a>
    </div>
    <div class="overlay" id="overlay" onclick="closeSidebar()"></div>
</div>
</body>
</html>
<script>
    function openSidebar() {
        document.getElementById("sidebar").style.display = "block";
        document.getElementById("overlay").style.display = "block";
    }

    function closeSidebar() {
        document.getElementById("sidebar").style.display = "none";
        document.getElementById("overlay").style.display = "none";
    }
</script>