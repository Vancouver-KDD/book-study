/* Client */
public class FacadeTest {
  public static void main(String[] args) {
    User user1 = new User("u_0001", true);
    MusicFacade facade1 = new MusicFacade(user1);
    /*
      Output:
      Fetching audio resource...
      Finding Output device...
      Play the song Hello - Full 4 mins
      Output device: AirPod
    */
    facade1.play("Hello");
    
    User user2= new User("u_0001", false);
    MusicFacade facade2 = new MusicFacade(user2);
    /*
      Output:
      Fetching audio resource...
      Finding Output device...
      Play the song World - Sample 1 min
      Output device: AirPod
    */
    facade2.play("World");
  }
}
