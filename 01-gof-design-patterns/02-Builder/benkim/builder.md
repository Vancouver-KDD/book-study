# Builder clsss
- object를 생성하는 방법중 하나  
- constructor를 통한 생성방식, setter를 통한 생성방식의 단점을 보완

## Pros

- 가독성을 높여주고 어디에 어떤 값을 세팅해야 하는지 명확하게 알고 사용할 수 있다
- 필요한 값만 설정하면, 나머지 값은 null로 초기화 된다
- 하나의 메서드를 사용하는 것처럼 사용 가능하고, 마지막 build()메서드까지 호출해야지만 User객체로써 활용할 수 있기 때문에 일관성을 지킬 수 있다

## Cons
- 클래스 작성으로 인해 코드가 길어진다


```java
String email = "benkim@gmail.com"
String password = "password"
String name = "benkim"

User user = new User.Builder().email(email).password(password).name(name).build();

public class User {
    private String email;
    
    private String password;
    
    private String name;
    
    public User(String email, String password, String name) {
        
        this.email = email;
        
        this.password = password;
        
        this.name = name;
        
    }
    
    static public class Builder {
        this.email = email;
        this.password = password;
        this.name = name;
        
        // 값 설정후 자기자신(Builder 객체)를 반환해야함
        public Builder email(String email) {
            this.email = email;
            return this;
        }
        
        public Builder password(String password) {
            this.password = password;
            return this;
        }
        
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        
        public User build() {
            return new User(email, password, name)
        }
    }

}


```

## Ref
- [youtube link](https://www.youtube.com/watch?v=BCI4mLPsJUk&ab_channel=Programmers)