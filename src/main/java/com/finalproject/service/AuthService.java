package com.finalproject.service;

import com.finalproject.model.UserCredentialsDto;
import com.finalproject.model.UserDto;
import org.keycloak.representations.AccessTokenResponse;


public interface AuthService {
    AccessTokenResponse login(UserCredentialsDto dto);

    void logout(String refreshToken);

    AccessTokenResponse refreshToken(String refreshToken);

    UserDto validateToken(String token);

    String getUserId(String token);
}
