package in.erail.notification;

import in.erail.glue.annotation.StartService;
import in.erail.glue.component.ServiceMap;

/**
 *
 * @author vinay
 */
public class PushNotificationManager {

  private ServiceMap mServiceMessageGenerator;
  private PushNotificationService mPushNotificationService;

  @StartService
  public void start() {
  }

  public void send(String pUser, String pToken, Card pCard) {
    
  }

  public ServiceMap getServiceMessageGenerator() {
    return mServiceMessageGenerator;
  }

  public void setServiceMessageGenerator(ServiceMap pServiceMessageGenerator) {
    this.mServiceMessageGenerator = pServiceMessageGenerator;
  }

  public PushNotificationService getPushNotificationService() {
    return mPushNotificationService;
  }

  public void setPushNotificationService(PushNotificationService pPushNotificationService) {
    this.mPushNotificationService = pPushNotificationService;
  }

}
