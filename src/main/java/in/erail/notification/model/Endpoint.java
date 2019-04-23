package in.erail.notification.model;

import in.erail.notification.ServiceType;
import io.vertx.core.json.JsonObject;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.CreationTimestamp;

/**
 *
 * @author vinay
 */
@Entity
@IdClass(EndpointId.class)
@Table(name = "erail_notif_endpoint")
public class Endpoint implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "user_id", length = 36)
  private String user;

  @Id
  private String token;

  private String endpoint;
  private ServiceType type;

  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdOn;

  private boolean deleted;

  public Endpoint() {
  }

  public Endpoint(String pUser, String pToken) {
    this.user = pUser;
    this.token = pToken;
  }

  public String getUser() {
    return user;
  }

  public Endpoint setUser(String pUser) {
    this.user = pUser;
    return this;
  }

  public String getToken() {
    return token;
  }

  public Endpoint setToken(String pToken) {
    this.token = pToken;
    return this;
  }

  public String getEndpoint() {
    return endpoint;
  }

  public Endpoint setEndpoint(String pEndpoint) {
    this.endpoint = pEndpoint;
    return this;
  }

  public ServiceType getType() {
    return type;
  }

  public Endpoint setType(ServiceType pType) {
    this.type = pType;
    return this;
  }

  public Date getCreatedOn() {
    return createdOn;
  }

  public Endpoint setCreatedOn(Date pCreatedOn) {
    this.createdOn = pCreatedOn;
    return this;
  }

  public boolean isDeleted() {
    return deleted;
  }

  public Endpoint setDeleted(boolean pDeleted) {
    this.deleted = pDeleted;
    return this;
  }

  @Override
  public String toString() {
    return JsonObject.mapFrom(this).toString();
  }

}
