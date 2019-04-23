package in.erail.notification.card;

import in.erail.notification.Card;

/**
 *
 * @author vinay
 */
public class DefaultCard implements Card {

  private String mTitle;
  private String mBody;

  public String getTitle() {
    return mTitle;
  }

  public DefaultCard setTitle(String pTitle) {
    this.mTitle = pTitle;
    return this;
  }

  public String getBody() {
    return mBody;
  }

  public DefaultCard setBody(String pBody) {
    this.mBody = pBody;
    return this;
  }

}
