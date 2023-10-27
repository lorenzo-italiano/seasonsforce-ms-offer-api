package fr.polytech.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

public class BearerTokenInterceptor extends RequestHeaderAuthenticationFilter {
    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        // Récupérez le Bearer Token de l'en-tête de la demande
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Pour enlever le préfixe "Bearer "
        }
        return null;
    }
}
