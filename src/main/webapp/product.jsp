<%--
  Created by IntelliJ IDEA.
  User: NEIL ADAVAN
  Date: 26/10/2024
  Time: 12:24 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Product List</title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<div class="container">
  <a href="index.jsp" class="btn btn-outline-secondary">Home</a>
  <h2>Available Products</h2>
  <%-- Display error message if available --%>
  <c:if test="${not empty errorMessage}">
    <div class="alert alert-danger">${errorMessage}</div>
  </c:if>

  <!-- Search form -->
  <form action="products" method="get">
    <%--<label>
      <input type="text" name="search" placeholder="Search products..." value="<%= request.getParameter("search") != null ? request.getParameter("search") : "" %>">
    </label>--%>
      <label>
        <input type="text" name="search" placeholder="Search products..."
               value="<c:out value='${param.search}'/>">
      </label>
    <button type="submit" class="btn btn-primary">Search</button>
  </form>
  <c:if test="${not empty param.search}">
    <div class="alert alert-info">
      Searched Keyword: <c:out value="${param.search}"/>
    </div>
  </c:if>
  <br/>

  <!-- Displaying products -->
  <table class="table table-bordered">
    <thead>
    <tr>
      <th>Product Name</th>
      <th>Action</th>
    </tr>
    </thead>
    <tbody>
      <c:choose>
        <c:when test="${not empty products}">
          <c:forEach var="product" items="${products}">
            <tr>
              <td><c:out value="${product.productName}" /></td>
              <td><a href="purchase?productId=${product.productId}" class="btn btn-success">Purchase</a></td>
            </tr>
          </c:forEach>
        </c:when>
        <c:otherwise>
          <tr>
            <td colspan="2">No products found.</td>
          </tr>
        </c:otherwise>
      </c:choose>
    </tbody>
  </table>
</div>
</body>
</html>

