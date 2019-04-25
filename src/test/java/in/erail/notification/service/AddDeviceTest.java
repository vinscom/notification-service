package in.erail.notification.service;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.CreatePlatformEndpointResult;
import com.amazonaws.services.sns.model.DeleteEndpointResult;
import com.amazonaws.services.sns.model.PublishResult;
import in.erail.glue.Glue;
import in.erail.notification.ServiceType;
import in.erail.notification.aws.AWSPushNotificationService;
import in.erail.notification.model.Endpoint;
import in.erail.notification.model.EndpointId;
import in.erail.server.Server;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.reactivex.ext.web.client.WebClient;
import javax.persistence.EntityManagerHelper;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

/**
 *
 * @author vinay
 */
@ExtendWith(VertxExtension.class)
public class AddDeviceTest {

  private final AWSPushNotificationService service = Glue.instance().resolve("/in/erail/notification/PushNotificationService");
  private final AmazonSNS client = service.getSNSClient();
  private final EntityManagerHelper emh = Glue.instance().resolve("/javax/persistence/EntityManagerHelper");

  public AddDeviceTest() {
    reset(client);
    when(client.createPlatformEndpoint(any())).thenReturn(new CreatePlatformEndpointResult().withEndpointArn("EndpointArn"));
    when(client.deleteEndpoint(any())).thenReturn(new DeleteEndpointResult());
    when(client.publish(any())).thenReturn(new PublishResult().withMessageId("MSGID"));
  }

  @Test
  public void testProcess(VertxTestContext testContext) {

    Server server = Glue.instance().<Server>resolve("/in/erail/server/Server");

    Endpoint ep = new Endpoint().setType(ServiceType.APNS).setToken("testtoken1");

    WebClient
            .create(server.getVertx())
            .post(server.getHttpServerOptions().getPort(), server.getHttpServerOptions().getHost(), "/v1/internal/notification/device/testuser1")
            .rxSendJsonObject(JsonObject.mapFrom(ep))
            .doOnSuccess(response -> assertEquals(200, response.statusCode()))
            .doOnSuccess((t) -> {
              String id = t.bodyAsJsonObject().getString("id");
              assertEquals(id,"EndpointArn");
            })
            .flatMap((res) -> {
              return emh
                      .getEM()
                      .doOnSuccess((em) -> assertNotNull(em.find(Endpoint.class, new EndpointId("testuser1", "testtoken1"))));
            })
            .subscribe(t -> testContext.completeNow(), err -> testContext.failNow(err));
  }

}
