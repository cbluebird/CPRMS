<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<html>
<head>
    <title>登录页</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/login.css">
    <%
        String error = (String) session.getAttribute("error");
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
<form action="${pageContext.request.contextPath}/auth/login" method="post" >
    <div class="login-form" id="form" >
        <div class="title">
            <h1>管理员登陆</h1>
        </div>
        <div class="input-field">
            <label for="username">用户名</label>
            <input type="text" id="username" name="username" placeholder="请输入你的用户名" required>
        </div>
        <div class="input-field">
            <label for="password">密&nbsp;&nbsp;&nbsp;码</label>
            <input type="password" id="password" name="password" required>
        </div>
        <div class="input-field">
            <button type="submit">登录</button>
        </div>
    </div>
</form>
</body>
</html>
