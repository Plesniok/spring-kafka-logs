package sgu.kafkaLog.middlewares;//package sgu.viki.vikicameras.sgu.kafkaLog.middlewares;
//
//import jakarta.servlet.*;
//import org.springframework.stereotype.Component;
//import org.springframework.web.util.ContentCachingRequestWrapper;
//import org.springframework.web.util.ContentCachingResponseWrapper;
//
//import javax.servlet.Filter;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.UnsupportedEncodingException;
//import java.util.Enumeration;
//
//@Component
//public class LoggingFilter extends GenericFilter {
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//
//        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
//            chain.doFilter(request, response);
//            return;
//        }
//
//        HttpServletRequest httpRequest = (HttpServletRequest) request;
//        HttpServletResponse httpResponse = (HttpServletResponse) response;
//
//        // Log request details
//        logRequestDetails(httpRequest);
//
//        // Proceed with the filter chain
//        chain.doFilter(request, response);
//
//        // Log response details (can customize further if needed)
//        logResponseDetails(httpResponse);
//    }
//
//    private void logRequestDetails(HttpServletRequest request) throws IOException {
//        // Log method, URI, and query parameters
//        String method = request.getMethod();
//        String uri = request.getRequestURI();
//        String queryString = request.getQueryString();
//        System.out.println("Request: Method=" + method + ", URI=" + uri +
//                (queryString != null ? ", QueryString=" + queryString : ""));
//
//        // Log headers
//        Enumeration<String> headerNames = request.getHeaderNames();
//        while (headerNames.hasMoreElements()) {
//            String headerName = headerNames.nextElement();
//            System.out.println("Header: " + headerName + " = " + request.getHeader(headerName));
//        }
//
//        // Log body (if applicable)
//        String body = getRequestBody(request);
//        if (!body.isEmpty()) {
//            System.out.println("Body: " + body);
//        }
//    }
//
//    private String getRequestBody(HttpServletRequest request) throws IOException {
//        StringBuilder body = new StringBuilder();
//        try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                body.append(line).append("\n");
//            }
//        }
//        return body.toString().trim();
//    }
//
//    private void logResponseDetails(HttpServletResponse response) {
//        int status = response.getStatus();
//        System.out.println("Response: Status=" + status);
//        // Note: Capturing response body requires a more complex approach.
//    }
//}