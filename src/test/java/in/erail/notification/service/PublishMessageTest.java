package in.erail.notification.service;

import in.erail.glue.Glue;
import in.erail.server.Server;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 *
 * @author vinay
 */
@ExtendWith(VertxExtension.class)
public class PublishMessageTest {
 
  @Test
  public void testProcess(VertxTestContext testContext) {
    
    Server server = Glue.instance().<Server>resolve("/in/erail/server/Server");
    testContext.completeNow();
//
//    WebClient
//            .create(server.getVertx())
//            .get(server.getHttpServerOptions().getPort(), server.getHttpServerOptions().getHost(), "/v1/session")
//            .putHeader(HttpHeaders.CONTENT_TYPE, MediaType.JSON_UTF_8.toString())
//            .rxSend()
//            .doOnSuccess(response -> Assertions.assertEquals(200, response.statusCode()))
//            .doOnSuccess((t) -> {
//              JsonArray data = t.bodyAsJsonArray();
//              Assertions.assertEquals(5, data.size());
//            })
//            .subscribe(t -> testContext.completeNow(), err -> testContext.failNow(err));
  }
  
}
