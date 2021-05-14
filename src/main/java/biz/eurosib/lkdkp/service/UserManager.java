package biz.eurosib.lkdkp.service;

import biz.eurosib.lkdkp.keycloak.KeyCloakConfig;
import biz.eurosib.lkdkp.keycloak.UserDto;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserManager {
    private Keycloak keycloak;

    @Autowired
    public UserManager (KeyCloakConfig config) {
        this.keycloak = KeycloakBuilder.builder()
                .serverUrl(config.getServerUrl())
                .realm(config.getRealm())
                .username(config.getUsername())
                .password(config.getPassword())
                .clientId(config.getClientId())
                .clientSecret(config.getClientSecret())
                .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(config.getPoolSize()).build())
                .build();
    }

    public void getAccessTokenExample() {
        AccessTokenResponse token = keycloak.tokenManager().getAccessToken();
        System.out.print(token.getToken());


        System.out.print("User creation...");
        UserRepresentation user = new UserRepresentation();

        user.setUsername("tester1");
        user.setFirstName("First");
        user.setLastName("Last");
        user.setEmail("tom+tester1@tdlabs.local");
        user.setAttributes(Collections.singletonMap("origin", Arrays.asList("demo")));

        var realmResource = keycloak.realm("");
        var userResource = realmResource.users();

        var response = userResource.create(user);
        System.out.println("Response: " + response.getStatusInfo());
        System.out.println(response.getLocation());
    }

    public void createUser(UserDto userDto, /*UUID*/String guid) {
        AccessTokenResponse token = keycloak.tokenManager().getAccessToken();
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userDto.getEmail());
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFio());
        user.setLastName(userDto.getFio());
        HashMap<String, List<String>> attributes = new HashMap<>();
        attributes.put("guid", Arrays.asList(guid));
        attributes.put("phone", Arrays.asList(userDto.getPhone()));
        attributes.put("type", Arrays.asList(userDto.getType()));
        attributes.put("inn", Arrays.asList(userDto.getInn()));
        attributes.put("kpp", Arrays.asList(userDto.getKpp()));
        attributes.put("ogrn", Arrays.asList(userDto.getOgrn()));
        user.setAttributes(attributes);

        var realmResource = keycloak.realm("");
        var userResource = realmResource.users();

        var response = userResource.create(user);
        System.out.println("Response: " + response.getStatusInfo());
        System.out.println(response.getLocation());
    }
}
