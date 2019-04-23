package in.erail.notification.service;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.CreatePlatformEndpointRequest;
import com.amazonaws.services.sns.model.DeleteEndpointRequest;
import com.amazonaws.services.sns.model.InvalidParameterException;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.google.common.base.Preconditions;
import in.erail.notification.Card;
import in.erail.notification.ServiceMessageGenerator;
import in.erail.notification.ServiceType;
import in.erail.notification.model.Endpoint;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.vertx.core.json.JsonObject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author vinay
 */
public class AWSPushNotificationService extends DefaultPushNotificationService {

  private AmazonSNS mSNSClient;
  private Regions mRegion = Regions.EU_WEST_1;
  private String mAPNSPlatformApplicationARN;
  private String mFCMPlatformApplicationARN;

  @Override
  public Single<Endpoint> addDevice(Endpoint pEndpoint) {
    Single<Endpoint> addEndpoint
            = createEndpoint(pEndpoint)
                    .map(e -> pEndpoint.setEndpoint(e))
                    .flatMap(ep -> super.addDevice(ep));

    return checkEndpointExists(pEndpoint).switchIfEmpty(addEndpoint);
  }

  protected Single<String> createEndpoint(Endpoint pEndpoint) {

    String applicationArn = getPlatformApplicationEndpoint(pEndpoint.getType());

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

  @Override
  public Completable removeDevice(Endpoint pEndpoint) {

    Preconditions.checkNotNull(pEndpoint.getEndpoint());

    DeleteEndpointRequest req = new DeleteEndpointRequest().withEndpointArn(pEndpoint.getEndpoint());

    return Single
            .fromCallable(() -> getSNSClient().deleteEndpoint(req))
            .flatMapCompletable(r -> super.removeDevice(pEndpoint));
  }

  protected String getPlatformApplicationEndpoint(ServiceType pType) {
    switch (pType) {
      case APNS:
        return getAPNSPlatformApplicationARN();
      case FCM:
        return getFCMPlatformApplicationARN();
      default:
        return "";
    }
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public Observable<String> send(String pUser, Card pCard) {
    return findDevices(pUser)
            .flatMapMaybe((ep) -> {
              getLog().debug(() -> "Endpoint:" + ep.toString());
              ServiceMessageGenerator smg = (ServiceMessageGenerator) getServiceMessageGenerator().get(ep.getType().name());
              JsonObject msg = smg.create(pCard);
              PublishRequest req = new PublishRequest()
                      .withTargetArn(ep.getEndpoint())
                      .withMessage(msg.toString())
                      .withMessageStructure("json");
              getLog().debug(() -> "Sending Push Message:" + req.toString());
              try {
                PublishResult res = getSNSClient().publish(req);
                getLog().debug(() -> "Result Push Message:" + res.toString());
                return Maybe.just(res.getMessageId() + ":" + ep.getEndpoint());
              } catch (Exception ex) {
                getLog().error(() -> ex);
              }
              return Maybe.empty();
            })
            .doOnNext(id -> getLog().debug("Message ID:" + id));
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
    if (mSNSClient == null) {
      mSNSClient = AmazonSNSClientBuilder
              .standard()
              .withRegion(getRegion())
              .withCredentials(new DefaultAWSCredentialsProviderChain())
              .build();
    }
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

}
