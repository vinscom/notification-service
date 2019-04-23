package in.erail.notification;

import in.erail.notification.model.Endpoint;
import io.reactivex.Single;

/**
 *
 * @author vinay
 */
public interface PushNotificationService {

  Single<Endpoint> add(Endpoint pEndpoint);
}
