package org.example.travelexpertsproductjsp.controller;

import java.io.*;
import java.sql.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.travelexpertsproductjsp.utils.DatabaseUtil;
import org.example.travelexpertsproductjsp.utils.LoggerUtil;
import org.mindrot.jbcrypt.BCrypt;

@WebServlet(name = "loginServlet", value = "/login")
public class LoginServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;
    private String agentFirstName = "";

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("isAuthenticated") != null) {
            // User is already logged in
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<h1>Welcome back, " + session.getAttribute("username") + "!</h1>");
            out.println("</body></html>");
            //session.invalidate();
        } else {
            // Display the login form
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();

            String errorMessage = (String) session.getAttribute("errorMessage");
            if (errorMessage != null)
            {
                displayLoginForm(out, errorMessage);
                session.removeAttribute("errorMessage");
            }
            else displayLoginForm(out, null);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String username = request.getParameter("username");
        String passwd = request.getParameter("passwd");

        //String hashedPassword = BCrypt.hashpw(passwd, BCrypt.gensalt());
        //System.out.println(hashedPassword);

        if (username == null || passwd == null || username.isEmpty() || passwd.isEmpty()) {
            // Invalid input, show login form with error message
            LoggerUtil.logError("Please enter both username and password.", new Exception("Invalid username and password."));
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            displayLoginForm(out, "Please enter both username and password.");
            return;
        }

        // Check the login credentials against the database
        if (checkLogin(username, passwd)) {
            // Login successful
            HttpSession session = request.getSession();
            session.setAttribute("agentFirstName", agentFirstName);
            session.setAttribute("username", username); // Store the user ID in the session
            session.setAttribute("isAuthenticated", true); // Optional: set an authentication flag

            request.setAttribute("message", "Welcome " + username);

            // Forward the request to the result JSP page
            request.getRequestDispatcher("index.jsp").forward(request, response);

        } else {
            // Login failed
            LoggerUtil.logError("Login failed. Please check your username and password.", new Exception("Login failed."));
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            displayLoginForm(out, "Login failed. Please check your username and password.");
        }
    }

    private boolean checkLogin(String username, String passwd){
        boolean isValid = false;
        Connection connection;
        PreparedStatement statement;
        ResultSet resultSet;

        try {
            // Establish the connection
            connection = DatabaseUtil.getConnection();

            // Query to check if the user exists with the provided password
            //String query = "SELECT * FROM users WHERE username = ?";
            String query = """
                    SELECT u.username, u.password, a.AgtFirstName
                    FROM users u
                    INNER JOIN agents a ON u.agentId = a.AgentId
                    WHERE u.username = ?;""";
            statement = DatabaseUtil.createPreparedStatement(connection, query, username);

            resultSet = statement.executeQuery();

            // If a record is found, login is valid
            if (resultSet.next()) {
                String storedHash = resultSet.getString("password");
                agentFirstName = resultSet.getString("AgtFirstName");
                if (BCrypt.checkpw(passwd, storedHash)) isValid = true;
            }
        } catch (SQLException e) {
            //e.printStackTrace();
            LoggerUtil.logError("An error occurred while logging in. Please try again later.", e);
            return false;
        }
        return isValid;
    }

    private void displayLoginForm(PrintWriter out, String errorMessage) {
        out.println("<!DOCTYPE html>");
        out.println("<html lang=\"en\">");
        out.println("<head>");
        out.println("<title>Login Page</title>");
        out.println("<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css\">");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class=\"container\">");
        out.println("<h1>Login Page</h1>");
        if (errorMessage != null) {
            out.println("<div class=\"alert alert-danger\" role=\"alert\">");
            out.println(errorMessage);
            out.println("</div>");
        }
        out.println("<form action=\"login\" method=\"post\">");
        out.println("<div class=\"mb-3\">");
        out.println("<label for=\"username\" class=\"form-label\">Username:</label>");
        out.println("<input type=\"text\" class=\"form-control\" id=\"username\" name=\"username\" required>");
        out.println("</div>");
        out.println("<div class=\"mb-3\">");
        out.println("<label for=\"passwd\" class=\"form-label\">Password:</label>");
        out.println("<input type=\"password\" class=\"form-control\" id=\"passwd\" name=\"passwd\" required>");
        out.println("</div>");
        out.println("<button type=\"submit\" class=\"btn btn-primary\">Login</button>");
        out.println("</form>");
        out.println("</div>");
        out.println("<script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js\"></script>");
        out.println("</body>");
        out.println("</html>");
    }
}