package in.erail.notification;

import in.erail.glue.annotation.StartService;

/**
 *
 * @author vinay
 */
public class PushNotificationManager {

  private PushNotificationService mPushNotificationService;

  @StartService
  public void start() {
  }

  public void send(String pUser, String pToken, Card pCard) {
    
  }

  public PushNotificationService getPushNotificationService() {
    return mPushNotificationService;
  }

  public void setPushNotificationService(PushNotificationService pPushNotificationService) {
    this.mPushNotificationService = pPushNotificationService;
  }

}
