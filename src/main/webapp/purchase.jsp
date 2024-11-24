<%@ page import="org.example.travelexpertsproductjsp.model.Product" %><%--
  Created by IntelliJ IDEA.
  User: NEIL ADAVAN
  Date: 29/10/2024
  Time: 3:05 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Product Details</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body>
<div class="container">
    <a href="index.jsp" class="btn btn-outline-secondary">Home</a>
    <h1>Product Details</h1>
    <table class="table">
        <tbody>
        <tr>
            <th>Product ID:</th>
            <td><%= ((Product) request.getAttribute("product")).getProductId() %></td>
        </tr>
        <tr>
            <th>Product Name</th>
            <td><c:out value="${product.productName}" /></td>
        </tr>
        </tbody>
    </table>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
