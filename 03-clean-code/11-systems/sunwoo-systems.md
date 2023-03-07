# Systems

## How Would You Build a City?
한 사람이 도시 전체의 디테일들을 관리 할 수 없듯이, 소프트웨어 시스템도 마찬가지이다.
적절한 abstraction levels을 통하여, big picture에 대한 완벽한 이해없이 시스템 각각의 모듈을 이해하는 것처럼,
더 높은 abstraction level에서 시스템을 어떻게 클린하게 관리하는지도 알아보자.

## Separate Constructing a System from Using It
먼저 고려해보아야 할 점은 system의 runtime logic과 startup process(객체가 생성이 되고, dependencies가 서로 연결되는 프로세스)를 분리해야 하는 것이다.
많은 경우, 이것들은 밑의 예와 같이 혼재되어 존재하는 경우가 많다.

```Java
public Service getService() {
    if (service == null)
        service = new MyServiceImpl(...); // Good enough default for most cases?
    return service;
}
```
만약 이와같은 startup process 코드가 `MyServiceImpl`내에 존재한다면, SRP를 위반하고 또한 테스트도 힘들어지게 된다.
만약 시스템 전역에 setup strategy가 분산되어 있다면, 이것은 모듈성을 저하시키고, 많은 코드를 반복하게 될 수 있다.
우리는 이러한 startup process를 런타임 로직과 분리하고, 같은 전략을 시스템 전체에 적용함으로써, 많은 의존성 문제를 해결 할 수 있다.

## Spearation of Main
construction과 runtime을 분리하는 방법중 하나는 모든 contruction을 main에 옮기고, main에서 불러들이는 모듈들은,
모듈안에서 dependency를 해결하게 된다.

`main` function은 시스템에 필요한 오브젝트를 생성하고, 그것을 application에 넘긴다.
이렇게 함으로써 application은 안에서 일어나는 정보를 알 필요가 없어지고, 간단하게 받은 오브젝트를 사용하게 된다.

## Factories
런타임에 오브젝트를 생성해야 할때는 abstract factory method를 사용할수 있다.
application은 직접 오브젝트를 생성하는 것이 아닌, factory에게 오브젝트 생성에 관한 책임을 맡긴다.
이렇게 함으로써, application에서는 생성된 오브젝트만을 가져다 사용하므로, 오브젝트 contruction과 decouple를 시킬수 있다.

## Dependency Injection
DI(Dpendency injection)을 사용한 construction은 IoC을 depedency management에 적용한 메커니즘이다. 
IoC란 design principle의 하나로 소프트웨어 한 부분의 excution을 다른 프레임워크나 콘테이너에게 control을 맡기는 것이다.
DI는 오브젝트 인스턴스 생성을 다른 콘테이너에게 책임을 전가함으로써 SRP를 고수한다.

예를 들어 JNDI(Java Naming and Directory Interface)는 필요한 의존성을 찾고 이름을 통해 찾고, 찾은 오브젝트를 반환해준다.
진정한 DI는 이러한 과정을 모두 passive하게 진행하게 된다.

## Scaling Up
도시가 발전하면서, 예상하지 못했던 기능 혹은 확장이 필요한 것처럼, 시스템 또한 처음부터 완벽하게 만들수는 없습니다.
우리는 현재를 구현하고, 내일의 확장을 위한 시스템을 구축해야 합니다. 이것은 시스템 레벨에서도 적용이 됩니다.

