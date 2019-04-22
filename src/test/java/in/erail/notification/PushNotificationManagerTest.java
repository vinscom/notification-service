
package in.erail.notification;

import in.erail.glue.Glue;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 *
 * @author vinay
 */
public class PushNotificationManagerTest {
  
  @Test
  public void testSend() {
    PushNotificationManager mgr = Glue.instance().resolve("/in/erail/notification/PushNotificationManager");
  }

}
