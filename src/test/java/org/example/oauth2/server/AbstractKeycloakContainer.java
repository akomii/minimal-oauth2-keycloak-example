package org.example.oauth2.server;

import java.nio.file.Paths;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

/**
 * The {@code AbstractKeycloakContainer} class serves as a base class for initializing a Keycloak
 * container for integration testing purposes. It uses Testcontainers to manage a Keycloak instance
 * with a predefined realm and client configuration.
 *
 * <p>This abstract class sets up a Keycloak container with the necessary configuration for testing.
 * It exposes static fields and methods for accessing the Keycloak instance and its URL.
 *
 * @see org.testcontainers.containers.FixedHostPortGenericContainer
 * @see <a href="https://www.testcontainers.org/" target="_blank">Testcontainers</a>
 */
abstract class AbstractKeycloakContainer {

  protected static final FixedHostPortGenericContainer KEYCLOAK;
  protected static final String KEYCLOAK_URL;

  // keycloak admin, not realm admin
  protected static final String DEFAULT_ADMIN_NAME = "admin";
  protected static final String DEFAULT_ADMIN_PASSWORD = "password";

  // analog to /resources/realm.json
  protected static final String DEFAULT_REALM_NAME = "oauth2-demo-realm";
  protected static final String DEFAULT_CLIENT_NAME = "oauth2-demo-client";

  // analog to pom.xml
  private static final String KEYCLOAK_VERISON = "22.0.1";

  private static final int HOST_PORT = 8180;
  private static final int KEYCLOAK_PORT = 8080;
  private static final String REALM_IMPORT_PATH =
      Paths.get("src", "test", "resources", "realm.json").toAbsolutePath().toString();

  static {
    KEYCLOAK = initKeycloakContainer();
    KEYCLOAK.start();
    KEYCLOAK_URL = buildKeycloakUrl();
    Runtime.getRuntime().addShutdownHook(new Thread(KEYCLOAK::stop));
  }

  private static FixedHostPortGenericContainer initKeycloakContainer() {
    return new FixedHostPortGenericContainer<>("quay.io/keycloak/keycloak:" + KEYCLOAK_VERISON)
        .withFixedExposedPort(HOST_PORT, KEYCLOAK_PORT)
        .withEnv("KEYCLOAK_ADMIN", DEFAULT_ADMIN_NAME)
        .withEnv("KEYCLOAK_ADMIN_PASSWORD", DEFAULT_ADMIN_PASSWORD)
        .withFileSystemBind(REALM_IMPORT_PATH, "/opt/keycloak/data/import/realm.json")
        .withCommand("start-dev", "--import-realm")
        .waitingFor(Wait.forHttp("/admin").forStatusCode(200));
  }

  private static String buildKeycloakUrl() {
    return "http://" + KEYCLOAK.getHost() + ":" + HOST_PORT;
  }
}