다음은 확장성에 관한 안티패턴의 시스템입니다.
```Java
// An EJB2 local interface for a Bank EJB
package com.example.banking;
import java.util.Collections;
import javax.ejb.*;
public interface BankLocal extends java.ejb.EJBLocalObject {
    String getStreetAddr1() throws EJBException;
    String getStreetAddr2() throws EJBException;
    String getCity() throws EJBException;
    String getState() throws EJBException;
    String getZipCode() throws EJBException;
    void setStreetAddr1(String street1) throws EJBException;
    void setStreetAddr2(String street2) throws EJBException;
    void setCity(String city) throws EJBException;
    void setState(String state) throws EJBException;
    void setZipCode(String zip) throws EJBException;
    Collection getAccounts() throws EJBException;
    void setAccounts(Collection accounts) throws EJBException;
    void addAccount(AccountDTO accountDTO) throws EJBException;
}
```
위에 인터페이스에서 보이는것과 같이 `BankLocal`은 주소와 어카운트등을 attributes로 가지고 있는 것을 볼 수 있다.
```Java
// The corresponding EJB2 Entity Bean Implementation
package com.example.banking;
import java.util.Collections;
import javax.ejb.*;
public abstract class Bank implements javax.ejb.EntityBean {
    // Business logic...
    public abstract String getStreetAddr1();
    public abstract String getStreetAddr2();
    public abstract String getCity();
    public abstract String getState();
    public abstract String getZipCode();
    public abstract void setStreetAddr1(String street1);
    public abstract void setStreetAddr2(String street2);
    public abstract void setCity(String city);
    public abstract void setState(String state);
    public abstract void setZipCode(String zip);
    public abstract Collection getAccounts();
    public abstract void setAccounts(Collection accounts);
    public void addAccount(AccountDTO accountDTO) {
        InitialContext context = new InitialContext();
        AccountHomeLocal accountHome = context.lookup("AccountHomeLocal");
        AccountLocal account = accountHome.create(accountDTO);
        Collection accounts = getAccounts();
        accounts.add(account);
    }
    // EJB container logic
    public abstract void setId(Integer id);
    public abstract Integer getId();
    public Integer ejbCreate(Integer id) { ... }
    public void ejbPostCreate(Integer id) { ... }
    // The rest had to be implemented but were usually empty:
    public void setEntityContext(EntityContext ctx) {}
    public void unsetEntityContext() {}
    public void ejbActivate() {}
    public void ejbPassivate() {}
    public void ejbLoad() {}
    public void ejbStore() {}
    public void ejbRemove() {}
}
```
보이는 것과 같이, 많은 비지니스 로직들이 실제 구현이 콘테이너 안에서 되어 있고, 이것들은 서로 tightly coupled되어 있습니다.

