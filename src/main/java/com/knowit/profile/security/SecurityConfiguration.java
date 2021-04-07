package com.knowit.profile.security;

import com.knowit.profile.security.filters.AuthorizationHeaderFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final AuthorizationHeaderFilter authorizationHeaderFilter;

    public SecurityConfiguration(AuthorizationHeaderFilter authorizationHeaderFilter) {
        this.authorizationHeaderFilter = authorizationHeaderFilter;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/**")
                .permitAll()
                .and()
                .addFilterBefore(this.authorizationHeaderFilter, UsernamePasswordAuthenticationFilter.class);
        super.configure(httpSecurity);
    }
}
