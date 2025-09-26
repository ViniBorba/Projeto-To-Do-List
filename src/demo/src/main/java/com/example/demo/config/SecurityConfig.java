package com.example.demo.config;

import com.example.demo.auth.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.http.HttpMethod;

import java.util.List;

@Configuration
public class SecurityConfig {

  private final JwtAuthFilter jwtAuthFilter;
  public SecurityConfig(JwtAuthFilter jwtAuthFilter) { this.jwtAuthFilter = jwtAuthFilter; }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
http
  .csrf(cs -> cs.disable())             
  .cors(c -> c.configurationSource(corsConfigurationSource()))
  .authorizeHttpRequests(auth -> auth
    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
    .requestMatchers(
      "/api/auth/**",
      "/swagger-ui/**", "/swagger-ui.html", "/swagger/**",
      "/v3/api-docs/**",
      "/h2-console/**"
    ).permitAll()
    .requestMatchers("/api/tasks/**").authenticated() 
    .anyRequest().authenticated()
  )
  .headers(h -> h.frameOptions(f -> f.disable()))
  .httpBasic(h -> h.disable())
  .sessionManagement(sm -> sm.sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.STATELESS));

http.addFilterBefore(jwtAuthFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  // CORS liberando a origem do Angular
@Bean
public CorsConfigurationSource corsConfigurationSource() {
  var cfg = new CorsConfiguration();
  cfg.setAllowedOrigins(List.of("http://localhost:4200"));
  cfg.setAllowedMethods(List.of("GET","POST","PUT","DELETE","PATCH","OPTIONS")); //PATCH + OPTIONS
  cfg.setAllowedHeaders(List.of("Authorization","Content-Type","Accept","Origin","X-Requested-With"));
  cfg.setExposedHeaders(List.of("Authorization"));
  cfg.setAllowCredentials(true);

  var source = new UrlBasedCorsConfigurationSource();
  source.registerCorsConfiguration("/**", cfg);
  return source;
}

  @Bean public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
  @Bean public AuthenticationManager authenticationManager(AuthenticationConfiguration c) throws Exception {
    return c.getAuthenticationManager();
  }
}
