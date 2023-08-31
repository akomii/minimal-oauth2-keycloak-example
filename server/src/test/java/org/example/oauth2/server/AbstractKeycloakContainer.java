package org.example.oauth2.server;

import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

//TODO change to GenericContainer
@Testcontainers
abstract class AbstractKeycloakContainer {
    
    protected static final FixedHostPortGenericContainer KEYCLOAK;
    protected static final String KEYCLOAK_URL;
    
    protected static final String DEFAULT_REALM_NAME = "oauth2-demo-realm";
    protected static final String DEFAULT_CLIENT_NAME = "oauth2-demo-client";
    protected static final String DEFAULT_ADMIN_NAME = "admin";
    protected static final String DEFAULT_ADMIN_PASSWORD = "password";
    
    private static final int HOST_PORT = 8180;
    private static final int KEYCLOAK_PORT = 8080;
    private static final String REALM_IMPORT_PATH = Paths.get("src", "test", "resources", "realm.json").toAbsolutePath().toString();
    
    static {
        KEYCLOAK = initKeycloakContainer();
        KEYCLOAK.start();
        KEYCLOAK_URL = buildKeycloakUrl();
    }
    
    private static FixedHostPortGenericContainer initKeycloakContainer() {
        String version = getKeycloakVersionFromProperties();
        return new FixedHostPortGenericContainer<>("quay.io/keycloak/keycloak:" + version)
                .withFixedExposedPort(HOST_PORT, KEYCLOAK_PORT)
                .withEnv("KEYCLOAK_ADMIN", DEFAULT_ADMIN_NAME)
                .withEnv("KEYCLOAK_ADMIN_PASSWORD", DEFAULT_ADMIN_PASSWORD)
                .withFileSystemBind(REALM_IMPORT_PATH, "/opt/keycloak/data/import/realm.json")
                .withCommand("start-dev", "--import-realm")
                .waitingFor(Wait.forHttp("/admin").forStatusCode(200));
    }
    
    private static String getKeycloakVersionFromProperties() {
        try {
            Properties props = new Properties();
            props.load(AbstractKeycloakContainer.class.getClassLoader().getResourceAsStream("project.properties"));
            return props.getProperty("keycloak.version");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private static String buildKeycloakUrl() {
        return "http://" + KEYCLOAK.getHost() + ":" + HOST_PORT;
    }
}
