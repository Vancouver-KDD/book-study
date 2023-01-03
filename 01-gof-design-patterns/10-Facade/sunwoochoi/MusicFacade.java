/* Facade to operate the music player */
public class MusicFacade {
  private User user;
  private MusicFetcher musicFetcher;
  private DeviceDetector detector;

  public MusicFacade(User user) {
    this.user = user;
    this.musicFetcher = new MusicFetcher();
    this.detector = new DeviceDetector();
  }

  public void play(String title) {
    String source = musicFetcher.getAudioSource(title, user.getStatus());
    String output = detector.findBluetoothDevice();
    System.out.println("Play the song " + source);
    System.out.println("Output device: " + output);

  }
}
