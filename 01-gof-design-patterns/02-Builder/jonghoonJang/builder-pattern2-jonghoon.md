## Builder Pattern

- 복잡한 구성의 객체를 효과적으로 생성하는 패턴
- 2가지 패턴

#### 2nd. Object 여러 단계를 순차적으로 거칠 때, 이 단계의 순서를 결정해 두고 각 단계를 다양하게 구현할 수 

```Java
public class Data{
    private String name;
    private String age;

    public Data(String name, String age){
        this.name = name;
        this.age = age;
    }

    public String getName(){
        return name;
    }

    public String getAge(){
        return age;
    }
}

public abstract class Builder{
    protected Data data;

    public Builder(Data data){
        this.data = data;
    }

    public abstract String head();
    public abstract String body();
    public abstract String foot();
}


public class Director {
    private Builder builder;

    public Director(Builder builder){
        this.builder = builder;
    }

    public String build(){
        StringBuilder sb = new StringBuilder();

        sb.append(builder.head());
        sb.append(builder.body());
        sb.append(builder.foot());

        return sb.toString();
    }
}

public class PlainTextBuilder extends Builder{
    public PlainTextBuilder(Data data){
        super(data);
    }

    @Override
    public String head() {
        return ""

    @Override
    public String body() {
        StringBuilder sb = new StringBuilder();

        sb.append("Name:  ");
        sb.append(data.getName());
        sb.append(", Age: ");
        sb.append(data.getAge());

        return sb.toString();
    }

    @Override
    public String foot() {
        return "";
    }
}

public class JSONBuilder extends Builder{
    public JSONBuilder(Data data){
        super(data);
    }

    @Override
    public String head() {
        return "{";
    }

    @Override
    public String body() {
        StringBuilder sb = new StringBuilder();

        sb.append("\"name\": \"");
        sb.append(data.getName());
        sb.append("\", \"age\": \"");
        sb.append(data.getAge());
        sb.append("\"");

        return sb.toString();
    }

    @Override
    public String foot() {
        return "}";
    }
}

public class MainEntry {
    public static void main(String[] args){
        Data data = new Data("John", "20");

        // PlainTextBuilder
        Builder builder = new PlainTextBuilder(data);
        Director director = new Director(builder);
        String result = director.build();
        System.out.println(result);

        // JSONBuilder
        builder = new JSONBuilder(data);
        director = new Director(builder);
        result = director.build();
        System.out.println(result);
    }
}
```

Resources:
https://www.youtube.com/watch?v=sg_6GWRBRas&ab_channel=GISDEVELOPER
