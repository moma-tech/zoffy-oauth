package top.moma.zoffy.oauth.as.config;

import lombok.extern.slf4j.Slf4j;
import org.keycloak.Config;
import org.keycloak.exportimport.ExportImportManager;
import org.keycloak.models.KeycloakSession;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.services.managers.ApplianceBootstrap;
import org.keycloak.services.managers.RealmManager;
import org.keycloak.services.resources.KeycloakApplication;
import org.keycloak.services.util.JsonConfigProviderFactory;
import org.keycloak.util.JsonSerialization;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import top.moma.zoffy.oauth.as.helper.RegularJsonConfigProviderFactory;

import java.util.NoSuchElementException;

@Slf4j
public class EmbeddedKeycloakApplication extends KeycloakApplication {
  static KeycloakServerProperties keycloakServerProperties;

  protected void loadConfig() {
    JsonConfigProviderFactory jsonConfigProviderFactory = new RegularJsonConfigProviderFactory();
    Config.init(
        jsonConfigProviderFactory
            .create()
            .orElseThrow(() -> new NoSuchElementException("No Value Present")));
  }

  @Override
  protected ExportImportManager bootstrap() {
    final ExportImportManager exportImportManager = super.bootstrap();
    createMasterRealmAdminUser();
    createZoffyRealm();
    return exportImportManager;
  }

  private void createMasterRealmAdminUser() {
    KeycloakSession session = getSessionFactory().create();
    ApplianceBootstrap applianceBootstrap = new ApplianceBootstrap(session);
    KeycloakServerProperties.AdminUser admin = keycloakServerProperties.getAdminUser();
    try {
      session.getTransactionManager().begin();
      applianceBootstrap.createMasterRealmUser(admin.getUsername(), admin.getPassword());
      session.getTransactionManager().commit();
    } catch (Exception ex) {
      log.warn("Couldn't create keycloak master admin user: {}", ex.getMessage());
      session.getTransactionManager().rollback();
    }
    session.close();
  }

  private void createZoffyRealm() {
    KeycloakSession session = getSessionFactory().create();
    try {
      session.getTransactionManager().begin();
      RealmManager manager = new RealmManager(session);
      Resource lessonRealmImportFile =
          new ClassPathResource(keycloakServerProperties.getRealmImportFile());
      manager.importRealm(
          JsonSerialization.readValue(
              lessonRealmImportFile.getInputStream(), RealmRepresentation.class));
      session.getTransactionManager().commit();
    } catch (Exception ex) {
      log.warn("Failed to import Realm json file: {}", ex.getMessage());
      session.getTransactionManager().rollback();
    }
    session.close();
  }
}
