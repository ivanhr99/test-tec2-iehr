package com.test.testtec2.controller;

import com.test.testtec2.jwt.JwtRequest;
import com.test.testtec2.jwt.JwtResponse;
import com.test.testtec2.jwt.JwtUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class AuthenticateController {

    private final static Logger logger = LoggerFactory.getLogger(AuthenticateController.class);

    @Autowired
    private JwtUtility jwtUtility;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/authenticate")
    public JwtResponse authenticate(@RequestBody JwtRequest jwtRequest) throws Exception{

        logger.error("las  credencial: " + jwtRequest.getUsername() + " y  "+  jwtRequest.getPassword());
        
        try {
            
            authenticationManager
            .authenticate( new UsernamePasswordAuthenticationToken(
                jwtRequest.getUsername(), 
                jwtRequest.getPassword())
                );
        } catch (Exception e) {
            logger.error("Fallo el credencial: " + e.getMessage());
            throw new Exception("INVALID_CREDENTIALS", e);
        }

        //final UserDetails userDetails =  userDetailsService.loadUserByUsername(jwtRequest.getUsername());
        logger.error("a generar el token con  " + jwtRequest.getUsername() );
        final String token = jwtUtility.generateTokenUserName(jwtRequest.getUsername());
        logger.error("el token con  " + token );
        return new JwtResponse(token);
    }
}
