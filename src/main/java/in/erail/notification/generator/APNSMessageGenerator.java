package in.erail.notification.generator;

import in.erail.notification.Card;
import in.erail.notification.ServiceMessageGenerator;
import in.erail.notification.card.DefaultCard;
import io.vertx.core.json.JsonObject;

/**
 *
 * @author vinay
 */
public class APNSMessageGenerator implements ServiceMessageGenerator<Card> {

  @Override
  public JsonObject create(Card pCard) {
    DefaultCard card = (DefaultCard) pCard;
    APNSMessage.Alert alert = new APNSMessage.Alert();
    alert.setTitle(card.getTitle());
    alert.setBody(card.getBody());
    APNSMessage.Aps aps = new APNSMessage.Aps();
    aps.setAlert(alert);
    APNSMessage msg = new APNSMessage();
    msg.setAps(aps);
    return JsonObject.mapFrom(msg);
  }

}
