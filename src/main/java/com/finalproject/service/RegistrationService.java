package com.finalproject.service;

import com.finalproject.model.UserCredentialsDto;

public interface RegistrationService {
    void createUser(UserCredentialsDto dto);

    void resetPassword(UserCredentialsDto dto);
}
