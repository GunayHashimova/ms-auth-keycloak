package com.finalproject.controller;

import com.finalproject.mapper.AuthMapper;
import com.finalproject.model.UserCredentialsDto;
import com.finalproject.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.finalproject.model.Headers.LWM_ACCESS_TOKEN;
import static com.finalproject.model.Headers.LWM_REFRESH_TOKEN;

@RestController
@RequestMapping("${ms.root.url}/auth")
@CrossOrigin(exposedHeaders={LWM_ACCESS_TOKEN, LWM_REFRESH_TOKEN})
public class AuthController {
    private final AuthService authService;
    private final AuthMapper authMapper;


    public AuthController(AuthService authService, AuthMapper authMapper) {
        this.authService = authService;
        this.authMapper = authMapper;
    }

    @PostMapping("public/login")
    public ResponseEntity<Void> login(@RequestBody UserCredentialsDto dto) {
        return authMapper.buildTokenResponse(authService.login(dto));
    }

    @PostMapping("protected/logout")
    public void logout(@RequestHeader(LWM_REFRESH_TOKEN) String refreshToken) {
        authService.logout(refreshToken);
    }

    @PostMapping("public/refresh-token")
    public ResponseEntity<Void> refreshToken(@RequestHeader(LWM_REFRESH_TOKEN) String refreshToken) {
        return authMapper.buildTokenResponse(authService.refreshToken(refreshToken));
    }

    @GetMapping("private/validate-token")
    public ResponseEntity<Void> validateToken(@RequestHeader(LWM_ACCESS_TOKEN) String token) {
        return authMapper.buildValidateTokenResponse(authService.validateToken(token));
    }

    @GetMapping("private/get-userId")
    public String getUserId(@RequestHeader(LWM_ACCESS_TOKEN) String token) {
        return authService.getUserId(token);
    }
}
