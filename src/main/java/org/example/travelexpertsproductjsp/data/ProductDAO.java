package org.example.travelexpertsproductjsp.data;

import org.example.travelexpertsproductjsp.utils.DatabaseUtil;
import org.example.travelexpertsproductjsp.model.Product;
import org.example.travelexpertsproductjsp.utils.LoggerUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    private static Connection connection = null; // Declare the connection here

    // Method to get a list of products, optionally filtered by a search term
    public static List<Product> getProducts(String searchTerm) throws SQLException {
        List<Product> products = new ArrayList<>();

        // SQL query with optional filtering by product name
        String sql = "SELECT * FROM Products";
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            searchTerm = "%" + searchTerm + "%";
            sql += " WHERE ProdName LIKE ?";
        }

        try {
            connection = DatabaseUtil.getConnection(); // Get the connection
            assert connection != null;
            PreparedStatement statement = DatabaseUtil.createPreparedStatement(connection, sql, searchTerm);

            // Execute the query
            ResultSet resultSet = statement.executeQuery();

            // Process the result set and create Product objects
            while (resultSet.next()) {
                int productId = resultSet.getInt("ProductId");
                String productName = resultSet.getString("ProdName");

                // Create a new Product object and add it to the list
                Product product = new Product(productId, productName);
                products.add(product);
            }

        } catch (SQLException e) {
            throw new SQLException("Database access error", e);
            //e.printStackTrace(); // You should log the error instead of printing the stack trace
        }

        return products;
    }

    public static Product getProductById(int productId) throws SQLException {
        Product product = null;
        String sql = "SELECT * FROM Products WHERE ProductId = ?";

        try {
            connection = DatabaseUtil.getConnection(); // Get the connection
            assert connection != null;
            PreparedStatement statement = DatabaseUtil.createPreparedStatement(connection, sql, productId);

            //statement.setString(1, productId);
            ResultSet resultSet = statement.executeQuery();

            // If the product is found, create a Product object
            if (resultSet.next()) {
                int id = resultSet.getInt("ProductId");
                String name = resultSet.getString("ProdName");

                product = new Product(id, name); // Adjust based on your Product class constructor
            }

        } catch (SQLException e) {
            // Log the error (optional, if you have logging set up)
            LoggerUtil.logError("Database access error.", e);
            throw new SQLException("Database access error", e);
        }

        return product;
    }
}
