package top.moma.zoffy.oauth.as.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "keycloak.server")
public class KeycloakServerProperties {
  String contextPath = "/auth";

  String realmImportFile = "zoffy-realm.json";

  AdminUser adminUser = new AdminUser();

  @Getter
  @Setter
  public static class AdminUser {
    String username = "admin";
    String password = "admin";
  }
}
