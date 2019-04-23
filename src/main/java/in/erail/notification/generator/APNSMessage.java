package in.erail.notification.generator;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 *
 * @author vinay
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APNSMessage {

  private Aps aps;
  private int notId;

  public Aps getAps() {
    return aps;
  }

  public void setAps(Aps pAps) {
    this.aps = pAps;
  }

  public int getNotId() {
    return notId;
  }

  public void setNotId(int pNotId) {
    this.notId = pNotId;
  }

  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class Alert {

    private String title;
    private String body;
    private String launchImage;

    public String getTitle() {
      return title;
    }

    public void setTitle(String pTitle) {
      this.title = pTitle;
    }

    public String getBody() {
      return body;
    }

    public void setBody(String pBody) {
      this.body = pBody;
    }

    public String getLaunchImage() {
      return launchImage;
    }

    public void setLaunchImage(String pLaunchImage) {
      this.launchImage = pLaunchImage;
    }

  }

  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class Aps {

    private Alert alert;
    private int badge;
    private String contentAvailable;
    private String category;
    private String threadId;
    private String sound;

    public Alert getAlert() {
      return alert;
    }

    public void setAlert(Alert pAlert) {
      this.alert = pAlert;
    }

    public int getBadge() {
      return badge;
    }

    public void setBadge(int pBadge) {
      this.badge = pBadge;
    }

    public String getContentAvailable() {
      return contentAvailable;
    }

    public void setContentAvailable(String pContentAvailable) {
      this.contentAvailable = pContentAvailable;
    }

    public String getCategory() {
      return category;
    }

    public void setCategory(String pCategory) {
      this.category = pCategory;
    }

    public String getThreadId() {
      return threadId;
    }

    public void setThreadId(String pThreadId) {
      this.threadId = pThreadId;
    }

    public String getSound() {
      return sound;
    }

    public void setSound(String pSound) {
      this.sound = pSound;
    }

  }
}
