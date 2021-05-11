//package com.nappster.serviceauth.security;
//
//import com.nappster.serviceauth.util.AuthUserDetailsService;
//import com.nappster.serviceauth.util.UtilsService;
//import java.io.IOException;
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//@Component
//public class RequestFilter extends OncePerRequestFilter {
//
//    @Autowired
//    private UtilsService utilsService;
//
//    @Autowired
//    private AuthUserDetailsService authUserDetailsService;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
//            throws ServletException, IOException {
//        final String requestTokenHeader = request.getHeader("Authorization");
//        String username = null;
//        String token;
//        String[] credentials = null;
//
//        if (requestTokenHeader != null && requestTokenHeader.startsWith("Basic ")) {
//            token = requestTokenHeader;
//
//            credentials = utilsService.extractCredentials(token);
//
//            if (credentials != null) {
//                username = credentials[0];
//            }
//        } else {
//            logger.warn("Token does not begin with Basic String");
//        }
//        // Once we get the token validate it.
//        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            UserDetails userDetails = this.authUserDetailsService.loadUserByUsername(username);
//            if (userDetails != null && userDetails.getPassword().equals(credentials[1])) {
//                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
//                        userDetails, null, userDetails.getAuthorities());
//                usernamePasswordAuthenticationToken
//                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                // After setting the Authentication in the context, we specify
//                // that the current user is authenticated. So it passes the
//                // Spring Security Configurations successfully.
//                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//            }
//        }
//        chain.doFilter(request, response);
//    }
//}
