package com.knowit.profile.security.filters;

import com.knowit.profile.domain.entities.User;
import com.knowit.profile.exceptions.UserDoesNotExistException;
import com.knowit.profile.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Component
public class AuthorizationHeaderFilter extends OncePerRequestFilter {

    private static final String X_USER_ID = "X-User-Id";

    private final ProfileService profileService;

    @Autowired
    public AuthorizationHeaderFilter(ProfileService profileService) {
        this.profileService = profileService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String userId = httpServletRequest.getHeader(X_USER_ID);

        if (userId == null) {
            httpServletResponse.setStatus(403);
            throw new ServletException("User id is null!");
        }
        logger.info("+++++++||||||");
        logger.info(httpServletRequest.getHeader("X-User-Scopes"));
        logger.info("+++++++||||||");
        try {
            User user = this.profileService.fetchByUserId(userId);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    new ArrayList<>()
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (UserDoesNotExistException e) {
            logger.error(e.getStackTrace(), e);
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}