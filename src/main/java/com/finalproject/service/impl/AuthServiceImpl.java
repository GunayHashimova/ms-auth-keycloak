package com.finalproject.service.impl;

import com.finalproject.client.KeycloakClient;
import com.finalproject.mapper.AuthMapper;
import com.finalproject.model.UserCredentialsDto;
import com.finalproject.model.UserDto;
import com.finalproject.model.exception.AuthException;
import com.finalproject.service.AuthService;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.keycloak.authorization.client.representation.TokenIntrospectionResponse;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthMapper authMapper;
    private final KeycloakClient keycloakClient;

    public AuthServiceImpl(AuthMapper authMapper, KeycloakClient keycloakClient) {
        this.authMapper = authMapper;
        this.keycloakClient = keycloakClient;
    }


    @Override
    public AccessTokenResponse login(UserCredentialsDto dto) {
        return keycloakClient.getTokens(authMapper.buildLoginRequest(dto));
    }

    @Override
    public void logout(String refreshToken) {
        keycloakClient.logout(authMapper.buildLogoutRequest(refreshToken));
    }


    @Override
    public AccessTokenResponse refreshToken(String refreshToken) {
        return keycloakClient.getTokens(authMapper.buildRefreshTokenRequest(refreshToken));
    }

    @Override
    public UserDto validateToken(String token) {
        TokenIntrospectionResponse response =
                keycloakClient.validateToken(authMapper.buildTokenIntrospectRequest(token));

        if (!response.getActive()) {
            throw new AuthException("invalid-token");
        }

        SignedJWT signedJWT;
        JWTClaimsSet jwtClaimsSet;
        try {
            signedJWT = SignedJWT.parse(token);
            jwtClaimsSet = signedJWT.getJWTClaimsSet();
            return new UserDto(jwtClaimsSet.getSubject(), jwtClaimsSet.getStringClaim("customerId"));
        } catch (ParseException e) {
            throw new AuthException("token-can-not-be-parsed");
        }
    }

    @Override
    public String getUserId(String token) {
        TokenIntrospectionResponse response =
                keycloakClient.validateToken(authMapper.buildTokenIntrospectRequest(token));

        if (!response.getActive()) {
            throw new AuthException("invalid-token");
        }

        SignedJWT signedJWT;
        JWTClaimsSet jwtClaimsSet;
        try {
            signedJWT = SignedJWT.parse(token);
            jwtClaimsSet = signedJWT.getJWTClaimsSet();
            return jwtClaimsSet.getSubject();
        } catch (ParseException e) {
            throw new AuthException("token-can-not-be-parsed");
        }
    }


}
