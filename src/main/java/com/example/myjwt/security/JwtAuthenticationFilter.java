package com.example.myjwt.security;

import com.example.myjwt.security.user.details.AdminUserDetailsService;
import com.example.myjwt.security.user.details.EmployeeUserDetailsService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtGenerator tokenGenerator;
    @Autowired
    private EmployeeUserDetailsService employeeUserDetailsService;
    @Autowired
    private AdminUserDetailsService adminUserDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Extract the token from the request header
        String token = getJWTFromRequest(request);

        // Validate the token
        if(StringUtils.hasText(token) && tokenGenerator.validateToken(token)) {

            // Extract the username and user type from the token
            Claims claims = tokenGenerator.getClaims(token);
            String username = claims.getSubject();
            String userType = claims.get("type", String.class);

            // Load the user details based on the user type
            UserDetails userDetails = null;
            if(userType.equals(UserTypes.ADMIN.getType())) {
                userDetails = adminUserDetailsService.loadUserByUsername(username);
            } else if(userType.equals(UserTypes.EMPLOYEE.getType())) {
                userDetails = employeeUserDetailsService.loadUserByUsername(username);
            } else {
                throw new RuntimeException("Invalid user type");
            }

            // Store the user details in the security context to mark the user as authenticated
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null,
                    userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        // Pass the request to the next filter in the filter chain
        // Note, if the token is invalid, nothing will be stored in the security context,
        // and therefore the security filter chain will consider the user unauthenticated.
        // we don't need to explicitly return a 401 error here
        filterChain.doFilter(request, response);
    }

    // A helper method to extract the JWT token from the request header
    private String getJWTFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
