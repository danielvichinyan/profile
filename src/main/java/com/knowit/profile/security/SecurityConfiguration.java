package com.knowit.profile.security;

import com.knowit.profile.security.filters.AuthorizationHeaderFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final AuthorizationHeaderFilter authHeaderFilter;

    public SecurityConfiguration(AuthorizationHeaderFilter authHeaderFilter) {
        this.authHeaderFilter = authHeaderFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/**")
                .permitAll()
                .and()
                .addFilterBefore(this.authHeaderFilter, UsernamePasswordAuthenticationFilter.class);
        super.configure(http);
    }
}