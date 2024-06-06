<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.LocalDate" %>
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
        <span id="currentTime">
            ${LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))}
        </span>
    </div>
    <div class="passcode">
        <img src="${pageContext.request.contextPath}/user/passcode/gen?type=<%= request.getParameter("type") %>&id=<%= request.getParameter("id") %>"
             alt="passcode">
    </div>
    <% if (LocalDate.now().isBefore(passcode.getAppointmentTime())) { %>
    <div class="validTime">
        有效时间始: ${passcode.appointmentTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))}
    </div>
    <div class="notice">
        当前时间不在有效预约时间内, 暂不可进校。
    </div>
    <% } else if (LocalDate.now().isAfter(passcode.getAppointmentTime())) { %>
    <div class="validTime">
        有效时间至: ${passcode.appointmentTime.atTime(23,59,59).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))}
    </div>
    <div class="notice">
        该通行码已过期, 请重新申请。
    </div>
    <% } else { %>
    <div class="validTime">
        有效时间至: ${passcode.appointmentTime.atTime(23,59,59).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))}
    </div>
    <div class="notice">
        凭此码或身份证进校, 并服从学校相关管理规定。
    </div>
    <% } %>
</div>
</body>
</html>
<script>
    setInterval(() => {
        const now = new Date();
        const year = now.getFullYear();
        let month = now.getMonth();
        month = month + 1;
        const day = now.getDate();
        const hour = now.getHours();
        const minute = now.getMinutes();
        const second = now.getSeconds();
        document.getElementById("currentTime").innerText = year + "-" + frontZero(month) + "-" + frontZero(day) + " " + frontZero(hour) + ":" + frontZero(minute) + ":" + frontZero(second);
    }, 1000)

    function frontZero(value) {
        return (value < 10 ? "0" : "") + value;
    }
</script>