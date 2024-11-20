package org.example.travelexpertsproductjsp.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@WebServlet("/customer-reward")
public class CustomerRewardServlet extends HttpServlet {
    private static final String GET_CUSTOMER_REWARD_URL = "http://localhost:8080/TravelExpertsREST_war_exploded/api/customer-reward/get/";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Retrieve session and validate user authentication
        HttpSession session = request.getSession(false);
        if (session != null && Boolean.TRUE.equals(session.getAttribute("isAuthenticated"))) {
            String userId = (String) session.getAttribute("userId");
            System.out.println(userId);

            if (userId != null) {
                // Build API URL with customer ID
                String apiUrl = GET_CUSTOMER_REWARD_URL + userId;

                // Use HttpClient to call the REST API
                try (HttpClient client = HttpClient.newHttpClient()) {
                    HttpRequest apiRequest = HttpRequest.newBuilder()
                            .uri(URI.create(apiUrl))
                            .header("Content-Type", "application/json")
                            .build();

                    try {
                        // Send the request and capture the response
                        HttpResponse<String> apiResponse = client.send(apiRequest, HttpResponse.BodyHandlers.ofString());

                        // Set response headers and write API response to client
                        response.setContentType("application/json");
                        response.setStatus(HttpServletResponse.SC_OK);
                        response.getWriter().write(apiResponse.body());
                    } catch (Exception e) {
                        // Handle API call errors
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "API request failed: " + e.getMessage());
                    }
                }
            } else {
                // Customer ID missing in session
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Customer ID not found in session.");
            }
        } else {
            // User is not authenticated
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized access.");
        }
    }
}
