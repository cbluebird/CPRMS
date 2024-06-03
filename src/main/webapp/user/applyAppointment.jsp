<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.time.LocalDate" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="sidebar.jsp"/>
<jsp:useBean id="departmentMap" scope="request"
             type="java.util.HashMap<java.lang.Integer,team.sugarsmile.cprms.model.Department>"/>
<html>
<head>
    <title>预约申请</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/user/apply-appointment.css">
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
            session.removeAttribute("error");
        }
    %>
</head>
<body>
<form action="${pageContext.request.contextPath}/user/appointment/${requestScope.type}/add" method="post"
      onsubmit="return validate()" class="container">
    <div class="header">
        ${requestScope.type=="public"?"社会公众":"公务"}进校预约申请
    </div>
    <div class="title">预约信息</div>
    <div class="form-group">
        <label for="date">申请日期</label>
        <input type="date" id="date" value="${LocalDate.now()}" disabled>
    </div>
    <div class="form-group">
        <label for="campus">预约校区</label>
        <select name="campus" id="campus">
            <option value="1">朝晖校区</option>
            <option value="2" selected>屏峰校区</option>
            <option value="3">莫干山校区</option>
        </select>
    </div>
    <div class="form-group">
        <label>预约进校时间</label>
        <div class="date-container">
            <input type="date" name="startTime" id="startTime" value="${LocalDate.now()}" min="${LocalDate.now()}">
            ——
            <input type="date" name="endTime" id="endTime" value="${LocalDate.now()}">
        </div>
    </div>
    <div class="form-group">
        <label for="unit">所在单位</label>
        <input type="text" name="unit" id="unit" required>
    </div>
    <c:if test="${requestScope.type eq 'official'}">
        <div class="form-group">
            <label for="departmentID">访问部门</label>
            <select name="departmentID" id="departmentID">
                <c:forEach var="item" items="${departmentMap}">
                    <option value="${item.key}">${item.value.name}</option>
                </c:forEach>
            </select>
        </div>
        <div class="form-group">
            <label for="receptionist">访问接待人</label>
            <input type="text" name="receptionist" id="receptionist" required>
        </div>
        <div class="form-group">
            <label for="reason">来访事由</label>
            <textarea name="reason" id="reason" rows="3" maxlength="200" required></textarea>
        </div>
    </c:if>
    <div class="title">访客信息</div>
    <div id="visitor-container">
        <div class="visitor-info">
            <div class="form-group">
                <label for="name">姓名</label>
                <input type="text" name="names" id="name" required>
            </div>
            <div class="form-group">
                <label for="idCard">身份证号</label>
                <input type="text" name="idCards" id="idCard" required>
            </div>
            <div class="form-group">
                <label for="phone">手机号</label>
                <input type="text" name="phones" id="phone" required>
            </div>
            <div class="form-group">
                <label for="transportation">交通方式</label>
                <select name="transportations" id="transportation" onchange="toggleLicensePlate(this)">
                    <option value="1">步行</option>
                    <option value="2">自驾</option>
                </select>
            </div>
            <div class="form-group" id="license-plate-container" style="display: none;">
                <label for="licensePlate">车牌号</label>
                <input type="text" name="licensePlates" id="licensePlate">
            </div>
            <button type="button" onclick="deleteVisitor(this)">×</button>
        </div>
    </div>
    <button type="button" class="add-button" onclick="addVisitor()">+</button>
    <input type="submit" class="submit-button" value="提交">
</form>
</body>
</html>
<script>
    function toggleLicensePlate(selectElement) {
        const licensePlateContainer = selectElement.parentElement.nextElementSibling;
        if (selectElement.value === '2') {
            licensePlateContainer.style.display = 'block';
            licensePlateContainer.querySelector('input').required = true;
        } else {
            licensePlateContainer.style.display = 'none';
            licensePlateContainer.querySelector('input').required = false;
        }
    }

    function addVisitor() {
        const visitorContainer = document.getElementById('visitor-container');
        const newVisitorInfo = document.querySelector('.visitor-info').cloneNode(true);
        newVisitorInfo.querySelectorAll('input').forEach(input => input.value = '');
        newVisitorInfo.querySelector('select').value = '1';
        const licensePlateContainer = newVisitorInfo.querySelector('#license-plate-container');
        licensePlateContainer.style.display = 'none';
        licensePlateContainer.querySelector('input').required = false;
        newVisitorInfo.querySelector('button').style.display = 'block';
        visitorContainer.appendChild(newVisitorInfo);
    }

    function deleteVisitor(button) {
        const visitorInfo = button.parentElement;
        visitorInfo.parentElement.removeChild(visitorInfo);
    }

    const startTime = document.getElementById('startTime');
    const endTime = document.getElementById('endTime');

    startTime.addEventListener('change', function () {
        endTime.setAttribute('min', startTime.value);
        if (endTime.value < startTime.value) {
            endTime.value = startTime.value;
        }
    });

    function validate() {
        const visitorInfos = document.querySelectorAll('.visitor-info');
        const idCards = new Set();
        const phones = new Set();

        for (let i = 0; i < visitorInfos.length; i++) {
            const visitorInfo = visitorInfos[i];
            const idCard = visitorInfo.querySelector('[name="idCards"]').value;
            const phone = visitorInfo.querySelector('[name="phones"]').value;
            const transportation = visitorInfo.querySelector('[name="transportations"]').value;
            const licensePlate = visitorInfo.querySelector('[name="licensePlates"]').value;

            if (!isValidIdCard(idCard)) {
                alert('第' + (i + 1) + '位访客的身份证号格式不正确');
                return false;
            }

            if (!isValidPhone(phone)) {
                alert('第' + (i + 1) + '位访客的手机号格式不正确');
                return false;
            }

            if (transportation === '2' && !isValidLicensePlate(licensePlate)) {
                alert('第' + (i + 1) + '位访客的车牌号格式不正确');
                return false;
            }

            if (idCards.has(idCard)) {
                alert('第' + (i + 1) + '位访客的身份证号已经存在');
                return false;
            }

            if (phones.has(phone)) {
                alert('第' + (i + 1) + '位访客的手机号已经存在');
                return false;
            }

            idCards.add(idCard);
            phones.add(phone);
        }

        return true;
    }

    function isValidIdCard(idCard) {
        const idCardRegex = /^[1-9]\d{5}(18|19|20)\d{2}(0[1-9]|1[0-2])(0[1-9]|[1-2]\d|3[0-1])\d{3}(\d|X|x)$/;
        return idCardRegex.test(idCard);
    }

    function isValidPhone(phone) {
        const phoneRegex = /^1[3-9]\d{9}$/;
        return phoneRegex.test(phone);
    }

    function isValidLicensePlate(licensePlate) {
        const licensePlateRegex = /^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{5}$/;
        return licensePlateRegex.test(licensePlate);
    }
</script>