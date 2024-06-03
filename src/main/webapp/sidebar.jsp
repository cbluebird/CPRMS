<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="team.sugarsmile.cprms.model.Admin" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<jsp:useBean id="admin" scope="session" type="team.sugarsmile.cprms.model.Admin"/>
<div class="sidebar">
    <a href="${pageContext.request.contextPath}/homepage.jsp">Home</a>

    <c:if test="${admin.adminType.value eq 2||admin.adminType.value eq 3}">
        <a href="${pageContext.request.contextPath}/admin/appointment/official/list?pageNum=1&pageSize=10">公务预约</a>
    </c:if>

    <c:if test="${admin.adminType.value eq 2||admin.adminType.value eq 3}">
        <a href="${pageContext.request.contextPath}/admin/appointment/public/list?pageNum=1&pageSize=10">社会预约</a>
    </c:if>

    <c:if test="${admin.adminType.value eq 1}">
        <a href="${pageContext.request.contextPath}/admin/system/list?pageNum=1&pageSize=10">管理员管理</a>
    </c:if>

    <c:if test="${admin.adminType.value eq 4}">
        <a href="${pageContext.request.contextPath}/admin/audit/list?pageNum=1&pageSize=10">审计管理</a>
    </c:if>

    <c:if test="${admin.adminType.value eq 2}">
        <a href="${pageContext.request.contextPath}/admin/department/list?pageNum=1&pageSize=10">部门管理</a>
    </c:if>

    <c:if test="${admin.adminType.value eq 2}">
        <a href="${pageContext.request.contextPath}/admin/departmentAdmin/list?pageNum=1&pageSize=10">部门管理员</a>
    </c:if>
</div>
