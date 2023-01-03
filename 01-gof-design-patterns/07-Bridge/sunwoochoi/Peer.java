/* object holding peer information */
public class Peer {
  private int id;
  private String name;

  public Peer(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public int getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }
}
