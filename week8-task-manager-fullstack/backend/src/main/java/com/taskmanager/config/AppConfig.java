package com.taskmanager.config;
import com.taskmanager.security.JwtFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.*;
import java.util.List;
@Configuration
public class AppConfig {
  @Bean BCryptPasswordEncoder encoder(){return new BCryptPasswordEncoder();}
  @Bean SecurityFilterChain security(HttpSecurity h,JwtFilter jwt)throws Exception{return h.csrf(c->c.disable()).cors(c->{})
    .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    .authorizeHttpRequests(a->a.requestMatchers("/api/auth/**","/ws/**").permitAll().anyRequest().authenticated())
    .addFilterBefore(jwt,UsernamePasswordAuthenticationFilter.class).build();}
  @Bean CorsConfigurationSource cors(@Value("${app.cors.origin:http://localhost:5173}") String origin){
    CorsConfiguration c=new CorsConfiguration(); c.setAllowedOrigins(List.of(origin)); c.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
    c.setAllowedHeaders(List.of("*")); c.setAllowCredentials(true); UrlBasedCorsConfigurationSource s=new UrlBasedCorsConfigurationSource(); s.registerCorsConfiguration("/**",c); return s;
  }
}
