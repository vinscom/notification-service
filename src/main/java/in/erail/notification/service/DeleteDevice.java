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
public class DeleteDevice extends RESTServiceImpl {

  private PushNotificationService mPushNotificationService;
  
  @Override
  public MaybeSource<Event> process(Maybe<Event> pEvent) {
    return pEvent.flatMap(this::deleteDevice);
  }

  protected Maybe<Event> deleteDevice(Event pEvent) {
    return Maybe.just(pEvent);
  }

  public PushNotificationService getPushNotificationService() {
    return mPushNotificationService;
  }

  public void setPushNotificationService(PushNotificationService pPushNotificationService) {
    this.mPushNotificationService = pPushNotificationService;
  }
  
}
