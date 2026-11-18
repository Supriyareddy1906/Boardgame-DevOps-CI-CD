package com.javaproject.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class LoggingAccessDeniedHandlerTest {

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldLogAuthenticatedUserAndRedirect() throws Exception {

        LoggingAccessDeniedHandler handler =
                new LoggingAccessDeniedHandler();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "testuser", "password"
                )
        );

        handler.handle(
                request,
                response,
                new AccessDeniedException("Access denied")
        );

        verify(response).sendRedirect("/permission-denied");
    }

    @Test
    void shouldRedirectWhenAuthenticationIsNull() throws Exception {

        LoggingAccessDeniedHandler handler =
                new LoggingAccessDeniedHandler();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        SecurityContextHolder.clearContext();

        handler.handle(
                request,
                response,
                new AccessDeniedException("Access denied")
        );

        verify(response).sendRedirect("/permission-denied");
    }
}