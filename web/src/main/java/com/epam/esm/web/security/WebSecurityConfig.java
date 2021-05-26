package com.epam.esm.web.security;

import com.epam.esm.service.service.user.UserService;
import com.epam.esm.web.exceptionhandler.RESTAuthenticationEntryPoint;
import com.epam.esm.web.exceptionhandler.RestAccessDeniedHandler;
import com.epam.esm.web.security.jwt.JwtTokenFilter;
import com.epam.esm.web.security.userdetails.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


import static java.lang.String.format;

@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final JwtTokenFilter jwtTokenFilter;

    @Autowired
    private RESTAuthenticationEntryPoint entryPoint;

    @Autowired
    private RestAccessDeniedHandler handler;

    public WebSecurityConfig(UserService userService,
                             JwtTokenFilter jwtTokenFilter) {
        super();

        this.userService = userService;
        this.jwtTokenFilter = jwtTokenFilter;

        // Inherit security context in async function calls
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(username -> userService.getByUniqueLoginOptional(username).map(Account::new)
                .orElseThrow(
                        () -> new UsernameNotFoundException(
                                format("User: %s, not found", username)
                        )
                ));
    }

    // Set password encoding schema
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Enable CORS and disable CSRF
        http = http.cors().and().csrf().disable();

        // Set session management to stateless
        http = http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and();

        // Set unauthorized requests exception handler
        http = http
                .exceptionHandling().accessDeniedHandler(handler)
                .authenticationEntryPoint(entryPoint)
                .and();

        // Set permissions on endpoints
        http.authorizeRequests()
                // Swagger endpoints must be publicly accessible
                .antMatchers("/").permitAll()
                // Our public endpoints
                .antMatchers(HttpMethod.GET,"/tags/**").permitAll()
                .antMatchers(HttpMethod.GET,"/certificates/**").permitAll()
                .antMatchers(HttpMethod.GET,"/users/**").permitAll()
                .antMatchers(HttpMethod.GET,"/orders/**").permitAll()
                .antMatchers("/login/**").permitAll()
                .antMatchers("/register/**").permitAll()
                // Our private endpoints
                .anyRequest().authenticated();

        // Add JWT token filter
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    // Used by spring security if CORS is enabled.
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    // Expose authentication manager bean
    @Override @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
