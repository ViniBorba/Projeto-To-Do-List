package com.example.demo.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService uds;

    public JwtAuthFilter(JwtService jwtService, CustomUserDetailsService uds) {
        this.jwtService = jwtService;
        this.uds = uds;
    }

// 
private static final Pattern BEARER = Pattern.compile("^Bearer\\s+(.+)$", Pattern.CASE_INSENSITIVE);

@Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws ServletException, IOException {

    String header = request.getHeader("Authorization");
    System.out.println("header=" + header);

    String token = null;
    if (header != null) {
        Matcher m = BEARER.matcher(header.trim());
        if (m.find()) {
            token = m.group(1).trim();  // extrai só o token
        }
    }

    if (token != null && !token.isBlank()) {
        try {
            var maybeUser = jwtService.validateAndGetUsername(token);

            if (maybeUser.isPresent() && SecurityContextHolder.getContext().getAuthentication() == null) {
                String username = maybeUser.get();
                System.out.println("JWT OK. subject=" + username);

                UserDetails ud = uds.loadUserByUsername(username);
                var auth = new UsernamePasswordAuthenticationToken(ud, null, ud.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
                System.out.println("Auth set: " + SecurityContextHolder.getContext().getAuthentication());
                System.out.println("Authorities: " + SecurityContextHolder.getContext().getAuthentication().getAuthorities());
            } else {
                System.out.println("JWT inválido ou expirado");
            }
        } catch (Exception e) {
            // e.printStackTrace(); // opcional para diagnosticar
            System.out.println("Erro ao processar token: " + e.getMessage());
        }
    }

    chain.doFilter(request, response);
}





}
