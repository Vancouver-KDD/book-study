import java.util.ArrayList;

/* Concrete Class for parsing chat data */
public class ChatDataParser extends AbstractDataParser {

  public ChatDataParser(String path) {
    super(path);
  }

  @Override
  String loadData(String path) {
    System.out.println("Load Chat data from " + path);
    return "text:Hello, wanna hang out?";
  }

  @Override
  String data2Str(String raw) {
    System.out.println("Transform raw text data to String: " + raw);
    return raw.substring(5);
  }

  @Override
  public void stem(ArrayList<String> dataTokens) {
    super.stem(dataTokens);
    System.out.println("Chat-Delete Emoji");
  }
}
