# Composite design pattern example

```java
// Client

public class Client {
  public static void main(String[] args) {
    // LaptopSleeve with its items inside
    LuggageItem macbook = new LuggageItem("Macbook Pro");
    LuggageItem macbookCharger = new LuggageItem("Macbook Pro Charger");
    LuggageItem iPad = new LuggageItem("IPad");
    LuggageItem laptopSleeve = new LuggageItem("Laptop Sleeve");

    laptopSleeve.putAll(List.of(macbook, macbookCharger, iPad));

    // CosmeticContainer with its items inside
    LuggageItem lipstick = new LuggageItem("Lipstick");
    LuggageItem sanitizer = new LuggageItem("Sanitizer");
    LuggageItem cosmeticContainer = new LuggageItem("Cosmetic Container");

    cosmeticContainer.putAll(List.of(lipstick, sanitizer));

    // Single item
    LuggageItem ballPen = new LuggageItem("Ball pen");
    // Single item
    LuggageItem eBookReader = new LuggageItem("E-book reader");

    // Put all items in a luggage
    List<LuggageItem> items = new ArrayList<>();
    items.addAll(List.of(laptopSleeve, cosmeticContainer, ballPen, eBookReader));

    Luggage luggage = new Luggage(items);
    luggage.getItems();
  }
}

// LuggageItem
public class LuggageItem {

  private String name;
  private List<LuggageItem> items;

  public LuggageItem(String name) {
      this.name = name;
      this.items = new ArrayList<>();
  }

  public void put(LuggageItem luggageItem) {
      items.add(luggageItem);
  }

  public void putAll(List<LuggageItem> luggageItems) {
      items.addAll(luggageItems);
  }

  public void getItemName() {
      System.out.printf("- %s\n", this.name);
      if (!items.isEmpty()) {
          System.out.printf("%s has the below items inside:\n", this.name);
          items.forEach(i -> i.getItemName());
          System.out.println("-------------------------------------------");
      }
      Luggage.numOfItems++;
  }
}


// Container
public class Container extends LuggageItem {
  public Container(String name) {
      super(name);
  }
}

// Luggage
public class Luggage {

  protected static int numOfItems = 0;
  private List<LuggageItem> items;

  public Luggage(List<LuggageItem> luggageItems) {
      if (luggageItems != null) this.items = luggageItems;
  }

  public void addItem(LuggageItem luggageItem) {
      items.add(luggageItem);
  }

  public void getItems() {
      if (items != null) {
          if (items.isEmpty()) System.out.println("This luggage is empty");
          else {
              items.forEach(i -> {
                  i.getItemName();
              });
          }
      }
      System.out.printf("Total items in the luggage: %d\n", numOfItems);
  }
}
```