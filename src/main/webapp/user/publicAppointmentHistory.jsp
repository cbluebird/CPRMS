<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<jsp:include page="sidebar.jsp"/>
<jsp:useBean id="pagination" scope="request"
             type="team.sugarsmile.cprms.dto.PaginationDto<team.sugarsmile.cprms.model.PublicAppointment>"/>
<fmt:formatNumber var="totalPage" scope="request" type="number"
                  value="${pagination.total == 0 ? 1 : (pagination.total - 1) / pagination.pageSize + 0.51}"
                  maxFractionDigits="0"/>
<html>
<head>
    <title>我的预约</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/user/appointment-history.css">
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
    <div class="header">
        <h2>社会公众进校预约记录</h2>
    </div>
    <div class="table">
        <div>
            <button class="button" id="change-button" onclick="changeAppointment()">公务进校预约记录</button>
        </div>
        <div class="table-container">
            <table>
                <thead>
                <tr>
                    <th>申请日期</th>
                    <th>预约校区</th>
                    <th>预约日期</th>
                    <th>通行码</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="appointment" items="${pagination.list}">
                    <tr>
                        <td>
                                ${appointment.createTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))}
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${appointment.campus.value == 1}">朝晖</c:when>
                                <c:when test="${appointment.campus.value == 2}">屏峰</c:when>
                                <c:when test="${appointment.campus.value == 3}">莫干山</c:when>
                            </c:choose>
                        </td>
                        <td>
                                ${appointment.appointmentTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))}
                        </td>
                        <td>
                            <button class="button" id="passcode-button" onclick="getPasscode('${appointment.id}')">
                                查看
                            </button>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
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
</div>
</body>
</html>
<script>
    function changeAppointment() {
        window.location.href = "${pageContext.request.contextPath}/user/appointment/official/list?pageNum=1&pageSize=10";
    }

    let currPage = ${pagination.pageNum};
    let totalPage = ${requestScope.totalPage};

    function loadNextPage() {
        if (currPage === totalPage) {
            alert("已经是最后一页了");
            return;
        }
        currPage++;
        window.location.href = "${pageContext.request.contextPath}/user/appointment/public/list?pageNum=" + currPage + "&pageSize=10";
    }

    function loadPreviousPage() {
        if (currPage === 1) {
            alert("已经是第一页了");
            return;
        }
        currPage--;
        window.location.href = "${pageContext.request.contextPath}/user/appointment/public/list?pageNum=" + currPage + "&pageSize=10";
    }

    function getPasscode(id) {
        window.location.href = "${pageContext.request.contextPath}/user/passcode/query?type=public&id=" + id;
    }
</script>