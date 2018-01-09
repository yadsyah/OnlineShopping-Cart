<%--
  Created by IntelliJ IDEA.
  User: Dian
  Date: 1/9/2018
  Time: 1:35 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>


<div class="menu-container">

    <a href="${pageContext.request.contextPath}/">Home</a>
    |
    <a href="${pageContext.request.contextPath}/productList">
        Product List
    </a>
    |
    <a href="${pageContext.request.contextPath}/shoppingCart">
        My Cart
    </a>
    |
    <security:authorize  access="hasAnyRole('ROLE_MANAGER','ROLE_EMPLOYEE')">
        <a href="${pageContext.request.contextPath}/orderList">
            Order List
        </a>
        |
    </security:authorize>

    <security:authorize  access="hasRole('ROLE_MANAGER')">
        <a href="${pageContext.request.contextPath}/product">
            Create Product
        </a>
        |
    </security:authorize>

</div>