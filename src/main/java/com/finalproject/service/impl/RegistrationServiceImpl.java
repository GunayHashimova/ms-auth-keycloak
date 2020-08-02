package com.finalproject.service.impl;

import com.finalproject.client.KeycloakClient;
import com.finalproject.mapper.RegistrationMapper;
import com.finalproject.model.UserCredentialsDto;
import com.finalproject.service.RegistrationService;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.stereotype.Service;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final KeycloakClient keycloakClient;
    private final RegistrationMapper registrationMapper;

    public RegistrationServiceImpl(KeycloakClient keycloakClient,
                                   RegistrationMapper registrationMapper) {
        this.keycloakClient = keycloakClient;
        this.registrationMapper = registrationMapper;
    }

    @Override
    public void createUser(UserCredentialsDto dto) {
        UsersResource userResource = keycloakClient.getKeycloakUserResource();
        userResource.create(registrationMapper.buildUserRepresentation(dto));
    }

    @Override
    public void resetPassword(UserCredentialsDto dto) {

        UsersResource userResource = keycloakClient.getKeycloakUserResource();
        userResource.get(dto.getUserId())
                .resetPassword(registrationMapper.buildCredentialRepresentation(dto.getPassword()));
    }
}
