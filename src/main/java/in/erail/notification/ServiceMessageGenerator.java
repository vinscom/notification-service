package in.erail.notification;

import io.vertx.core.json.JsonObject;

/**
 *
 * @author vinay
 * @param <T>
 */
public interface ServiceMessageGenerator<T extends Card> {
  JsonObject create(T pCard);
}
