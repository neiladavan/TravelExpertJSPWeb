package org.example.travelexpertsproductjsp.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.travelexpertsproductjsp.data.ProductDAO;
import org.example.travelexpertsproductjsp.model.Product;
import org.example.travelexpertsproductjsp.utils.DatabaseUtil;
import org.example.travelexpertsproductjsp.utils.InputSanitizer;
import org.example.travelexpertsproductjsp.utils.LoggerUtil;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serial;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String country = request.getParameter("country");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        if (country == null || country.isEmpty()) {
            out.print("<option value=''>Select a province/state</option>");
            return;
        }

        try {
            ResultSet resultSet;
            Connection connection = DatabaseUtil.getConnection();
            String sql = "SELECT province_state_code, province_state_name FROM ProvincesStates WHERE country_code = ? ORDER BY province_state_name";
            PreparedStatement statement = DatabaseUtil.createPreparedStatement(connection, sql, country);

            resultSet = statement.executeQuery();

            // Generate HTML <option> elements based on query results
            StringBuilder options = new StringBuilder();
            while (resultSet.next()) {
                String code = resultSet.getString("province_state_code");
                String name = resultSet.getString("province_state_name");
                options.append("<option value='").append(code).append("'>").append(name).append("</option>");
            }
            out.print(options);

        } catch (SQLException e) {
            // Set the error response before sending it
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("text/plain");
            response.getWriter().write("An error occurred while getting provinces/states. Please try again later.");
            return;
            //throw new RuntimeException(e);
        }
    }
}
