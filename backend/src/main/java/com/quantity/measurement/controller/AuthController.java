package com.quantity.measurement.controller;



import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    /**
     * GET /api/auth/login
     * Redirects browser to Google's OAuth2 consent screen.
     */
    @GetMapping("/login")
    public RedirectView login() {
        return new RedirectView("/oauth2/authorization/google");
    }

    /**
     * GET /api/auth/me
     * Returns the currently authenticated user's email from JWT.
     * Requires: Authorization: Bearer <token>
     */
   
    @GetMapping("/me")
    public Map<String, String> getCurrentUser(@AuthenticationPrincipal String email) {
        return Map.of(
                "email",   email,
                "status",  "authenticated");
        
    }
}