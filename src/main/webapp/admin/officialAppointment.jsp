<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<jsp:include page="sidebar.jsp"/>
<jsp:useBean id="pagination" scope="request"
             type="team.sugarsmile.cprms.dto.PaginationDto<team.sugarsmile.cprms.model.OfficialAppointment>"/>
<jsp:useBean id="departmentMap" scope="request" type="java.util.HashMap"/>
<fmt:formatNumber var="totalPage" scope="request" type="number"
                  value="${pagination.total == 0 ? 1 : (pagination.total - 1) / pagination.pageSize + 0.51}"
                  maxFractionDigits="0"/>
<jsp:useBean id="admin" scope="session" type="team.sugarsmile.cprms.model.Admin"/>
<html>
<head>
    <title>公务预约管理</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin/table.css">
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
<div class="container">
    <div class="table">
        <div class="header-container">
            <h2>公务预约管理</h2>
        </div>
        <div class="search-container">
            <form action="${pageContext.request.contextPath}/admin/appointment/official/query?pageNum=1&pageSize=10"
                  method="get">
                <div class="row">
                    <label for="applyDate">申请日期:</label>
                    <input type="date" id="applyDate" name="applyDate" value="${param.applyDate}">

                    <label for="appointmentDate">预约日期:</label>
                    <input type="date" id="appointmentDate" name="appointmentDate" value="${param.appointmentDate}">

                    <label for="countApplyDate">申请月度:</label>
                    <input type="month" id="countApplyDate" name="countApplyDate" value="${param.countApplyDate}">

                    <label for="countAppointmentDate">预约月度:</label>
                    <input type="month" id="countAppointmentDate" name="countAppointmentDate"
                           value="${param.countAppointmentDate}">

                    <label for="campus">预约校区:</label>
                    <select id="campus" name="campus">
                        <option value="">全部</option>
                        <option value="1" <c:if test="${param.campus == '1'}">selected</c:if>>朝晖校区</option>
                        <option value="2" <c:if test="${param.campus == '2'}">selected</c:if>>屏峰校区</option>
                        <option value="3" <c:if test="${param.campus == '3'}">selected</c:if>>莫干山校区</option>
                    </select>
                </div>
                <div class="row">
                    <label for="unit">所在单位:</label>
                    <input type="text" id="unit" name="unit" value="${param.unit}">

                    <label for="name">预约人姓名:</label>
                    <input type="text" id="name" name="name" value="${param.name}">

                    <label for="idCard">身份证号:</label>
                    <input type="text" id="idCard" name="idCard" value="${param.idCard}">
                </div>
                <div class="row">
                    <label for="department">访问部门:</label>
                    <select id="department" name="department">
                        <option value="">请选择</option>
                        <c:forEach items="${departmentMap}" var="entry">
                            <option value="${entry.key}"
                                    <c:if test="${param.department == entry.key}">selected</c:if>>${entry.value.name}</option>
                        </c:forEach>
                    </select>

                    <label for="receptionist">访问接待人:</label>
                    <input type="text" id="receptionist" name="receptionist" value="${param.receptionist}">

                    <label for="status">审核状态:</label>
                    <select id="status" name="status">
                        <option value="">全部</option>
                        <option value="1" <c:if test="${param.status == '1'}">selected</c:if>>未审核</option>
                        <option value="2" <c:if test="${param.status == '2'}">selected</c:if>>通过</option>
                        <option value="3" <c:if test="${param.status == '3'}">selected</c:if>>驳回</option>
                    </select>
                </div>
                <div class="row">
                    <button type="submit" class="query">查询</button>
                    <label for="count">符合条件的记录总数:</label>
                    <span id="count">${pagination.total}</span>
                </div>
            </form>
        </div>
        <div class="table-container">
            <table>
                <thead>
                <tr>
                    <th>申请日期</th>
                    <th>预约校区</th>
                    <th>预约日期</th>
                    <th>所在单位</th>
                    <th>姓名</th>
                    <th>访问部门</th>
                    <th>访问接待人</th>
                    <th>审核状态</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="appointment" items="${pagination.list}">
                    <tr>
                        <td>${appointment.createTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))}</td>
                        <td>
                            <c:choose>
                                <c:when test="${appointment.campus.value == 1}">朝晖</c:when>
                                <c:when test="${appointment.campus.value == 2}">屏峰</c:when>
                                <c:when test="${appointment.campus.value == 3}">莫干山</c:when>
                            </c:choose>
                        </td>
                        <td>${appointment.appointmentTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))}</td>
                        <td>${appointment.unit}</td>
                        <td>${appointment.name}</td>
                        <td>${departmentMap[appointment.departmentId].name}</td>
                        <td>${appointment.receptionist}</td>
                        <td>
                            <c:choose>
                                <c:when test="${appointment.status.value == 1}">未审核</c:when>
                                <c:when test="${appointment.status.value == 2}">通过</c:when>
                                <c:when test="${appointment.status.value == 3}">驳回</c:when>
                            </c:choose>
                        </td>
                        <td>
                            <button class="detail"
                                    onclick="getAppointment('${appointment.id}', '${appointment.name}', '${appointment.idCard}', '${appointment.phone}', '${appointment.campus.value}', '${appointment.appointmentTime}', '${appointment.createTime}', '${appointment.unit}', '${appointment.transportation.value}', '${appointment.licensePlate}', '${departmentMap[appointment.departmentId].name}', '${appointment.receptionist}', '${appointment.reason}', '${appointment.status.value}')">
                                查看详情
                            </button>
                            <c:choose>
                                <c:when test="${appointment.status.value == 1}">
                                    <button class="approve"
                                            onclick="reviewAppointment('${appointment.id}',2)">
                                        通过
                                    </button>
                                    <button class="reject"
                                            onclick="reviewAppointment('${appointment.id}',3)">
                                        驳回
                                    </button>
                                </c:when>
                                <c:otherwise>
                                    <button class="cancel"
                                            onclick="reviewAppointment('${appointment.id}',1)">
                                        撤销审核
                                    </button>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
        <div class="foot-container">
            <div class="foot-page">
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

        <div id="overlay" class="overlay"></div>

        <div id="popup-get" class="popup">
            <h2>查看详细信息</h2>
            <div>
                <label>预约ID:</label>
                <span id="getID"></span>
            </div>
            <div>
                <label>申请日期:</label>
                <span id="getCreateTime"></span>
            </div>
            <div>
                <label>预约校区:</label>
                <span id="getCampus"></span>
            </div>
            <div>
                <label>预约日期:</label>
                <span id="getAppointmentTime"></span>
            </div>
            <div>
                <label>所在单位:</label>
                <span id="getUnit"></span>
            </div>
            <div>
                <label>姓名:</label>
                <span id="getName"></span>
            </div>
            <div>
                <label>身份证号:</label>
                <span id="getIDCard"></span>
            </div>
            <div>
                <label>电话:</label>
                <span id="getPhone"></span>
            </div>
            <div>
                <label>交通方式:</label>
                <span id="getTransportation"></span>
            </div>
            <div id="licensePlate">
                <label>车牌号:</label>
                <span id="getLicensePlate"></span>
            </div>
            <div>
                <label>公务访问部门:</label>
                <span id="getDepartmentID"></span>
            </div>
            <div>
                <label>公务访问接待人:</label>
                <span id="getReceptionist"></span>
            </div>
            <div>
                <label>来访事由:</label>
                <span id="getReason"></span>
            </div>
            <div>
                <label>审核状态:</label>
                <span id="getStatus"></span>
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
        updatePageParams();
    }

    function loadPreviousPage() {
        if (currPage === 1) {
            alert("已经是第一页了");
            return;
        }
        currPage--;
        updatePageParams();
    }

    function updatePageParams() {
        const params = new URLSearchParams(window.location.search);
        params.set('pageNum', currPage);
        params.set('pageSize', ${pagination.pageSize});
        window.location.search = params.toString();
    }

    function reviewAppointment(id, status) {
        window.location.href = "${pageContext.request.contextPath}/admin/appointment/official/review?id=" + id + "&status=" + status;
    }

    function getAppointment(id, name, idCard, phone, campus, appointmentTime, createTime, unit, transportation, licensePlate, departmentId, receptionist, reason, status) {
        document.getElementById("getID").innerText = id;
        document.getElementById("getName").innerText = name;
        document.getElementById("getIDCard").innerText = idCard;
        document.getElementById("getPhone").innerText = phone;
        document.getElementById("getCampus").innerText = campus === '1' ? '朝晖' : campus === '2' ? '屏峰' : '莫干山';
        document.getElementById("getAppointmentTime").innerText = new Date(appointmentTime).toISOString().split('T')[0];
        document.getElementById("getCreateTime").innerText = new Date(createTime).toISOString().split('T')[0];
        document.getElementById("getUnit").innerText = unit;
        document.getElementById("getTransportation").innerText = transportation === '1' ? '步行' : '自驾';
        if (transportation === '1') {
            document.getElementById("licensePlate").style.display = 'none';
        } else {
            document.getElementById("licensePlate").style.display = 'block';
            document.getElementById("getLicensePlate").innerText = licensePlate;
        }
        document.getElementById("getDepartmentID").innerText = departmentId;
        document.getElementById("getReceptionist").innerText = receptionist;
        document.getElementById("getReason").innerText = reason;
        document.getElementById("getStatus").innerText = status === '1' ? '未审核' : status === '2' ? '通过' : '驳回';
        showGetPopup();
    }

    function showGetPopup() {
        const overlay = document.getElementById("overlay");
        const popup = document.getElementById("popup-get");
        overlay.style.display = "block";
        popup.style.display = "block";
    }

    function closeGetPopup() {
        const overlay = document.getElementById("overlay");
        const popup = document.getElementById("popup-get");
        overlay.style.display = "none";
        popup.style.display = "none";
    }
</script>
</body>
</html>
