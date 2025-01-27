package com.example.keycloak.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nimbusds.jwt.SignedJWT;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/tokens")
public class TokenController {

    private final OAuth2AuthorizedClientService authorizedClientService;

    public TokenController(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    @GetMapping
    public Map<String, String> getTokens(OAuth2AuthenticationToken authentication) {
        Map<String, String> tokens = new HashMap<>();

        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        tokens.put("ID Token", oidcUser.getIdToken().getTokenValue());
        tokens.put("Access Token", oidcUser.getIdToken().getTokenValue());

        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                authentication.getAuthorizedClientRegistrationId(),
                authentication.getName()
        );

        if (client != null) {
            tokens.put("Refresh Token", client.getRefreshToken() != null
                    ? client.getRefreshToken().getTokenValue()
                    : "Refresh Token отсутствует");
        } else {
            tokens.put("Refresh Token", "OAuth2AuthorizedClient не найден");
        }

        return tokens;
    }



    @GetMapping("/validate-id-token")
    public ResponseEntity<Map<String, Object>> validateIdToken(OAuth2AuthenticationToken authentication) throws Exception {
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        String idToken = oidcUser.getIdToken().getTokenValue();

        SignedJWT signedJWT = SignedJWT.parse(idToken);
        Map<String, Object> claims = signedJWT.getJWTClaimsSet().getClaims();

        return ResponseEntity.ok(claims);
    }

}

