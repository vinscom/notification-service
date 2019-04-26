package in.erail.notification.service;

import in.erail.auth.OAuth2Utils;
import in.erail.model.Event;
import in.erail.notification.PushNotificationService;
import in.erail.notification.model.Endpoint;
import in.erail.service.RESTServiceImpl;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import java.util.Base64;

/**
 *
 * @author vinay
 */
public class DeleteDevice extends RESTServiceImpl {

  private PushNotificationService mPushNotificationService;

  @Override
  public MaybeSource<Event> process(Maybe<Event> pEvent) {
    return pEvent.flatMap(this::deleteDevice);
  }

  protected Maybe<Event> deleteDevice(Event pEvent) {

    String user = OAuth2Utils
            .getUserIdFromRequest(pEvent.getRequest())
            .orElse(pEvent.getRequest().getPathParameters().get("user"));

    String token = new String(Base64.getDecoder().decode(pEvent.getRequest().getPathParameters().get("token")));

    Endpoint ep = new Endpoint(user, token);

    return getPushNotificationService()
            .removeDevice(ep)
            .andThen(Maybe.just(pEvent));
  }

  public PushNotificationService getPushNotificationService() {
    return mPushNotificationService;
  }

  public void setPushNotificationService(PushNotificationService pPushNotificationService) {
    this.mPushNotificationService = pPushNotificationService;
  }

}
