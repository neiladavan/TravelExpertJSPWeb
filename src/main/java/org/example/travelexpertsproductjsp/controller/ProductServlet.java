package org.example.travelexpertsproductjsp.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.travelexpertsproductjsp.utils.InputSanitizer;
import org.example.travelexpertsproductjsp.model.Product;
import org.example.travelexpertsproductjsp.data.ProductDAO;
import org.example.travelexpertsproductjsp.utils.LoggerUtil;

import java.io.IOException;
import java.io.Serial;
import java.sql.SQLException;
import java.util.List;

// Assuming you have a Product and ProductDAO class for managing products
@WebServlet("/products")
public class ProductServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Retrieve the search parameter
            String search = request.getParameter("search");
            // Sanitize the input to prevent XSS attacks
            String sanitizedInput = InputSanitizer.sanitize(search);

            // Fetch the list of products, possibly filtered by the search term
            List<Product> products = ProductDAO.getProducts(sanitizedInput); // ProductDAO is assumed to fetch the products from the database

            // Set the products list as a request attribute
            request.setAttribute("products", products);

            // Forward the request to the product.jsp
            RequestDispatcher dispatcher = request.getRequestDispatcher("product.jsp");
            dispatcher.forward(request, response);
        } catch (SQLException e) {
            // Log the error (optional, if you have logging set up)
            LoggerUtil.logError("An error occurred while fetching products. Please try again later.", e);

            // Handle database errors
            request.setAttribute("errorMessage", "An error occurred while fetching products. Please try again later.");

            request.getRequestDispatcher("product.jsp").forward(request, response);
        } catch (ServletException | IOException e) {
            // Log the error (optional, if you have logging set up)
            LoggerUtil.logError("An unexpected error occurred. Please try again later.", e);

            // Handle other servlet exceptions
            request.setAttribute("errorMessage", "An unexpected error occurred. Please try again later.");
            request.getRequestDispatcher("product.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // You can implement POST handling if needed
    }
}

