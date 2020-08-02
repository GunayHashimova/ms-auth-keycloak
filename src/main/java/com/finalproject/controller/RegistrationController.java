package com.finalproject.controller;

import com.finalproject.model.UserCredentialsDto;
import com.finalproject.service.RegistrationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${ms.root.url}/auth")
@CrossOrigin
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("public/user")
    public void createUser(@RequestBody UserCredentialsDto dto) {
        registrationService.createUser(dto);
    }

    @PostMapping("protected/reset-password")
    public void resetPassword(@RequestBody UserCredentialsDto dto) {
        registrationService.resetPassword(dto);
    }
}
