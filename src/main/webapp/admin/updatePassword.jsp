<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="sidebar.jsp"/>
<html>
<head>
    <title>更新密码</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin/update-password.css">
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
    <form class="form" action="${pageContext.request.contextPath}/admin/password/update"
          method="post" onsubmit="return validate()">
        <h2>更新密码</h2>
        <div class="input-field">
            <i class="fas fa-lock"></i>
            <input type="password" id="password" name="password" placeholder="请输入你的新密码" required>
        </div>
        <div class="input-field">
            <i class="fas fa-lock"></i>
            <input type="password" id="rePassword" name="rePassword" placeholder="请确认你的新密码" required>
        </div>
        <button type="submit">确定</button>
    </form>
</div>

<script>
    function validate() {
        const password = document.getElementById("password").value;
        const rePassword = document.getElementById("rePassword").value;

        if (!isValidPassword(password)) {
            alert("密码长度需8-16位, 且包含数字、大小写字母和特殊字符");
            return false;
        }

        if (password !== rePassword) {
            alert("密码不一致");
            return false;
        }

        return true;
    }

    function isValidPassword(password) {
        const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^a-zA-Z0-9]).{8,16}$/;
        return passwordRegex.test(password);
    }
</script>
</body>
</html>
