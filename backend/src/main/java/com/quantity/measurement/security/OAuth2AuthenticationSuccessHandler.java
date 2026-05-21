package com.quantity.measurement.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    public OAuth2AuthenticationSuccessHandler(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        Object principal = authentication.getPrincipal();

        String email   = null;
        String name    = null;
        String picture = null;

        if (principal instanceof CustomOAuth2User customUser) {
            email   = customUser.getEmail();
            name    = customUser.getFullName();
            picture = customUser.getPicture();
        } else if (principal instanceof OAuth2User oAuth2User) {
            Map<String, Object> attributes = oAuth2User.getAttributes();
            email   = (String) attributes.get("email");
            name    = (String) attributes.get("name");
            picture = (String) attributes.get("picture");
        }

        if (email == null) {
            response.setStatus(500);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Could not extract email\"}");
            return;
        }

        String token = jwtTokenProvider.createToken(email, name, picture);

        // ── REDIRECT TARGET FIXED FOR REACT VITE (5173) ───────────────────
        // Port 3001 ko complete clean format me badal kar 5173 set kar diya hai
        String redirectUrl = "http://localhost:5173?token=" + token
                + "&email="   + URLEncoder.encode(email,                          StandardCharsets.UTF_8)
                + "&name="    + URLEncoder.encode(name    != null ? name    : "", StandardCharsets.UTF_8)
                + "&picture=" + URLEncoder.encode(picture != null ? picture : "", StandardCharsets.UTF_8);

        response.sendRedirect(redirectUrl);
    }
}