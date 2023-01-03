/* subsystem to fetch the audio source from db */
public class MusicFetcher {
  public String getAudioSource(String title, Boolean isSub) {
      System.out.println("Fetching audio resource...");
      if (!isSub) {
        return title + " - Sample 1 min";
      }
     return title + " - Full 4 mins";
  }
}
