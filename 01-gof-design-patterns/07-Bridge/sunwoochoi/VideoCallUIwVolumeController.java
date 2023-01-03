/* RefindAbstraction class to have volume controller feature */
public class VideoCallUIwVolumeController extends VideoCallUI {
  public VideoCallUIwVolumeController(VideoCallHandler handler) {
    super(handler);
  }

  public void setVolume(int volume) {
    handler.setVolume(volume);
  }
  
  public void muteVolume() {
    System.out.println("mute volume");
    handler.setVolume(0);
  }
}
