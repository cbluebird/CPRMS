<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>重置密码</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/login.css">
    <script>
        document.addEventListener("DOMContentLoaded", function () {
            const urlParams = new URLSearchParams(window.location.search);
            const error = urlParams.get('error');
            const id = urlParams.get('id');

            if (error) {
                alert(error);
            }

            const userForm = document.getElementById("userForm");
            if (id) {
                const idField = document.createElement("input");
                idField.setAttribute("type", "hidden");
                idField.setAttribute("name", "id");
                idField.setAttribute("value", id);
                userForm.appendChild(idField);
            }

            const passwordInput = document.getElementById("password");
            const rePasswordInput = document.getElementById("re_password");

            function validatePassword() {
                const password = passwordInput.value;
                const passwordRegExp = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[^]{8,16}$/;
                if (!passwordRegExp.test(password)) {
                    alert("密码至少由8-16个字符，至少1个大写字母，1个小写字母和1个数字");
                    return false;
                }
                return true;
            }

            function validateRePassword() {
                const password = passwordInput.value;
                const rePassword = rePasswordInput.value;
                if (password !== rePassword) {
                    alert("密码和确认密码不一致");
                    return false;
                }
                return true;
            }

            passwordInput.addEventListener("blur", validatePassword);
            rePasswordInput.addEventListener("blur", validateRePassword);

            document.getElementById("submitButton").addEventListener("click", function (event) {
                if (!validatePassword() || !validateRePassword()) {
                    event.preventDefault();
                } else {
                    userForm.submit();
                }
            });
        });
    </script>
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
    <div class="login-section">
        <form class="login-form" id="userForm" action="${pageContext.request.contextPath}/auth/rePassword"
              method="post">
            <div class="title">
                <h1>重置密码</h1>
            </div>
            <div class="input-field">
                <i class="fas fa-lock"></i>
                <input type="password" id="password" name="password" placeholder="请输入你的新密码" required>
            </div>
            <div class="input-field">
                <i class="fas fa-lock"></i>
                <input type="password" id="re_password" name="re_password" placeholder="重新输入你的密码" required>
            </div>
            <div class="input-field">
                <button type="button" id="submitButton">重置密码</button>
            </div>
        </form>
    </div>
</div>
</body>
</html>
