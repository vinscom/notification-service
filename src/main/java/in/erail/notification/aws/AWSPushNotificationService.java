package in.erail.notification.aws;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.CreatePlatformEndpointRequest;
import com.amazonaws.services.sns.model.InvalidParameterException;
import in.erail.glue.annotation.StartService;
import in.erail.notification.DefaultPushNotificationService;
import in.erail.notification.model.Endpoint;
import io.reactivex.Single;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author vinay
 */
public class AWSPushNotificationService extends DefaultPushNotificationService {

  private AmazonSNS mSNSClient;
  private Regions mRegion = Regions.EU_WEST_1;
  private String mAPNSPlatformApplicationARN;
  private String mFCMPlatformApplicationARN;
  private boolean mEnable;
  private Logger mLog;

  @StartService
  public void start() {
    if (mSNSClient == null && isEnable()) {
      mSNSClient = AmazonSNSClientBuilder
              .standard()
              .withRegion(getRegion())
              .withCredentials(new DefaultAWSCredentialsProviderChain())
              .build();
    }
  }

  @Override
  public Single<Endpoint> add(Endpoint pEndpoint) {

    if(!isEnable()){
      return add(pEndpoint);
    }
    
    Single<Endpoint> addEndpoint
            = createEndpoint(pEndpoint)
                    .map(e -> pEndpoint.setEndpoint(e))
                    .flatMap(ep -> super.add(ep));

    return checkEndpointExists(pEndpoint).switchIfEmpty(addEndpoint);
  }

  protected Single<String> createEndpoint(Endpoint pEndpoint) {

    String applicationArn = null;

    switch (pEndpoint.getType()) {
      case APNS:
        applicationArn = getAPNSPlatformApplicationARN();
        break;
      case FCM:
        applicationArn = getFCMPlatformApplicationARN();
        break;
    }

    CreatePlatformEndpointRequest cpeReq
            = new CreatePlatformEndpointRequest()
                    .withPlatformApplicationArn(applicationArn)
                    .withToken(pEndpoint.getToken())
                    .withCustomUserData(pEndpoint.getUser());

    return Single
            .fromCallable(() -> getSNSClient().createPlatformEndpoint(cpeReq))
            .map(res -> res.getEndpointArn())
            .onErrorReturn((err) -> {
              if (InvalidParameterException.class.isAssignableFrom(err.getClass())) {
                InvalidParameterException ipe = (InvalidParameterException) err;
                String message = ipe.getErrorMessage();
                getLog().error(() -> "Exception message: " + message);
                Pattern p = Pattern
                        .compile(".*Endpoint (arn:aws:sns[^ ]+) already exists "
                                + "with the same token.*");
                Matcher m = p.matcher(message);
                if (m.matches()) {
                  // The platform endpoint already exists for this token, but with
                  // additional custom data that
                  // createEndpoint doesn't want to overwrite. Just use the
                  // existing platform endpoint.
                  return m.group(1);
                }
              }
              throw new RuntimeException("Not able to create endpoint:", err);
            });
  }

  public String getAPNSPlatformApplicationARN() {
    return mAPNSPlatformApplicationARN;
  }

  public void setAPNSPlatformApplicationARN(String pAPNSPlatformApplicationARN) {
    this.mAPNSPlatformApplicationARN = pAPNSPlatformApplicationARN;
  }

  public String getFCMPlatformApplicationARN() {
    return mFCMPlatformApplicationARN;
  }

  public void setFCMPlatformApplicationARN(String pFCMPlatformApplicationARN) {
    this.mFCMPlatformApplicationARN = pFCMPlatformApplicationARN;
  }

  public AmazonSNS getSNSClient() {
    return mSNSClient;
  }

  public void setSNSClient(AmazonSNS pSNSClient) {
    this.mSNSClient = pSNSClient;
  }

  public Regions getRegion() {
    return mRegion;
  }

  public void setRegion(Regions pRegion) {
    this.mRegion = pRegion;
  }

  public Logger getLog() {
    return mLog;
  }

  public void setLog(Logger pLog) {
    this.mLog = pLog;
  }

  public boolean isEnable() {
    return mEnable;
  }

  public void setEnable(boolean pEnable) {
    this.mEnable = pEnable;
  }

}
