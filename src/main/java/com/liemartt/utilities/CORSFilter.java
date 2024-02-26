package com.liemartt.utilities;

import jakarta.servlet.*;

import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "CORSFilter", urlPatterns = "/*")
public class CORSFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Filter initialization can be handled here if necessary
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // Specify the allowed origins
        response.setHeader("Access-Control-Allow-Origin", "*");

        // Specify the HTTP methods allowed
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT, PATCH");

        // Specify the headers allowed in requests
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, x-requested-with, authorization");

        // If it's a preflight (OPTIONS) request, just return the headers (above)
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            // For other requests, continue the filter chain
            chain.doFilter(req, res);
        }
    }

    @Override
    public void destroy() {
        // Clean up any resources here if necessary
    }
}
