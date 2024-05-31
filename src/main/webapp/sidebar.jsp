<%--
  Created by IntelliJ IDEA.
  User: crk
  Date: 2024/5/31
  Time: 09:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="team.sugarsmile.cprms.model.Admin" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<jsp:useBean id="admin" scope="session" type="team.sugarsmile.cprms.model.Admin"/>
<div class="sidebar">
    <a href="${pageContext.request.contextPath}/homepage.jsp">Home</a>
    <a href="${pageContext.request.contextPath}/admin/appointment/official/list?pageNum=1&pageSize=10">公务预约</a>
    <a href="${pageContext.request.contextPath}/admin/appointment/public/list?pageNum=1&pageSize=10">社会预约</a>
    <a href="${pageContext.request.contextPath}/admin/system/list?pageNum=1&pageSize=10">管理员管理</a>
    <c:if test="${admin.adminType.value eq 2}">
        <a href="${pageContext.request.contextPath}/admin/department/list?pageNum=1&pageSize=10">部门管理</a>
    </c:if>
    <c:if test="${admin.adminType.value eq 2}">
        <a href="${pageContext.request.contextPath}/admin/departmentAdmin/list?pageNum=1&pageSize=10">部门管理员</a>
    </c:if>
</div>
