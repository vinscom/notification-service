package in.erail.notification.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.CreatePlatformEndpointResult;
import com.amazonaws.services.sns.model.DeleteEndpointResult;
import com.amazonaws.services.sns.model.PublishResult;

import in.erail.glue.Glue;
import in.erail.notification.ServiceType;
import in.erail.notification.aws.AWSPushNotificationService;
import in.erail.notification.card.DefaultCard;
import in.erail.notification.model.Endpoint;
import in.erail.server.Server;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.reactivex.ext.web.client.WebClient;

/**
 *
 * @author vinay
 */
@ExtendWith(VertxExtension.class)
public class PublishMessageTest {

  private final AWSPushNotificationService service = Glue.instance().resolve("/in/erail/notification/PushNotificationService");
  private final AmazonSNS client = service.getSNSClient();

  public PublishMessageTest() {
    reset(client);
    when(client.createPlatformEndpoint(any())).thenReturn(new CreatePlatformEndpointResult().withEndpointArn("EndpointArn"));
    when(client.deleteEndpoint(any())).thenReturn(new DeleteEndpointResult());
    when(client.publish(any())).thenReturn(new PublishResult().withMessageId("MSGID"));
  }

  @Test
  public void testProcess(VertxTestContext testContext) {

    Server server = Glue.instance().<Server>resolve("/in/erail/server/Server");

    Endpoint ep = new Endpoint().setType(ServiceType.APNS).setToken("testtoken3");

    DefaultCard card = new DefaultCard().setBody("card-body").setTitle("card-title");
    
    WebClient
            .create(server.getVertx())
            .post(server.getHttpServerOptions().getPort(), server.getHttpServerOptions().getHost(), "/v1/notification/device/testuser3")
            .rxSendJsonObject(JsonObject.mapFrom(ep))
            .doOnSuccess(response -> assertEquals(200, response.statusCode()))
            .doOnSuccess((t) -> {
              String id = t.bodyAsJsonObject().getString("id");
              assertEquals(id, "EndpointArn");
            })
            .flatMap((res) -> {
              return WebClient
                      .create(server.getVertx())
                      .post(server.getHttpServerOptions().getPort(), server.getHttpServerOptions().getHost(), "/v1/notification/publish/testuser3/push")
                      .rxSendJsonObject(JsonObject.mapFrom(card));
            })
            .doOnSuccess(response -> assertEquals(200, response.statusCode()))
            .doOnSuccess(response -> verify(client,times(1)).publish(any()))
            .subscribe(t -> testContext.completeNow(), err -> testContext.failNow(err));
  }

}