## Cross-Cutting Concerns
Cross-cutting concerns이란 시스템의 부분으로써, 시스템 다른 부분에 영향을 끼치거나 의존성으로 가지고 있는 것을 말합니다. [1](https://en.wikipedia.org/wiki/Cross-cutting_concern#:~:text=Cross%2Dcutting%20concerns%20are%20parts,oriented%20programming%20or%20procedural%20programming.) AOP는 프로그램의 파라다임중 하나로써, cross-cutting concerns을 모듈화하고, aspects이라 불리는 모듈로 encapsulation하여 분리시킨다.

### Java Proxies
Java proxies는 오브젝트나 클레스에서 메소드 호출을 wrapping하는 것처럼 간단한 상황에서 적합하게 사용할 수 있다. 밑의 예제처럼 간단한 코드에도 proxies는 clean하게 작성이 어렵고, 이해하기 또한 힘들다.
```Java
// Bank.java (suppressing package names...)
import java.utils.*;
// The abstraction of a bank.
public interface Bank {
    Collection<Account> getAccounts();
    void setAccounts(Collection<Account> accounts);
}
// BankImpl.java
import java.utils.*;
// The “Plain Old Java Object” (POJO) implementing the abstraction.
public class BankImpl implements Bank {
    private List<Account> accounts;
    public Collection<Account> getAccounts() {
        return accounts;
    }
    public void setAccounts(Collection<Account> accounts) {
        this.accounts = new ArrayList<Account>();
        for (Account account: accounts) {
            this.accounts.add(account);
        }
    }
}

// BankProxyHandler.java
import java.lang.reflect.*;
import java.util.*;
// “InvocationHandler” required by the proxy API.
public class BankProxyHandler implements InvocationHandler {
    private Bank bank;
    public BankHandler (Bank bank) {
        this.bank = bank;
    }
    // Method defined in InvocationHandler
    public Object invoke(Object proxy, Method method, Object[] args)
        throws Throwable {
        String methodName = method.getName();
        if (methodName.equals("getAccounts")) {
            bank.setAccounts(getAccountsFromDatabase());
            return bank.getAccounts();
        } else if (methodName.equals("setAccounts")) {
            bank.setAccounts((Collection<Account>) args[0]);
            setAccountsToDatabase(bank.getAccounts());
            return null;
        } else {
            ...
        }
    }
    // Lots of details here:
    protected Collection<Account> getAccountsFromDatabase() { ... }
    protected void setAccountsToDatabase(Collection<Account> accounts) { ... }
}
// Somewhere else...
Bank bank = (Bank) Proxy.newProxyInstance(
    Bank.class.getClassLoader(),
    new Class[] { Bank.class },
    new BankProxyHandler(new BankImpl()));
```
위에처럼 ProxyHandler는 InvocationHandler의 `invoke`를 구현해 세부사항을 `Proxy`안에서 핸들링 할 수 있게 해준다.

### Pure Java AOP Frameworks
AOP 프레임워크를 통하여, proxy의 boilerplate 코드를 자동으로 처리 할 수 있습니다.
Spring 프레임워크를 사용한다면, 우리는 비지니스 로직을 POJO(Plain-Old Java Objects)로 작성할 수 있고,
나머지 부분은 Annotation을 이용해 구현을 할 수 있게됩니다.

XML Configuration 파일에 `<bean>`을 통해서 도메인 오브젝트를 wrapping 할 수 있습니다.
클라이언트 쪽에서는 직접적으로 도메인 오브젝트와 상호작용을 하는 것처럼 보이지만, 사실을 decorator로 wrapping된 오브젝트들과 소통을 하게 됩니다.

```Java
package com.example.banking.model;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
@Entity
@Table(name = "BANKS")
public class Bank implements java.io.Serializable {
    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    @Embeddable // An object “inlined” in Bank’s DB row
    public class Address {
        protected String streetAddr1;
        protected String streetAddr2;
        protected String city;
        protected String state;
        protected String zipCode;
    }
    @Embedded
    private Address address;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER,
    mappedBy="bank")
    private Collection<Account> accounts = new ArrayList<Account>();
        public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void addAccount(Account account) {
        account.setBank(this);
        accounts.add(account);
    }
    public Collection<Account> getAccounts() {
        return accounts;
    }
}
```

### AspectJ Aspect
AspectJ 언어는 Java의 확장된 언어로, 모듈를 construct하는데 확장이 아닌 first-class로써 사용을 할 수 있게됩니다 (annotation으로도 제공).

## Test Drive the System Archictecture
이렇게 많은 상호작용 하는 코드를 분리시키는 작업은 도메인 로직을 POJOs로 작성할 수 있게 도와주고, 또 시스템을 decouple시켜 테스트 가능하게 도와줍니다.
시스템이 확장성에 대해 열리면, 따로 코드를 작성하기 전에 큰 설계를 할 필요없이, 더 쉽게 변화에 대해서 유연함을 가질수 있게 됩니다.

## Optimize Decision Making
이렇게 시스템 레벨에서 분리를 할 수 있게된다면, 각각 서브 시스템레벨에서 코드를 관리 할 수 있고, 또한 변화에 대한 의사결정도 할 수 있게 됩니다.

## Use Standards Wisely, When They Add Demonstrable Value
많은 팀들이 EJB2같은 구조를 표준으로 삼고있지만, 구조에 집착을 하고 그것에 너무 많은 시간을 쏟기보다 더 중요한것은 유저에게 가치를 부여하는 것에 초점을 둬야 합니다.

## Systems Need Domain-Specific Languages
DSL(Domain-Specific Languages)는 스크립트 언어 혹은 API로 도메인 전문가가 제안한 형태로 코드가 작성되는 것을 도와줍니다.
좋은 DSL은 도메인과 그것을 구현한 코드간의 소통의 차이를 최소합니다. 이것은 우리가 도메인을 코드로 구현하는 것에 있어서 리스크를 줄여줄수 있습니다.

## Conclusion
Invasive한 아키텍쳐는 도메인 로직을 방해하고 agility에 악영향을 끼친다.
모든 추상 레벨에서 의도가 POJO를 통해 명확하게 나타내지고, 나머지는 위의 메카니즘을 통해서 구현한다면 testable한 아키텍쳐를 가질수 있습니다.
<strong>use the simplest thing that can possibly work.</strong>