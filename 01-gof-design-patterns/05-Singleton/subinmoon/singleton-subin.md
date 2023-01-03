# [Subin] Singleton

## What is Singleton?
- A creational design pattern that lets you ensure that a class has *only one instance*, while providing a global access point to this instance


### Singleton은 다음과 같은 경우 사용된다:
- 보통 데이터페이스 연결 모듈에 많이 사용됨
- 단 하나의 instance만 존재해야 하고, client가 잘 알려진 access point로 접근할 수 있어야 할 때

### Singleton의 구성
- Singleton:
  - client가 그들만의 독립적인 instance를 가질 수 있도록 Instance operation을 정의
  - Singleton 클래스의 고유한 instance를 만드는 역할을 한다

## How to implement it?
~~~
// MongoDB in JavaScript

Mongoose.prototype.connect = function(uri, options, callback) {
    const _mongoose = this instanceof Mongoose ? this : mongoose;
    const conn = _mongoose.connection;

    return _mongoose._promiseOrCallback(callback, cb => {
        conn.openUri(uri, options, err => {
            if (err != null) {
                return cb(err);
            }
            return cb(null, _mongoose);
        })
    })
}
~~~

## Advantage
- 만들어진 단 하나의 인스턴스를 다른 모듈들이 공유하면서 사용 -> 인스턴스를 생성할 때 드는 비용이 줄어든다
  - 메모리 낭비를 방지하고, 이미 생성된 인스턴스를 활용하면서 속도도 빨라질 수 있다
- 다른 클래스 간에 데이터 공유가 쉽다. 싱글톤 인스턴스가 전역으로 사용되는 인스턴스이기 때문에 다른 클래스의 인스턴스들이 접근하여 사용할 수 있다.
  - 단, 여러 클래스의 인스턴스에서 싱글톤 인스턴스의 데이터에 동시에 접근하지 않도록 주의가 필요하다. "동시성 문제"

## Disadvantage
- TDD(Test Driven Development)를 할 때, 각 테스트마다 '독립적인' 인스턴스를 만들어 단위 테스트 하기 어렵다
- 모듈 간의 결합을 강하게 만들 수 있다
  - 이때 의존성 주입(Dependency Injection)을 통해 모듈 간의 결합을 조금 더 느슨하게 만들어 해결할 수 있음

## Sources
- Erich Gamma, John Vlissides, Ralph Johnson, Richard Helm, *Design Patterns: Elements of Reusable Object-Oriented Software*, Addison-Wesley, 1994, pp.127-134.
- https://refactoring.guru/design-patterns/singleton