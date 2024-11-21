package org.example.travelexpertsproductjsp.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@WebServlet("/customer-reward")
public class CustomerRewardServlet extends HttpServlet {
    private static final String GET_CUSTOMER_REWARD_URL = "http://localhost:8080/TravelExpertsREST_war_exploded/api/customer-reward/get/";
    private static final String POST_CUSTOMER_REWARD_URL = "http://localhost:8080/TravelExpertsREST_war_exploded/api/customer-reward/post/";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userId = getAuthenticatedUserId(request, response);
        if (userId != null) {
            String apiUrl = GET_CUSTOMER_REWARD_URL + userId;
            sendApiRequest(response, apiUrl, null);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userId = getAuthenticatedUserId(request, response);
        if (userId != null) {
            String requestBody = new String(request.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            JSONObject jsonObject = new JSONObject(requestBody);
            jsonObject.put("customerId", userId);  // Add customerId to the JSON payload

            String modifiedRequestBody = jsonObject.toString();
            sendApiRequest(response, POST_CUSTOMER_REWARD_URL, modifiedRequestBody);
        }
    }

    // Helper method to get the authenticated user ID from session
    private String getAuthenticatedUserId(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null && Boolean.TRUE.equals(session.getAttribute("isAuthenticated"))) {
            return (String) session.getAttribute("userId");
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized access.");
            return null;
        }
    }

    // Helper method to send an API request (GET or POST) and handle the response
    private void sendApiRequest(HttpServletResponse response, String apiUrl, String requestBody) throws IOException {
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("Content-Type", "application/json");

            if (requestBody != null) {
                requestBuilder.POST(HttpRequest.BodyPublishers.ofString(requestBody));
            } else {
                requestBuilder.GET();
            }

            HttpRequest apiRequest = requestBuilder.build();

            // Send the request and capture the response
            HttpResponse<String> apiResponse = client.send(apiRequest, HttpResponse.BodyHandlers.ofString());

            // Write the API response back to the client
            response.setContentType("application/json");
            response.setStatus(apiResponse.statusCode());
            response.getWriter().write(apiResponse.body());
        } catch (Exception e) {
            System.err.println("API request failed: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "API request failed: " + e.getMessage());
        }
    }
}
