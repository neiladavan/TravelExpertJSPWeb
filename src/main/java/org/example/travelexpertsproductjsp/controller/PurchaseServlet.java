package org.example.travelexpertsproductjsp.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.travelexpertsproductjsp.data.ProductDAO;
import org.example.travelexpertsproductjsp.model.Product;
import org.example.travelexpertsproductjsp.utils.LoggerUtil;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/purchase")
public class PurchaseServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if the user is logged in by verifying a session attribute
        HttpSession session = request.getSession(true); // Create a new session if it doesn't exist
        if (session.getAttribute("isAuthenticated") == null || !(Boolean) session.getAttribute("isAuthenticated")) {
            session.setAttribute("errorMessage", "User must be logged in to purchase.");
            response.sendRedirect("login");
            return;
        }

        Product product = null;
        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            product = ProductDAO.getProductById(productId);
        } catch (NumberFormatException e) {
            // Log the error (optional, if you have logging set up)
            LoggerUtil.logError("Invalid product ID. Please try again with a valid ID.", e);

            // Handle the case where the productId is not a valid integer
            request.setAttribute("errorMessage", "Invalid product ID. Please try again with a valid ID.");
        } catch (SQLException e) {
            // Log the error (optional, if you have logging set up)
            LoggerUtil.logError("An error occurred while fetching product details. Please try again later.", e);

            // Handle database-related errors
            request.setAttribute("errorMessage", "An error occurred while fetching product details. Please try again later.");
        }

        // Check if the product exists
        if (product == null) {
            // Set an error message if the product is not found and forward to an error page or home page
            request.setAttribute("errorMessage", "Product not found.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("product.jsp");
            dispatcher.forward(request, response);
            return;
        }

        // Set product details in the request and forward to the purchase page
        request.setAttribute("product", product);
        RequestDispatcher dispatcher = request.getRequestDispatcher("purchase.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Implement purchase processing logic here if required, like handling payment
    }
}
