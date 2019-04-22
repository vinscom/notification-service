package in.erail.notification.aws;

import in.erail.notification.PushNotificationService;
import in.erail.notification.ServiceType;
import in.erail.persistence.EntityManagerHelper;
import io.vertx.core.json.JsonObject;

/**
 *
 * @author vinay
 */
public class AWSPushNotificationService implements PushNotificationService {

  EntityManagerHelper mEntityManagerHelper;
  
  @Override
  public void add(String pUser, String pToken, ServiceType pType) {
    
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void remove(String pUser, String pToken) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void send(String pUser, JsonObject pMessage) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public EntityManagerHelper getEntityManagerHelper() {
    return mEntityManagerHelper;
  }

  public void setEntityManagerHelper(EntityManagerHelper pEntityManagerHelper) {
    this.mEntityManagerHelper = pEntityManagerHelper;
  }

}
