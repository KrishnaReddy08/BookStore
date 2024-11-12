package com.project.BookStore.JWT;

import com.project.BookStore.exception.InvalidJwtTokenException;
import com.project.BookStore.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String AuthHeader = request.getHeader("Authorization");
        String Token;
        String UserName;
        try {
            if (AuthHeader != null && AuthHeader.startsWith("Bearer")) {
                Token = AuthHeader.substring(7);
                UserName = jwtservice.extractUsername(Token);

                if (UserName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userService.loadUserByUsername(UserName);

                    if (jwtservice.validate(Token, userDetails)) {
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
            }
            filterChain.doFilter(request, response);
        }
        catch (InvalidJwtTokenException e){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(e.getMessage());
            response.getWriter().flush();

        }

    }
}
