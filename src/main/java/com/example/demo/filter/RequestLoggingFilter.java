package com.example.demo.filter;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        long startTime = System.currentTimeMillis();

        log.info("Incoming Request: {} {}",
                request.getMethod(),
                request.getRequestURI());

        filterChain.doFilter(
                request,
                response);

        long endTime = System.currentTimeMillis();

        log.info(
                "Completed Request: {} {} Status={} Time={} ms",
                request.getMethod(),
                request.getRequestURI(),
                response.getStatus(),
                (endTime - startTime));
    }

}
