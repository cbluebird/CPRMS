<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<html>
<head>
    <title>登录</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin/login.css">
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
<div class="container">
    <div class="welcome-section">
    </div>
    <div class="login-section">
        <form class="login-form" action="${pageContext.request.contextPath}/auth/login" method="post">
            <h2>管理员登录</h2>
            <p>Please login to your account</p>
            <div class="input-field">
                <i class="fas fa-user"></i>
                <input type="text" name="username" placeholder="用户名" required>
            </div>
            <div class="input-field">
                <i class="fas fa-lock"></i>
                <input type="password" name="password" placeholder="密码" required>
            </div>
            <div class="remember-me">
                <input type="checkbox" id="rememberMe">
                <label for="rememberMe">Remember Me</label>
            </div>
            <button type="submit">登录</button>
        </form>
    </div>
</div>
</body>
</html>
