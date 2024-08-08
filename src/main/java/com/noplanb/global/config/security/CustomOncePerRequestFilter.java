package com.noplanb.global.config.security;

import com.noplanb.global.config.security.util.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.util.HashMap;

@Component
public class CustomOncePerRequestFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtParser jwtParser;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${app.auth.tokenSecret}")
    private String jwtSecret;

    public CustomOncePerRequestFilter(UserDetailsService userDetailsService, @Value("${app.auth.tokenSecret}") String jwtSecret) {
        this.userDetailsService = userDetailsService;
        this.jwtSecret = jwtSecret;
        Key signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        this.jwtParser = Jwts.parserBuilder().setSigningKey(signingKey).build(); // Initialize JwtParser once
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String jwt = getJwtFromRequest(request);

        if (jwt != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            String username = null;
            try {
                username = jwtTokenUtil.getUsernameFromJWT(jwt);
            } catch (ExpiredJwtException ex) {
                String refreshToken = getRefreshTokenFromRequest(request);
                if (refreshToken != null && jwtTokenUtil.validateRefreshToken(refreshToken)) {
                    String newAccessToken = jwtTokenUtil.generateToken(new HashMap<>(), jwtTokenUtil.getUsernameFromRefreshToken(refreshToken));
                    response.setHeader("Authorization", "Bearer " + newAccessToken);
                    username = jwtTokenUtil.getUsernameFromJWT(newAccessToken);
                }
            }

            if (username != null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (jwtTokenUtil.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private String getRefreshTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Refresh-Token");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
