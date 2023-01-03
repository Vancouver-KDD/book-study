# Flyweight design pattern example

- Client
```java
public class Client {
    public static void main(String[] args) {
        List<Laptop> laptops = new ArrayList<>();

        ExteriorFactory exteriorFactory = new ExteriorFactory();

        String[] cpus = new String[]{"M1", "M1Pro", "M1Max", "M2", "M2Pro", "M2Max"};
        String[] rams = new String[]{"16GB", "32GB", "64GB"};
        String[] ssds = new String[]{"512GB", "1TB", "2TB", "4TB"};

        Laptop laptop;
        Random rand = new Random();

        for (int i = 0; i < 1000; ++i) {
            int cpuIndex = rand.nextInt(5) + 1;
            int ramIndex = rand.nextInt(2) + 1;
            int ssdIndex = rand.nextInt(3) + 1;

            if (cpuIndex % 2 == 0)
                 laptop = new Laptop(cpus[cpuIndex], rams[ramIndex], ssds[ssdIndex], exteriorFactory.getExterior("SilverPlastic", "Silver", "Plastic"));
            else
                laptop = new Laptop(cpus[cpuIndex], rams[ramIndex], ssds[ssdIndex], exteriorFactory.getExterior("SilverPlastic", "SpaceGray", "Aluminium"));
            laptops.add(laptop); 
        }
    }
}
```

- Laptop

```java
public class Laptop {

    // String types for convenience
    // Extrinsic
    private String cpu;
    private String ram;
    private String ssd;
    // Intrinsic 
    private Exterior exterior;

    public Laptop(String cpu, String ram, String ssd, Exterior exterior) {
        this.cpu = cpu;
        this.ram = ram;
        this.ssd = ram;
        this.exterior = exterior;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public String getSsd() {
        return ssd;
    }

    public void setSsd(String ssd) {
        this.ssd = ssd;
    }
}
```

- Exterior (extrinsic)
```java
public class Exterior {

    // String types for convenience
    private String color;
    private String material;

    public Exterior(String color, String material) {
        this.color    = color;
        this.material = material;
    }

    @Override
    public String toString() {
        return this.color + this.material;
    }
}
```

- ExteriorFactory
```java
public class ExteriorFactory {
    private final HashMap<String, Exterior> EXTERIOR_LIST = new HashMap();

    public Exterior getExterior(String exteriorType, String color, String material) {
        if (EXTERIOR_LIST.get(exteriorType) != null) {
            EXTERIOR_LIST.put(exteriorType, new Exterior(color, material));
        }
        return EXTERIOR_LIST.get(exteriorType);
    }
}
```