package org.example.travelexpertsproductjsp;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class AuthorizationFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        String uri = httpRequest.getRequestURI();
        String role = session != null ? (String) session.getAttribute("role") : null;
        boolean isAuthorized = session != null && session.getAttribute("isAuthenticated") != null;
        if (uri.endsWith("/product.jsp") && !isAuthorized) {
            httpResponse.sendRedirect("unauthorized.jsp");
        } else {
            chain.doFilter(request, response);
        }
    }
}

