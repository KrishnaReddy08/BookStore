package com.project.BookStore.JWT;

import com.project.BookStore.exception.InvalidJwtTokenSignatureException;
import com.project.BookStore.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    private jwtservice jwtservice;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String AuthHeader = request.getHeader("Authorization");
        String Token;
        String UserName;

        if (AuthHeader != null && AuthHeader.startsWith("Bearer")) {
            Token = AuthHeader.substring(7);
            UserName = jwtservice.extractUsername(Token);

            try {

                if (UserName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userService.loadUserByUsername(UserName);

                    if (jwtservice.validate(Token, userDetails)) {
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
            } catch (InvalidJwtTokenSignatureException e) {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.getWriter().write("INVALID TOKEN");
                response.getWriter().flush();
                response.getWriter().close();
                return;
            }

        }
        filterChain.doFilter(request, response);
    }
}
