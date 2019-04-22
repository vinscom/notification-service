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

  public void setTitle(String pTitle) {
    this.mTitle = pTitle;
  }

  public String getBody() {
    return mBody;
  }

  public void setBody(String pBody) {
    this.mBody = pBody;
  }

}
