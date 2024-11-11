package com.project.BookStore.JWT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import com.project.BookStore.exception.InvalidJwtTokenSignatureException;
import com.project.BookStore.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
public class JWTFilterTest {

    @Mock
    private jwtservice jwtservice;

    @Mock
    private UserService userService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;;

    @Mock
    private FilterChain filterChain;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private JWTFilter jwtFilter;

    @BeforeEach
    public void setUp() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void testDoFilterInternal_withValidToken() throws ServletException, IOException {
        String token = "valid-token";
        String username = "valid-username";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtservice.extractUsername(token)).thenReturn(username);
        when(userService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtservice.validate(token, userDetails)).thenReturn(true);

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(jwtservice, times(1)).extractUsername(token);
        verify(userService, times(1)).loadUserByUsername(username);
        verify(jwtservice, times(1)).validate(token, userDetails);
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_withInvalidToken() throws ServletException, IOException {
        String token = "invalid-token";
        String username = "invalid-username";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtservice.extractUsername(token)).thenReturn(username);
        when(userService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtservice.validate(token, userDetails)).thenReturn(false);

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(jwtservice, times(1)).extractUsername(token);
        verify(userService, times(1)).loadUserByUsername(username);
        verify(jwtservice, times(1)).validate(token, userDetails);
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_withNullAuthHeader() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(jwtservice, never()).extractUsername(anyString());
        verify(userService, never()).loadUserByUsername(anyString());
        verify(jwtservice, never()).validate(anyString(), any());
        verify(filterChain, times(1)).doFilter(request, response);
    }

}
