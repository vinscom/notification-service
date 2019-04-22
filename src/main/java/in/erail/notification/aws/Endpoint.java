package in.erail.notification.aws;

import in.erail.notification.ServiceType;
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
  @Column(name = "user_id")
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

  public String getEndpoint() {
    return endpoint;
  }

  public void setEndpoint(String pEndpoint) {
    this.endpoint = pEndpoint;
  }

  public ServiceType getType() {
    return type;
  }

  public void setType(ServiceType pType) {
    this.type = pType;
  }

  public Date getCreatedOn() {
    return createdOn;
  }

  public void setCreatedOn(Date pCreatedOn) {
    this.createdOn = pCreatedOn;
  }

  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(boolean pDeleted) {
    this.deleted = pDeleted;
  }

}
