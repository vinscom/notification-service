package in.erail.notification.aws;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.CreatePlatformEndpointResult;
import com.amazonaws.services.sns.model.DeleteEndpointResult;
import in.erail.glue.Glue;
import in.erail.notification.ServiceType;
import in.erail.notification.model.Endpoint;
import static org.junit.jupiter.api.Assertions.*;

import in.erail.notification.model.EndpointId;
import io.vertx.junit5.Timeout;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import java.util.concurrent.TimeUnit;
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
  private final EntityManagerHelper emh = Glue.instance().resolve("/javax/persistence/EntityManagerHelper");
  private final AmazonSNS client = service.getSNSClient();

  public AWSPushNotificationServiceTest() {
    when(client.createPlatformEndpoint(any())).thenReturn(new CreatePlatformEndpointResult().withEndpointArn("EndpointArn"));
    when(client.deleteEndpoint(any())).thenReturn(new DeleteEndpointResult());
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
    Endpoint ep2 = new Endpoint().setUser("endpoint2").setToken("token2").setType(ServiceType.APNS).setEndpoint("EndpointArn");
    service.removeDevice(ep2).blockingAwait();
    emh
            .getEM()
            .doOnSuccess((em) -> assertNull(em.find(Endpoint.class, new EndpointId("endpoint2", "token2"))))
            .subscribe(t -> testContext.completeNow(), err -> testContext.failNow(err));
  }

}
