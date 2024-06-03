<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="sidebar.jsp"/>
<html>
<head>
    <title>我的预约</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/user/appointment.css">
    <%
        String name = (String) request.getSession().getAttribute("name");
        String idCard = (String) request.getSession().getAttribute("idCard");
        String phone = (String) request.getSession().getAttribute("phone");
        if (name != null && idCard != null && phone != null) {
            response.sendRedirect(request.getContextPath() + "/user/appointment/public/list?pageNum=1&pageSize=10");
            return;
        }

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
<form action="${pageContext.request.contextPath}/user/appointment" method="post"
      onsubmit="return validate()" class="card">
    <h2>我的预约</h2>
    <p>输入以下信息, 查询历史预约记录</p>
    <div class="form-group">
        <label for="name">姓名</label>
        <input type="text" name="name" id="name" required>
    </div>
    <div class="form-group">
        <label for="idCard">身份证号</label>
        <input type="text" name="idCard" id="idCard" required>
    </div>
    <div class="form-group">
        <label for="phone">手机号</label>
        <input type="text" id="phone" name="phone" required>
    </div>
    <input type="submit" value="提交">
</form>
</body>
</html>
<script>
    function validate() {
        const idCard = document.getElementById("idCard").value;
        const phone = document.getElementById("phone").value;

        if (!isValidIdCard(idCard)) {
            alert('身份证号格式不正确');
            return false;
        }

        if (!isValidPhone(phone)) {
            alert('手机号格式不正确');
            return false;
        }
    }

    function isValidIdCard(idCard) {
        const idCardRegex = /^[1-9]\d{5}(18|19|20)\d{2}(0[1-9]|1[0-2])(0[1-9]|[1-2]\d|3[0-1])\d{3}(\d|X|x)$/;
        return idCardRegex.test(idCard);
    }

    function isValidPhone(phone) {
        const phoneRegex = /^1[3-9]\d{9}$/;
        return phoneRegex.test(phone);
    }
</script>
