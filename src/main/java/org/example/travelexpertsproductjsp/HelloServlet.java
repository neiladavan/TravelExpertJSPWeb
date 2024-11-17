package org.example.travelexpertsproductjsp;

import java.io.*;
import java.text.DecimalFormat;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;
    @Serial
    private static final long serialVersionUID = 1L;

    public void init() {
        message = "Hello OOSD Students!!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");
        out.println("</body></html>");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        final double INITIAL_KM = 100.0;
        final double CANADIAN_MPG = 2.8248;
        final double US_MPG = 2.352;

        // Get the input value from the form
        String litresPer100kmStr = request.getParameter("litresPer100km");
        double litresPer100km;
        try {
            litresPer100km = Double.parseDouble(litresPer100kmStr);
        } catch (NumberFormatException e) {
            litresPer100km = 0;
        }

        // Perform the conversion
        double kilometersPerLitre = INITIAL_KM / litresPer100km;
        double mpgCanadian = kilometersPerLitre * CANADIAN_MPG;
        double mpgUS = kilometersPerLitre * US_MPG;

        // Create a DecimalFormat instance
        DecimalFormat df = new DecimalFormat("#.##"); // 2 decimal places

        // Set the results as request attributes
        request.setAttribute("litresPer100km", litresPer100kmStr);
        request.setAttribute("kilometersPerLitre", df.format(kilometersPerLitre));
        request.setAttribute("mpgCanadian", df.format(mpgCanadian));
        request.setAttribute("mpgUS", df.format(mpgUS));

        // Forward the request to the result JSP page
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    public void destroy() {
    }
}