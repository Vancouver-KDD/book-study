import java.util.ArrayList;
import java.util.Arrays;

public abstract class AbstractDataParser {
  private String path;
  
  public AbstractDataParser(String path) {
    this.path = path;
  }

  /* template method for parsing algorithm */
  public void templateMethod() {
    System.out.println("Start data parser");
    String raw = loadData(path);
    String strData = data2Str(raw);
    ArrayList<String> tokens = tokenize(strData);
    removeStopWord(tokens);
    stem(tokens);
    word2Vec(tokens);
    feed(tokens);
    System.out.println("End data parser");
  }

  abstract String loadData(String path);
  abstract String data2Str(String raw);

  public ArrayList<String> tokenize(String strData) {
    System.out.println("Abstract-Tokenize string " + strData);
    ArrayList<String> tokens = new ArrayList<String>(Arrays.asList(strData.split(" ", 0)));
    return tokens;
  }

  public void removeStopWord(ArrayList<String> dataTokens) {
    System.out.println("Abstract-Removing stop words in place " + dataTokens);
  }

  public void stem(ArrayList<String> dataTokens) {
    System.out.println("Abstract-Stemming words in place " + dataTokens);
  }

  public void word2Vec(ArrayList<String> dataTokens) {
    System.out.println("Abstract-Transform string to vector of " +dataTokens);
  }

  public void feed(ArrayList<String> dataTokens) {
    System.out.println("Abstract-Feed data " + dataTokens + " to model");
  }
}
