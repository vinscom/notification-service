package in.erail.notification;

import io.vertx.core.json.JsonObject;

/**
 *
 * @author vinay
 */
public interface PushNotificationService {

  void send(String pUser, JsonObject pMessage);

  void add(String pUser, String pToken, ServiceType pType);

  void remove(String pUser, String pToken);
}
