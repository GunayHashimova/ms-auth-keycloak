package com.finalproject.mapper;


import com.finalproject.model.UserCredentialsDto;
import com.finalproject.model.UserDto;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static com.finalproject.model.Headers.*;

@Component
public class AuthMapper {
    private final String clientId;
    private final String secretKey;

    public AuthMapper(@Value("${keycloak.resource}") String clientId,
                      @Value("${keycloak.credentials.secret}") String secretKey) {
        this.clientId = clientId;
        this.secretKey = secretKey;
    }

    public MultiValueMap<String,String> buildLoginRequest(UserCredentialsDto dto){
        MultiValueMap<String, String> urlParameters = new LinkedMultiValueMap<>();
        urlParameters.add("grant_type", "password");
        urlParameters.add("client_id", clientId);
        urlParameters.add("username", dto.getUsername());
        urlParameters.add("password", dto.getPassword());
        urlParameters.add("client_secret", secretKey);

        return urlParameters;
    }

    public MultiValueMap<String, String> buildLogoutRequest(String refreshToken) {
        MultiValueMap<String, String> urlParameters = new LinkedMultiValueMap<>();
        urlParameters.add("refresh_token", refreshToken);
        urlParameters.add("client_id", clientId);
        urlParameters.add("client_secret", secretKey);
        return urlParameters;
    }

    public MultiValueMap<String, String> buildRefreshTokenRequest(String refreshToken) {
        MultiValueMap<String, String> urlParameters = new LinkedMultiValueMap<>();
        urlParameters.add("grant_type", "refresh_token");
        urlParameters.add("client_id", clientId);
        urlParameters.add("refresh_token", refreshToken);
        urlParameters.add("client_secret", secretKey);

        return urlParameters;
    }

    public ResponseEntity<Void> buildTokenResponse(AccessTokenResponse response) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(LWM_ACCESS_TOKEN, response.getToken());
        headers.add(LWM_REFRESH_TOKEN, response.getRefreshToken());
        return ResponseEntity.ok().headers(headers).build();
    }

    public MultiValueMap<String, String> buildTokenIntrospectRequest(String refreshToken) {
        MultiValueMap<String, String> urlParameters = new LinkedMultiValueMap<>();
        urlParameters.add("token", refreshToken);
        urlParameters.add("client_id", clientId);
        urlParameters.add("client_secret", secretKey);

        return urlParameters;
    }

    public ResponseEntity<Void> buildValidateTokenResponse(UserDto userDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(LWM_CUSTOMER_ID, userDto.getCustomerId());
        headers.add(LWM_USER_ID, userDto.getUserId());
        return ResponseEntity.ok().headers(headers).build();
    }
}

