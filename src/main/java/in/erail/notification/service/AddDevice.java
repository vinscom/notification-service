package in.erail.notification.service;

import in.erail.model.Event;
import in.erail.notification.PushNotificationService;
import in.erail.notification.model.Endpoint;
import in.erail.service.RESTServiceImpl;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.vertx.core.json.JsonObject;

/**
 *
 * @author vinay
 */
public class AddDevice extends RESTServiceImpl {

  private PushNotificationService mPushNotificationService;

  @Override
  public MaybeSource<Event> process(Maybe<Event> pEvent) {
    return pEvent.flatMap(this::addDevice);
  }

  protected Maybe<Event> addDevice(Event pEvent) {
    Endpoint ep = new JsonObject(pEvent.getRequest().bodyAsString()).mapTo(Endpoint.class);
    getPushNotificationService().addDevice(ep);
    return Maybe.just(pEvent);
  }

  public PushNotificationService getPushNotificationService() {
    return mPushNotificationService;
  }

  public void setPushNotificationService(PushNotificationService pPushNotificationService) {
    this.mPushNotificationService = pPushNotificationService;
  }

}
