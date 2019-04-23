package in.erail.notification.service;

import in.erail.model.Event;
import in.erail.notification.PushNotificationService;
import in.erail.service.RESTServiceImpl;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;

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
    return Maybe.empty();
  }

  public PushNotificationService getPushNotificationService() {
    return mPushNotificationService;
  }

  public void setPushNotificationService(PushNotificationService pPushNotificationService) {
    this.mPushNotificationService = pPushNotificationService;
  }
  
}
