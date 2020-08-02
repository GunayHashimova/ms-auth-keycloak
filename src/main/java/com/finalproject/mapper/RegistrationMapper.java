package com.finalproject.mapper;

import com.finalproject.model.UserCredentialsDto;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RegistrationMapper {

    public UserRepresentation buildUserRepresentation(UserCredentialsDto dto) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(dto.getUsername());

        List<CredentialRepresentation> credentialRepresentations = new ArrayList<>();
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType("password");
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setValue(dto.getPassword());
        credentialRepresentations.add(credentialRepresentation);

        userRepresentation.setCredentials(credentialRepresentations);
        userRepresentation.setEnabled(true);

//        Map<String, List<String>> attributes = new HashMap<>();
//        attributes.put("customerId", List.of(dto.getCustomerId()));
//        userRepresentation.setAttributes(attributes);

        return userRepresentation;
    }

    public CredentialRepresentation buildCredentialRepresentation(String password) {
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType("password");
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setValue(password);
        return credentialRepresentation;
    }
}
