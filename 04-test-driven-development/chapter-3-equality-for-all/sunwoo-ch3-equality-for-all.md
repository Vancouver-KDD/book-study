# Chapter 3. Equality for All

Value Object는 object를 값처럼 사용하는 패턴으로, 객체의 필드값은 constructor에서 설정이 된 후 변경이 되지 않는다.
이렇게 value object를 사용할 경우, aliasing등 의도치 않은 버그들을 예방 할 수있다.

Value object는 `equals`라는 메소드를 구현해야 하기때문에, 테스트 코드를 통해 어떤것이 equal한지를 생각을 적습니다.

```Java
public void testEquality() {
    assertTrue(new Dollar(5).equals(new Dollar(5)));
}
```

이 테스트 코드에 대한 소스 코드 변경점

```Java
public boolean equals(Object object) {
    return true;
}
```

테스트 코드를 변경

```Java
public void testEquality() {
    assertTrue(new Dollar(5).equals(new Dollar(5)));
    assertFalse(new Dollar(5).equals(new Dollar(6)));
}
```

소스 코드를 변경

```Java
public boolean equals(Object object) {
    Dollar dollar= (Dollar) object;
    return amount == dollar.amount;
}
```
