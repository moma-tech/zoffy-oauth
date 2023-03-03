package top.moma.zoffy.oauth.as.filter;

import org.keycloak.common.ClientConnection;
import org.keycloak.services.filters.AbstractRequestFilter;
import top.moma.m64.core.constants.StringConstants;
import top.moma.m64.core.exceptions.M64Exception;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class EmbeddedKeycloakRequestFilter extends AbstractRequestFilter implements Filter {

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    Filter.super.init(filterConfig);
  }

  @Override
  public void doFilter(
      ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    servletRequest.setCharacterEncoding(StringConstants.UTF_8);
    ClientConnection clientConnection = createClientConnection((HttpServletRequest) servletRequest);
    filter(
        clientConnection,
        (keycloakSession -> {
          try {
            filterChain.doFilter(servletRequest, servletResponse);
          } catch (Exception e) {
            throw new M64Exception(e);
          }
        }));
  }

  @Override
  public void destroy() {
    Filter.super.destroy();
  }

  private ClientConnection createClientConnection(HttpServletRequest httpServletRequest) {
    return new ClientConnection() {
      @Override
      public String getRemoteAddr() {
        return httpServletRequest.getRemoteAddr();
      }

      @Override
      public String getRemoteHost() {
        return httpServletRequest.getRemoteHost();
      }

      @Override
      public int getRemotePort() {
        return httpServletRequest.getRemotePort();
      }

      @Override
      public String getLocalAddr() {
        return httpServletRequest.getLocalAddr();
      }

      @Override
      public int getLocalPort() {
        return httpServletRequest.getLocalPort();
      }
    };
  }
}
