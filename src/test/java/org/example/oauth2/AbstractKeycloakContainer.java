package org.example.oauth2;

import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.file.Paths;

//TODO change to GenericContainer
@Testcontainers
abstract class AbstractKeycloakContainer {
    
    protected static final FixedHostPortGenericContainer KEYCLOAK;
    protected static final String KEYCLOAK_URL;
    
    private static final int HOST_PORT = 8180;
    private static final int KEYCLOAK_PORT = 8080;
    private static final String REALM_IMPORT_PATH = Paths.get("src", "test", "resources", "realm.json").toAbsolutePath().toString();
    
    static {
        KEYCLOAK = new FixedHostPortGenericContainer<>("quay.io/keycloak/keycloak:22.0.1")
                .withFixedExposedPort(HOST_PORT, KEYCLOAK_PORT)
                .withEnv("KEYCLOAK_ADMIN", "admin")
                .withEnv("KEYCLOAK_ADMIN_PASSWORD", "admin")
                .withFileSystemBind(REALM_IMPORT_PATH, "/opt/keycloak/data/import/realm.json")
                .withCommand("start-dev", "--import-realm")
                .waitingFor(Wait.forHttp("/admin").forStatusCode(200));
        KEYCLOAK.start();
        KEYCLOAK_URL = "http://" + KEYCLOAK.getHost() + ":" + HOST_PORT;
    }
}
