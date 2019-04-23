package in.erail.notification.model;

import java.io.Serializable;

/**
 *
 * @author vinay
 */
public class EndpointId implements Serializable {

  private String user;
  private String token;

  public EndpointId() {
  }

  public EndpointId(String pUser, String pToken) {
    this.user = pUser;
    this.token = pToken;
  }

  public EndpointId(Endpoint pEndpoint) {
    this.user = pEndpoint.getUser();
    this.token = pEndpoint.getToken();
  }
  
  public String getUser() {
    return user;
  }

  public void setUser(String pUser) {
    this.user = pUser;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String pToken) {
    this.token = pToken;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((user == null) ? 0 : user.hashCode());
    result = prime * result + ((token == null) ? 0 : token.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }

    EndpointId other = (EndpointId) obj;
    if (user == null) {
      if (other.user != null) {
        return false;
      }
    } else if (!user.equals(other.user)) {
      return false;
    }

    if (token == null) {
      if (other.token != null) {
        return false;
      }
    } else if (!token.equals(other.token)) {
      return false;
    }

    return true;
  }

}
