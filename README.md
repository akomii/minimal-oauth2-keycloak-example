## minimal-oauth2-keycloak-example ![Java 17](https://img.shields.io/badge/Java-17-green) ![Spring Boot 3.0.6](https://img.shields.io/badge/Spring--Boot-3.0.6-green)

This is a minimalist OAuth2 server built with Spring Boot and integrated with Keycloak. It allows users to securely access its features by using Keycloak as an _identity provider_. Keycloak verifies user identities and is responsible for managing user roles and groups.

Access to secure resources is redirected to Keycloak for authentication, while authorization is handled within the application.

It's important to note that this application acts as an OAuth2 client, not an OAuth2 resource server. It doesn't manage resources in terms of data or functionality; instead, it serves static HTML pages (which are session-secured).

### Configuration

The `application.yml` file is the configuration file for this OAuth2 client application. It contains settings related to security, authentication, and other application-specific configurations. Below is an overview of the key sections within this file:

```
server
  port: Specifies the port on which the application will run.

spring
  security:
    oauth2:
      client: Configuration related to OAuth2 client registration.
        registration: Information about the OAuth2 client registration.
          my-oauth2-client: A unique identifier for the OAuth2 client registration.
            client-id: The client ID assigned to this client by the OAuth2 authorization server.
            authorization-grant-type: The type of authorization grant to be used (e.g., authorization_code).
            scope: The OAuth2 scopes required for access (e.g., openid).
            redirect-uri: The URI to which the authorization server will redirect after authentication.
        provider: Configuration for the OAuth2 provider.
          my-oauth2-client: Configuration specific to the previously defined client.
            logout-url: The URL to which the application should redirect for logout.
            issuer-uri: The URI of the OAuth2 identity provider (Keycloak in this case).
            user-name-attribute: The attribute in the JWT token that represents the user's preferred username.
            authorities: Additional claims represented in the JWT token.
              roles: The attribute/attributes chain in the JWT token that represents the user's roles.
logging
  level: Specifies the logging level for the application, specifically for Spring Security in this case.
```

### Deployment

#### Prerequisites

1. [Java Development Kit (JDK) 17](https://jdk.java.net/17/) - Download and install the JDK.
2. [Apache Maven](https://maven.apache.org/download.cgi) - Download and install the Maven build tool.
3. [Keycloak](https://www.keycloak.org/) - Ensure that Keycloak is set up and running.
4. Keycloak Realm and Client - Configure the Keycloak Realm and Client according to your application's requirements. Alternatively, load and use the configuration file from `/test/resources/realm.json`.

#### Deployment Steps

1. **Download the project**: Obtain the project folder. This folder should contain the `pom.xml` file and the project's source code.


2. **Configure application.yml**: Edit the `application.yml` file to configure your application's settings. Ensure that the Keycloak configuration (client ID, issuer URI, etc.) matches your Keycloak setup.


3. **Build the project**: Run the following command to build and package the project: `mvn clean package`. This command will download the required dependencies, compile the project, and package it into an executable JAR file. The final JAR file will be located in the directory `target`, and its name will be `oauth2-1.0-SNAPSHOT.jar`.


4. **Run the application**: Execute the following command: `java -jar target/oauth2-1.0-SNAPSHOT.jar`. This will start the Spring Boot application, and it will be accessible locally.


5. **Access the application**: Once the application is up and running, you can access it by opening a web browser and navigating to: `http://localhost:8080/home`. Ensure that the port matches the one specified in your `application.yml` file.


6. **Authenticate via Keycloak**: Your application will redirect you to the Keycloak login page for authentication. Log in using the credentials you've configured in Keycloak. The default credentials for the user _John_ are `john:john` and for the user _Admin_ `admin:admin`.


7. **Access Protected Resources**: After successful authentication, you can access the protected endpoints based on the roles and permissions granted through Keycloak.
