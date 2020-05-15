package com.hitachi.schedule.controller.component;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UriAccessService {

    public boolean canAccess(HttpServletRequest request, Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal == null) {
            return false;
        }

        if (authentication instanceof AnonymousAuthenticationToken) {
            //check if this uri can be access by anonymous
            return false;
        }


        Set<String> roles = authentication.getAuthorities()
                .stream()
                .map(e -> e.getAuthority())
                .collect(Collectors.toSet());
        String uri = request.getRequestURI();

        for (String s : roles) {
            if (uri.startsWith(s)) {
                return true;
            }
        }

        return false;
    }
}
