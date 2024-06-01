<%@ page import="team.sugarsmile.cprms.model.OfficialAppointment" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="pagination" scope="request" type="team.sugarsmile.cprms.dto.PaginationDto<team.sugarsmile.cprms.model.PublicAppointment>"/>
<jsp:useBean id="departmentMap" scope="request" type="java.util.HashMap"/>
<fmt:formatNumber var="totalPage" scope="request" type="number" value="${pagination.total == 0 ? 1 : (pagination.total - 1) / pagination.pageSize + 1}" maxFractionDigits="0"/>
<html>
<head>
    <title>公务预约管理</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/appointment.css">
</head>
<body>
<jsp:useBean id="admin" scope="session" type="team.sugarsmile.cprms.model.Admin"/>
<jsp:include page="sidebar.jsp" />
<div class="content">
    <div class="container">
        <div class="header">
            <h2>社会预约管理</h2>
        </div>
        <div class="search-section">
            <form action="${pageContext.request.contextPath}/admin/appointment/public/query?pageNum=1&pageSize=10" method="get">
                <div class="row">
                    <label for="applyDate">申请日期：</label>
                    <input type="date" id="applyDate" name="applyDate">

                    <label for="appointmentDate">预约日期：</label>
                    <input type="date" id="appointmentDate" name="appointmentDate">

                    <label for="campus">预约校区：</label>
                    <select id="campus" name="campus">
                        <option value="">全部</option>
                        <option value="1">朝晖校区</option>
                        <option value="2">屏峰校区</option>
                        <option value="3">莫干山校区</option>
                    </select>

                    <label for="unit">所在单位：</label>
                    <input type="text" id="unit" name="unit">
                </div>
                <div class="row">
                    <label for="name">预约人姓名：</label>
                    <input type="text" id="name" name="name">

                    <label for="idCard">身份证号：</label>
                    <input type="text" id="idCard" name="idCard">

                </div>
                <div class="row">
                    <button type="submit" class="btn btn-primary">查询</button>
                    <button type="button" class="btn btn-secondary" onclick="window.location.href='appointmentStatistics.jsp'">统计</button>
                </div>
            </form>
        </div>
        <div class="result-section">
            <table>
                <thead>
                <tr>
                    <th>申请日期</th>
                    <th>预约校区</th>
                    <th>预约进校时间</th>
                    <th>所在单位</th>
                    <th>姓名</th>
                    <th>查看详情</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="appointment" items="${pagination.list}">
                    <tr>
                        <td><fmt:formatDate value="${appointment.createTime}" pattern="yyyy-MM-dd" /></td>
                        <td>
                            <c:choose>
                                <c:when test="${appointment.campus.value == 1}">朝晖</c:when>
                                <c:when test="${appointment.campus.value == 2}">屏峰</c:when>
                                <c:when test="${appointment.campus.value == 3}">莫干山</c:when>
                            </c:choose>
                        </td>
                        <td><fmt:formatDate value="${appointment.startTime}" pattern="yyyy-MM-dd" /> - <fmt:formatDate value="${appointment.endTime}" pattern="yyyy-MM-dd" /></td>
                        <td>${appointment.unit}</td>
                        <td>${appointment.name}</td>
                        <td>
                            <button class="modify" onclick="getAppointment('${appointment.id}', '${appointment.name}', '${appointment.idCard}', '${appointment.phone}', '${appointment.campus.value}', '${appointment.startTime}', '${appointment.endTime}', '${appointment.createTime}', '${appointment.unit}', '${appointment.transportation.value}', '${appointment.licensePlate}')">
                                查看详情
                            </button>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <div class="foot">
                <div class="foot_page">
                    共&nbsp;${pagination.total}&nbsp;条&nbsp;${pagination.pageSize}条/页&nbsp;
                    <c:if test="${pagination.pageNum gt 1}">
                        <a onclick="loadPreviousPage()">上一页</a>
                    </c:if>
                    ${pagination.pageNum} / ${requestScope.totalPage}
                    <c:if test="${pagination.pageNum lt requestScope.totalPage}">
                        <a onclick="loadNextPage()">下一页</a>
                    </c:if>
                </div>
            </div>
        </div>

        <div id="overlay" class="overlay"></div>

        <div id="popup_get" class="popup">
            <h2>查看详细信息</h2>
            <div>
                <label>预约ID：</label>
                <span id="getID"></span>
            </div>
            <div>
                <label>申请日期：</label>
                <span id="getCreateTime"></span>
            </div>
            <div>
                <label>预约校区：</label>
                <span id="getCampus"></span>
            </div>
            <div>
                <label>预约进校时间：</label>
                <span id="getStartTime"></span> - <span id="getEndTime"></span>
            </div>
            <div>
                <label>所在单位：</label>
                <span id="getUnit"></span>
            </div>
            <div>
                <label>姓名：</label>
                <span id="getName"></span>
            </div>
            <div>
                <label>身份证号：</label>
                <span id="getIDCard"></span>
            </div>
            <div>
                <label>电话：</label>
                <span id="getPhone"></span>
            </div>
            <div>
                <label>交通方式：</label>
                <span id="getTransportation"></span>
            </div>
            <div>
                <label>车牌号：</label>
                <span id="getLicensePlate"></span>
            </div>
            <div>
                <button onclick="closeGetPopup()">关闭</button>
            </div>
        </div>
    </div>
</div>

<script>
    let currPage = ${pagination.pageNum};
    let totalPage = ${requestScope.totalPage};

    function loadNextPage() {
        if (currPage === totalPage) {
            alert("已经是最后一页了");
            return;
        }
        currPage++;
        window.location.href = "${pageContext.request.contextPath}/admin/appointment/public/query?pageNum=" + currPage + "&pageSize=10";
    }

    function loadPreviousPage() {
        if (currPage === 1) {
            alert("已经是第一页了");
            return;
        }
        currPage--;
        window.location.href = "${pageContext.request.contextPath}/admin/appointment/public/query?pageNum=" + currPage + "&pageSize=10";
    }

    function getAppointment(id, name, idCard, phone, campus, startTime, endTime, createTime, unit, transportation, licensePlate) {
        document.getElementById("getID").innerText = id;
        document.getElementById("getName").innerText = name;
        document.getElementById("getIDCard").innerText = idCard;
        document.getElementById("getPhone").innerText = phone;
        document.getElementById("getCampus").innerText = campus === '1' ? '朝晖' : campus === '2' ? '屏峰' : '莫干山';
        document.getElementById("getStartTime").innerText = new Date(startTime).toISOString().split('T')[0];
        document.getElementById("getEndTime").innerText = new Date(endTime).toISOString().split('T')[0];
        document.getElementById("getCreateTime").innerText = new Date(createTime).toISOString().split('T')[0];
        document.getElementById("getUnit").innerText = unit;
        document.getElementById("getTransportation").innerText = transportation === '1' ? '步行' : '自驾';
        document.getElementById("getLicensePlate").innerText = licensePlate;
        showGetPopup();
    }

    function showGetPopup() {
        const overlay = document.getElementById("overlay");
        const popup = document.getElementById("popup_get");
        overlay.style.display = "block";
        popup.style.display = "block";
    }

    function closeGetPopup() {
        const overlay = document.getElementById("overlay");
        const popup = document.getElementById("popup_get");
        overlay.style.display = "none";
        popup.style.display = "none";
    }
</script>

</body>
</html>