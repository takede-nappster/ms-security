package com.nappster.serviceauth.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] SWAGGER_WHITELIST = {
        // -- swagger ui
        "/v2/api-docs",
        "/swagger-resources",
        "/swagger-resources/**",
        "/configuration/ui",
        "/configuration/security",
        "/api/swagger.json",
        "/swagger-ui.html",
        "/webjars/**",
        "/api/user/**",
        "/marchands/**",
        "/marchands/login",
        "/api/user/reinitilizePassword/**",
        "/api/user/reinitilizePassword",
        "/api/user/reinitilizePassword/*",
        "/api/user/getkycinfos"
    // other public endpoints of your API may be appended to this array
    };

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        // To authorized OPTIONS request
        httpSecurity.cors();

        // We don't need CSRF for this example
        httpSecurity.csrf().disable()
                // dont authenticate this particular request
                .authorizeRequests().antMatchers(SWAGGER_WHITELIST).permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // all other requests need to be authenticated
                .anyRequest().permitAll();
        // Add a filter to validate the tokens with every request
//        httpSecurity.addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity.httpBasic().disable();
    }

}
