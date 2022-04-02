package com.test.testtec2.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.test.testtec2.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.beans.factory.annotation.Value;



@Component
public class JwtFilter extends OncePerRequestFilter {

    private final static Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    @Autowired
    private JwtUtility jwtutility;

    @Autowired
    private UserService userservice;
    @Value("${jwt.secret}")

    private String secret;
    private String bearer =  "Bearer ";
    private String autorization =  "Authorization" ;

    private String getToken(HttpServletRequest request) {

        String authorizationHeader = request.getHeader(this.autorization);

        if(authorizationHeader != null && authorizationHeader.startsWith(this.bearer)){
            return   authorizationHeader.substring(this.bearer.length());  
         }

        return null;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response
                , FilterChain filterChain) throws ServletException, IOException {

        try{

            String username = "";
            String token = getToken(request);

            if(token!=null && jwtutility.validateTokenEstructure(token)){
                username = jwtutility.getUserNameFromToken(token);
            }
            
            if(username != null &&  SecurityContextHolder.getContext().getAuthentication()==null){
            
                UserDetails userDetails =  userservice.loadUserByUsername(username);

                if(jwtutility.validateToken(token, userDetails)){
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = 
                            new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails( new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }

        }catch(Exception e){
            
            logger.error("Error de filtrar Request: " + e.getMessage() );
        }

            filterChain.doFilter(request, response);
        
    }
    
}
