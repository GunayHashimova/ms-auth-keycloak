package com.finalproject.client;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.authorization.client.representation.TokenIntrospectionResponse;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class KeycloakClient {
    private String authUrl;
    private String realm;
    private RestTemplate restTemplate;
    private String adminSecret;

    public KeycloakClient(@Value("${keycloak.auth-server-url}") String authUrl,
                          @Value("${keycloak.realm}") String realm,
                          @Value("${custom.credentials.admin-secret}") String adminSecret) {
        this.authUrl = authUrl;
        this.realm = realm;
        this.adminSecret = adminSecret;
        restTemplate = new RestTemplate();
    }

    public AccessTokenResponse getTokens(MultiValueMap<String,String> urlParameters){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String url = String.format("%s/realms/%s/protocol/openid-connect/token", authUrl, realm);


        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(urlParameters, headers);

        ResponseEntity<AccessTokenResponse> response =
                restTemplate.postForEntity(url, request, AccessTokenResponse.class);
        return response.getBody();
    }

    public void logout(MultiValueMap<String, String> urlParameters) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String url = String.format("%s/realms/%s/protocol/openid-connect/logout", authUrl, realm);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(urlParameters, headers);

        ResponseEntity<Void> response =
                restTemplate.postForEntity(url, request, Void.class);
    }

    public TokenIntrospectionResponse validateToken(MultiValueMap<String, String> urlParameters) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String url = String.format("%s/realms/%s/protocol/openid-connect/token/introspect", authUrl, realm);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(urlParameters, headers);

        ResponseEntity<TokenIntrospectionResponse> response =
                restTemplate.postForEntity(url, request, TokenIntrospectionResponse.class);
        return response.getBody();
    }

    public UsersResource getKeycloakUserResource() {

        Keycloak kc = KeycloakBuilder.builder()
                .serverUrl(authUrl)
                .realm("master")
                .clientSecret(adminSecret)
                .username("admin")
                .password("admin")
                .clientId("admin-cli")
                .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build())
                .build();

        return kc.realm(realm).users();
    }

    public UsersResource getKeycloakLWMUserResource() {

        Keycloak kc = KeycloakBuilder.builder()
                .serverUrl(authUrl)
                .realm(realm)
                .clientSecret(adminSecret)
                .clientSecret("33b02e77-d592-47c7-a9c1-09bdeb4d68da")
                .clientId("lwm-app")
                .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build())
                .build();

        return kc.realm(realm).users();
    }
}
