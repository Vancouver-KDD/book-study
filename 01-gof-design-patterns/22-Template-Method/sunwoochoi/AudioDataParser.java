/* Concrete Class for parsing audio data */
public class AudioDataParser extends AbstractDataParser {

  public AudioDataParser(String path) {
    super(path);
  }

  @Override
  String loadData(String path) {
    System.out.println("Load Audio data from " + path);
    return "aac:hello, want to hang out?";
  }

  @Override
  String data2Str(String raw) {
    System.out.println("Transform raw audio data to String: " + raw);
    return raw.substring(4);
  }
  
}
