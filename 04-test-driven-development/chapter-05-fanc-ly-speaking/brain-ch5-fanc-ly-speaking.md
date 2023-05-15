# 5장 솔직히 말하자면

## 목록
- $5 + 10CHF = $10(환율 2:1일 경우)
- ~~$5 x 2 = $10~~
- ~~amount를 private로 만들기~~
- ~~Dollar 부작용?~~
- Money 반올림?
- ~~equals()~~
- hashCode()
- Equal null
- Equal object
- 5CHF x 2 = 10CHF

1. 테스트 작성
2. 컴파일되게 하기
3. 실패하는지 확인하기 위해 실행
4. 실행하게 만듦
5. 중복 제거

Dollar 복붙
```java
class Franc {    
    private int amount;
    Franc(int amount) {
        this.amount= amount;
    }
   
    Franc times(int multiplier) {
        return new Franc(amount * multiplier);
    }
   
    public boolean equals(Object object) {
        Franc franc= (Franc) object;
        return amount == franc.amount;
    }
}
```

## 목록
- $5 + 10CHF = $10(환율 2:1일 경우)
- ~~$5 x 2 = $10~~
- ~~amount를 private로 만들기~~
- ~~Dollar 부작용?~~
- Money 반올림?
- ~~equals()~~
- hashCode()
- Equal null
- Equal object
- ~~5CHF x 2 = 10CHF~~ (삭제)
- Dollar/Franc 중복 (추가)
- 공용 equals (추가)
- 공용 times (추가)

## 배운점
- 큰 테스트를 공략할 수 없다. 그래서 진전을 나타낼 수 있는 자그마한 테스트를 만들었다.
- 뻔뻔스럽게도 중복을 만들고 조금 고쳐서 테스트를 작성했다.
- 설상가상으로 보델 코드까지 도매금으로 보사하고 수정해서 테스트를 통과 했다.
- 중복이 사라지기 전에는 집에 가지 않겠다고 약속했다.