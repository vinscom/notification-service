package in.erail.notification;

import in.erail.notification.model.Endpoint;
import io.reactivex.Completable;
import io.reactivex.Single;

/**
 *
 * @author vinay
 */
public interface PushNotificationService {

  Single<Endpoint> addDevice(Endpoint pEndpoint);
  Completable removeDevice(Endpoint pEndpoint);
  
}
