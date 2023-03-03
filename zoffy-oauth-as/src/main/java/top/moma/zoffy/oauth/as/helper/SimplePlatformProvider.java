package top.moma.zoffy.oauth.as.helper;

import org.keycloak.Config;
import org.keycloak.platform.PlatformProvider;
import org.keycloak.services.ServicesLogger;

import java.io.File;

public class SimplePlatformProvider implements PlatformProvider {

  Runnable shutdownHook;

  @Override
  public void onStartup(Runnable runnable) {
    runnable.run();
  }

  @Override
  public void onShutdown(Runnable runnable) {
    this.shutdownHook = runnable;
  }

  @Override
  public void exit(Throwable throwable) {
    ServicesLogger.LOGGER.fatal(throwable);
    new Thread(() -> System.exit(1)).start();
  }

  @Override
  public File getTmpDirectory() {
    return new File(System.getProperty("java.io.tmpdir"));
  }

  @Override
  public ClassLoader getScriptEngineClassLoader(Config.Scope scope) {
    return null;
  }
}
