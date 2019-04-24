package in.erail.notification.aws;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.CreatePlatformEndpointResult;
import com.amazonaws.services.sns.model.DeleteEndpointResult;
import com.amazonaws.services.sns.model.PublishResult;
import in.erail.glue.Glue;
import in.erail.notification.ServiceType;
import in.erail.notification.card.DefaultCard;
import in.erail.notification.model.Endpoint;
import static org.junit.jupiter.api.Assertions.*;

import in.erail.notification.model.EndpointId;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManagerHelper;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author vinay
 */
@ExtendWith(VertxExtension.class)
public class AWSPushNotificationServiceTest {

  private final AWSPushNotificationService service = Glue.instance().resolve("/in/erail/notification/PushNotificationService");
  private final AmazonSNS client = service.getSNSClient();
  private final EntityManagerHelper emh = Glue.instance().resolve("/javax/persistence/EntityManagerHelper");

  public AWSPushNotificationServiceTest() {
    reset(client);
    when(client.createPlatformEndpoint(any())).thenReturn(new CreatePlatformEndpointResult().withEndpointArn("EndpointArn"));
    when(client.deleteEndpoint(any())).thenReturn(new DeleteEndpointResult());
    when(client.publish(any())).thenReturn(new PublishResult().withMessageId("MSGID"));
  }

  @Test
  @Order(1)
  public void addDeviceTest(VertxTestContext testContext) {
    Endpoint ep = new Endpoint().setUser("endpoint1").setToken("token").setType(ServiceType.APNS);
    Endpoint result = service.addDevice(ep).blockingGet();
    assertEquals(result.getEndpoint(), "EndpointArn");
    emh
            .getEM()
            .doOnSuccess((em) -> assertNotNull(em.find(Endpoint.class, new EndpointId("endpoint1", "token"))))
            .subscribe(t -> testContext.completeNow(), err -> testContext.failNow(err));
  }

  @Test
  @Order(2)
  public void removeDeviceTest(VertxTestContext testContext) {
    Endpoint ep = new Endpoint().setUser("endpoint2").setToken("token2").setType(ServiceType.APNS);
    service.addDevice(ep).blockingGet();
    Endpoint ep2 = new Endpoint().setUser("endpoint2").setToken("token2");
    service.removeDevice(ep2).blockingAwait();
    emh
            .getEM()
            .doOnSuccess((em) -> assertNull(em.find(Endpoint.class, new EndpointId("endpoint2", "token2"))))
            .subscribe(t -> testContext.completeNow(), err -> testContext.failNow(err));
  }

  @Test
  @Order(3)
  public void sendTest(VertxTestContext testContext) {
    service.addDevice(new Endpoint().setUser("endpoint3").setToken("token3").setType(ServiceType.APNS)).blockingGet();
    service.addDevice(new Endpoint().setUser("endpoint3").setToken("token4").setType(ServiceType.APNS)).blockingGet();

    DefaultCard card = new DefaultCard();
    card.setBody("Body");
    card.setTitle("Title");

    service
            .publish("endpoint3", card)
            .toList()
            .doOnSuccess(l -> assertEquals(2, l.size()))
            .subscribe((l) -> testContext.completeNow(), err -> testContext.failNow(err));

  }
}
