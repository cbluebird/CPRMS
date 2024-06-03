<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="passcode" type="team.sugarsmile.cprms.dto.PasscodeDto" scope="request"/>
<jsp:include page="sidebar.jsp"/>
<html>
<head>
    <title>通行码</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/user/passcode.css">
</head>
<body>
<div class="container">
    <div class="name">
        ${passcode.name}通行码
    </div>
    <div class="currentTime">
        <%= LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) %>
    </div>
    <div class="passcode">
        <img src="${pageContext.request.contextPath}/user/passcode/gen?type=<%= request.getParameter("type") %>&id=<%= request.getParameter("id") %>"
             alt="passcode">
    </div>
    <% if (LocalDateTime.now().isBefore(passcode.getStartTime())) {%>
    <div class="validTime">
        有效时间始: <%= passcode.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) %>
    </div>
    <% } else {%>
    <div class="validTime">
        有效时间至: <%= passcode.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) %>
    </div>
    <% } %>
    <div class="notice">
        凭此码或身份证进校，并服从学校相关管理规定。
    </div>
</div>
</body>
</html>