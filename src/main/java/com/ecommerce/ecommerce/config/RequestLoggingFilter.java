package com.ecommerce.ecommerce.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);
    private static final List<String> SENSITIVE_HEADERS = Arrays.asList(
            "authorization", "cookie", "set-cookie", "x-api-key"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Wrappers para cachear request/response
        ContentCachingRequestWrapper requestWrapper =
                new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper =
                new ContentCachingResponseWrapper(response);

        long startTime = System.currentTimeMillis();

        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {
            long duration = System.currentTimeMillis() - startTime;

            // Log apenas para endpoints importantes
            if (shouldLog(requestWrapper)) {
                logRequest(requestWrapper, responseWrapper, duration);
            }

            // Copiar response para o output stream original
            responseWrapper.copyBodyToResponse();
        }
    }

    private boolean shouldLog(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/api/auth") ||
                path.startsWith("/api/admin") ||
                path.startsWith("/api/imagens/upload") ||
                request.getMethod().equals("POST") ||
                request.getMethod().equals("PUT") ||
                request.getMethod().equals("DELETE");
    }

    private void logRequest(ContentCachingRequestWrapper request,
                            ContentCachingResponseWrapper response,
                            long duration) {

        Map<String, Object> logData = new LinkedHashMap<>();
        logData.put("timestamp", new Date());
        logData.put("method", request.getMethod());
        logData.put("uri", request.getRequestURI());
        logData.put("query", request.getQueryString());
        logData.put("status", response.getStatus());
        logData.put("duration", duration + "ms");
        logData.put("clientIp", getClientIP(request));
        logData.put("userAgent", request.getHeader("User-Agent"));

        // Headers (removendo sensíveis)
        Map<String, String> headers = new HashMap<>();
        Collections.list(request.getHeaderNames())
                .forEach(headerName -> {
                    if (!SENSITIVE_HEADERS.contains(headerName.toLowerCase())) {
                        headers.put(headerName, request.getHeader(headerName));
                    }
                });
        logData.put("headers", headers);

        // Request body (para logs de segurança)
        String requestBody = getContentAsString(request.getContentAsByteArray(),
                request.getCharacterEncoding());
        if (requestBody != null && !requestBody.isEmpty()) {
            logData.put("requestBody", maskSensitiveData(requestBody));
        }

        logger.info("Security Log: {}", logData);
    }

    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

    private String getContentAsString(byte[] buf, String charsetName) {
        if (buf == null || buf.length == 0) return "";
        try {
            return new String(buf, 0, buf.length, charsetName != null ?
                    charsetName : "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            return "[unknown]";
        }
    }

    private String maskSensitiveData(String data) {
        // Mascara senhas, tokens, etc.
        return data.replaceAll("(?i)(\"password\"\\s*:\\s*\")[^\"]*(\")",
                        "$1***$2")
                .replaceAll("(?i)(\"senha\"\\s*:\\s*\")[^\"]*(\")",
                        "$1***$2")
                .replaceAll("(?i)(\"token\"\\s*:\\s*\")[^\"]*(\")",
                        "$1***$2");
    }
}