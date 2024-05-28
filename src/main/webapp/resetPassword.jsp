<%--
  Created by IntelliJ IDEA.
  User: crk
  Date: 2024/5/23
  Time: 21:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>重置密码</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
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
</head>
<body>
<form id="userForm" action="${pageContext.request.contextPath}/auth/rePassword" method="post">
    <div class="login-form" id="form">
        <div class="title">
            <h1>重置密码</h1>
        </div>
        <div class="input-field">
            <label for="password">新密码</label>
            <input type="password" id="password" name="password" placeholder="请输入你的新密码" required>
        </div>
        <div class="input-field">
            <label for="re_password">重复输入</label>
            <input type="password" id="re_password" name="re_password" required>
        </div>
        <div class="input-field">
            <button type="button" id="submitButton">重置密码</button>
        </div>
    </div>
</form>
</body>
</html>
