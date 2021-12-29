package net.proselyte.jwtappdemo.config;

import net.proselyte.jwtappdemo.exception.SecurityExceptionResolver;
import net.proselyte.jwtappdemo.exception.securityException.CustomAuthenticationFailureHandler;
import net.proselyte.jwtappdemo.security.jwt.AccessDeniedHandlerJwt;
import net.proselyte.jwtappdemo.security.jwt.JwtConfigurer;
import net.proselyte.jwtappdemo.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;
    private final SecurityExceptionResolver securityExceptionResolver;
    private final AccessDeniedHandlerJwt accessDeniedHandlerJwt;

    private static final String LOGIN_ENDPOINT = "/api/v1/auth/login";

    @Autowired
    public SecurityConfig(JwtTokenProvider jwtTokenProvider, SecurityExceptionResolver securityExceptionResolver, AccessDeniedHandlerJwt accessDeniedHandlerJwt) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.securityExceptionResolver = securityExceptionResolver;
        this.accessDeniedHandlerJwt = accessDeniedHandlerJwt;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .formLogin().disable()
                .exceptionHandling().authenticationEntryPoint(securityExceptionResolver)
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandlerJwt)
                .and()
                .authorizeRequests()
                .antMatchers(LOGIN_ENDPOINT).permitAll()
                .antMatchers(HttpMethod.GET,"/**").permitAll()
                .antMatchers(HttpMethod.POST,"/characters/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST,"/comics/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST,"/characters/**").hasRole("USER")
                .antMatchers(HttpMethod.POST,"/comics/**").hasRole("USER")
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider));
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }
}
