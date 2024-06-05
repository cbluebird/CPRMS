<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<jsp:useBean id="admin" scope="session" type="team.sugarsmile.cprms.model.Admin"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin/sidebar.css">
<div class="sidebar">
    <a href="${pageContext.request.contextPath}/admin/home.jsp">主页</a>

    <c:if test="${admin.adminType.value eq 1}">
        <a href="${pageContext.request.contextPath}/admin/system/list?pageNum=1&pageSize=10">管理员管理</a>
    </c:if>

    <c:if test="${admin.adminType.value eq 2}">
        <a href="${pageContext.request.contextPath}/admin/department/list?pageNum=1&pageSize=10">部门管理</a>
    </c:if>

    <c:if test="${admin.adminType.value eq 2}">
        <a href="${pageContext.request.contextPath}/admin/departmentAdmin/list?pageNum=1&pageSize=10">部门管理员管理</a>
    </c:if>

    <c:if test="${admin.adminType.value eq 2 || admin.adminType.value eq 3}">
        <a href="${pageContext.request.contextPath}/admin/appointment/public/list?pageNum=1&pageSize=10">社会公众预约管理</a>
    </c:if>

    <c:if test="${admin.adminType.value eq 2 || admin.adminType.value eq 3}">
        <a href="${pageContext.request.contextPath}/admin/appointment/official/list?pageNum=1&pageSize=10">公务预约管理</a>
    </c:if>

    <c:if test="${admin.adminType.value eq 4}">
        <a href="${pageContext.request.contextPath}/admin/audit/list?pageNum=1&pageSize=10">审计管理</a>
    </c:if>

    <a href="${pageContext.request.contextPath}/admin/updatePassword.jsp">修改密码</a>

    <a href="${pageContext.request.contextPath}/admin/logout">退出登录</a>
</div>