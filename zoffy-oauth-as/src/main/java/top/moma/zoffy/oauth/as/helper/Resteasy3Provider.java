package top.moma.zoffy.oauth.as.helper;

import org.jboss.resteasy.core.ResteasyContext;
import org.jboss.resteasy.spi.Dispatcher;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.keycloak.common.util.ResteasyProvider;

public class Resteasy3Provider implements ResteasyProvider {
  @Override
  public <R> R getContextData(Class<R> aClass) {
    return ResteasyProviderFactory.getInstance().getContextData(aClass);
  }

  @Override
  public void pushDefaultContextObject(Class aClass, Object o) {
    ResteasyProviderFactory.getInstance()
        .getContextData(Dispatcher.class)
        .getDefaultContextObjects()
        .put(aClass, o);
  }

  @Override
  public void pushContext(Class aClass, Object o) {
    ResteasyContext.pushContext(aClass, o);
  }

  @Override
  public void clearContextData() {
    ResteasyContext.clearContextData();
  }
}
