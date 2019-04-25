package in.erail.notification.service;

import com.google.common.net.MediaType;

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

    String user = pEvent.getRequest().getPathParameters().get("user");

    Endpoint endpoint = new JsonObject(pEvent.getRequest().bodyAsString()).mapTo(Endpoint.class);
    endpoint.setUser(user);

    return getPushNotificationService()
            .addDevice(endpoint)
            .doOnSuccess(ep -> {
              pEvent.getResponse().setBody(new JsonObject().put("id", ep.getEndpoint()).toBuffer().getBytes());
              pEvent.getResponse().setMediaType(MediaType.JSON_UTF_8);
            })
            .map(ep -> pEvent)
            .toMaybe();
  }

  public PushNotificationService getPushNotificationService() {
    return mPushNotificationService;
  }

  public void setPushNotificationService(PushNotificationService pPushNotificationService) {
    this.mPushNotificationService = pPushNotificationService;
  }

}
