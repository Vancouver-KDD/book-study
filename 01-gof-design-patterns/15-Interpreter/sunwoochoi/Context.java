import java.util.HashMap;
import java.util.Map;

/* Context is a namespace to get reference of variables */
public class Context {
  private static Map<String, Integer> vars = new HashMap<>();

  public static void assign(String name, Integer value){
    vars.put(name, value);
  }
  public static Integer getValue(String name) {
    if (vars.containsKey(name)) {
      return vars.get(name);
    }
    return 0;
  }
}
