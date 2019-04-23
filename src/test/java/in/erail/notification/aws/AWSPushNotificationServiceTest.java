package in.erail.notification.aws;

import com.amazonaws.auth.AWS3Signer;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.CreatePlatformEndpointResult;
import in.erail.glue.Glue;
import in.erail.notification.ServiceType;
import in.erail.notification.aws.AWSPushNotificationService;
import in.erail.notification.model.Endpoint;
import io.reactivex.Single;
import javax.persistence.EntityManagerHelper;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mock.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author vinay
 */
public class AWSPushNotificationServiceTest {

  @Test
  public void testAdd() {
    AWSPushNotificationService service = Glue.instance().resolve("/in/erail/notification/PushNotificationService");
    AmazonSNS client = service.getSNSClient();

    when(client.createPlatformEndpoint(any())).thenReturn(new CreatePlatformEndpointResult().withEndpointArn("EndpointArn"));
    Endpoint ep = new Endpoint().setUser("endpoint1").setToken("token").setType(ServiceType.APNS);
    Endpoint result = service.add(ep).blockingGet();
    assertEquals(result.getEndpoint(), "EndpointArn");
  }

}
