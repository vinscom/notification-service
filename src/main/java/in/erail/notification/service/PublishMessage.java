package in.erail.notification.service;

import com.google.common.base.Joiner;
import in.erail.model.Event;
import in.erail.notification.PushNotificationService;
import in.erail.notification.card.DefaultCard;
import in.erail.service.RESTServiceImpl;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.vertx.core.json.JsonObject;

/**
 *
 * @author vinay
 */
public class PublishMessage extends RESTServiceImpl {

  private PushNotificationService mPushNotificationService;

  @Override
  public MaybeSource<Event> process(Maybe<Event> pEvent) {
    return pEvent.flatMap(this::publishMessage);
  }

  protected Maybe<Event> publishMessage(Event pEvent) {
    String user = pEvent.getRequest().getPathParameters().get("user");
    //String group = pEvent.getRequest().getPathParameters().get("group");

    DefaultCard card = new JsonObject(pEvent.getRequest().bodyAsString()).mapTo(DefaultCard.class);

    return getPushNotificationService()
            .publish(user, card)
            .toList()
            .doOnSuccess(l -> getLog().debug(() -> "Message sent devices:" + Joiner.on(",").join(l)))
            .map(l -> pEvent)
            .toMaybe();

  }

  public PushNotificationService getPushNotificationService() {
    return mPushNotificationService;
  }

  public void setPushNotificationService(PushNotificationService pPushNotificationService) {
    this.mPushNotificationService = pPushNotificationService;
  }

}
