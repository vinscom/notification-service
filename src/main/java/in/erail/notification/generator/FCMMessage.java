package in.erail.notification.generator;

/**
 *
 * @author vinay
 */
public class FCMMessage {
  
  private Data data;
  private String priority;

  public Data getData() {
    return data;
  }

  public void setData(Data pData) {
    this.data = pData;
  }

  public String getPriority() {
    return priority;
  }

  public void setPriority(String pPriority) {
    this.priority = pPriority;
  }
  
  public static class Data{
    private String title;
    private String message;
    private int count;
    private String sound;
    private int notId;
    private String contentAvailable;

    public String getTitle() {
      return title;
    }

    public void setTitle(String pTitle) {
      this.title = pTitle;
    }

    public String getEssage() {
      return message;
    }

    public void setEssage(String pEssage) {
      this.message = pEssage;
    }

    public int getCount() {
      return count;
    }

    public void setCount(int pCount) {
      this.count = pCount;
    }

    public String getSound() {
      return sound;
    }

    public void setSound(String pSound) {
      this.sound = pSound;
    }

    public int getNotId() {
      return notId;
    }

    public void setNotId(int pNotId) {
      this.notId = pNotId;
    }

    public String getContentAvailable() {
      return contentAvailable;
    }

    public void setContentAvailable(String pContentAvailable) {
      this.contentAvailable = pContentAvailable;
    }
    
  }
}
