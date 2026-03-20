package com.example.jobsathi.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

/**
 * Created by Rabindra Adhikari on 3/9/26
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AppRequestLoginFilter extends OncePerRequestFilter {
    private static final int CACHE_LIMIT = 1 * 1024 * 1024;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request, CACHE_LIMIT);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        long startTime = System.currentTimeMillis();
        filterChain.doFilter(requestWrapper, responseWrapper);
        long timeTaken = System.currentTimeMillis() - startTime;

        String requestBody = new String(requestWrapper.getContentAsByteArray());
        LOGGER.info("Request: {} {} | Time: {}ms | Body: {}", request.getMethod(), request.getRequestURI(), timeTaken, requestBody);

        responseWrapper.copyBodyToResponse();
    }
}
