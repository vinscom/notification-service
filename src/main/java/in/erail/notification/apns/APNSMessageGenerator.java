package in.erail.notification.apns;

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
    return JsonObject.mapFrom(card);
  }

}
