package kz.beeset.med.gateway2.config;

import kz.beeset.med.gateway2.security.filter.AuthenticationTokenFilter;
import kz.beeset.med.gateway2.security.service.TokenAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final TokenAuthenticationService tokenAuthenticationService;

    @Autowired
    protected SecurityConfig(final TokenAuthenticationService tokenAuthenticationService){
        super();
        this.tokenAuthenticationService = tokenAuthenticationService;
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/med/login").permitAll()
                .antMatchers("/med/user/color/read").permitAll()
                .antMatchers("/med/forgotPassword/**").permitAll()
                .antMatchers("/med/access_denied").permitAll()
                .antMatchers("/med/signup/**").permitAll()
                .antMatchers("/med/swagger-ui.html").permitAll()
                .antMatchers("/med/med-ki/m/reg/doc/**").permitAll()
                .antMatchers("/med/reset/**").permitAll()
                //.anyRequest().permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new AuthenticationTokenFilter(tokenAuthenticationService), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable();
    }
}