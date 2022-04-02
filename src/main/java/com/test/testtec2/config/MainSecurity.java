package com.test.testtec2.config;



import java.util.Arrays;

import com.test.testtec2.jwt.JwtFilter;
import com.test.testtec2.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;



@Configuration
@EnableWebSecurity
public class MainSecurity extends WebSecurityConfigurerAdapter {
    @Autowired
    private    UserService   userService;

    @Autowired
    private JwtFilter jwtFilter;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService); //.passwordEncoder(passwordEncoder());
    }

    @Bean(name =  BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
      CorsConfiguration configuration = new CorsConfiguration();
      configuration.setAllowedOrigins(Arrays.asList("https://localhost:8080/"));
      configuration.setAllowedMethods(Arrays.asList("GET","POST","POST", "PUT", "DELETE"));
      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
      return source;
    }

    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        

        http.cors().and().csrf().disable().authorizeRequests()
.antMatchers(HttpMethod.POST, "/authenticate").permitAll()
.anyRequest().authenticated()
.and()
.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //http.cors().configurationSource(corsConfigurationSource.get());
        //http.cors().configurationSource(request -> corsConfigurationSource().getCorsConfiguration(request));
        //http.csrf().disable();

        //http.authorizeRequests()            .antMatchers(HttpMethod.POST, "/authenticate").permitAll();
            //.anyRequest().authenticated();

       // http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

             http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        
    }

 

    /*@Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://goocle.com"));
        configuration.setAllowedMethods(Arrays.asList("HEAD","GET", "POST", "PUT", "DELETE"));
        // setAllowCredentials(true) is important, otherwise:
        // The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*' when the request's credentials mode is 'include'.
        configuration.setAllowCredentials(true);
        // setAllowedHeaders is important! Without it, OPTIONS preflight request
        // will fail with 403 Invalid CORS request
        //configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }*/
    

}
