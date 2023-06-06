# Chapter 17. Money Restrospective

### What's Next?

TDD can be used as a way to strive for perfection. but not the most effective use.
큰 시스템에서 매일 다루는 부분들은 견고해야 매일 다른 변화나 주변부의 변화를 더욱 자신감을 가지고 할 수 있음
테스트는 항상 그자리에서 실행해줌
작동하지 말하야 하는 테스트 -> 실제로는 작동할 수도 있음 -> `known limitation` 으로 기록하거나 `work to be done later` 로 기록해놓을 수 있음
모든 Todo list가 비었을때가 디자인을 리뷰해보기 좋은 때임 -> 코드와 컨셉이 함께 어울러졌는지, 현재 디자인에서 제거하기 어려운 중복이 있는지 등

### Metaphor

`MoneySum`, `MoneyBag`, `Wallet` metaphor를 사용했음
metaphor를 사용해서 merging duplicated currencies 에 관련된 이슈들로부터 자유롭게 해주었다는데...?? 글쎼....

### JUnit Usage

코드를 쓰면서 계속 테스트를 돌림

### Code Metrics

### Process

red/green/refactor

- Add a little test
- Run all tests and fail
- Make a change
- Run the tests and succeed
- Refactor to remove duplication
  The number of changes per refactoring should follow a "fat tail" or eptokurtotic profile (bell curve)

### Test Quality

How do TDD tests stack up against conventional test metrics?
TDD 테스트가 다른 종류의 테스트를 대체할 수 없음 : performance, stress, usability

- `Statement coverage` : 테스트 퀄리티를 측정하는 충분한 방법은 아니지만 시작점 정도는 됨. 100% statement coverage 가져야함.
- `Defect insertion` : "코드 라인을 바꾸면 테스트가 실패해야 한다" 라는 아이디어.

테스트 커버리지를 높이는 방법은 테스트를 필요로 하는 방면을 나누어서 다른 방면들을 테스트하는 테스트의 수를 늘리는것
또 다른 방법는 fixed set of tests를 사용하고 logic를 최대한 단순화하는 것

마지막 포인트 리뷰

- 테스트를 더 깔끔하게 작동하게 만드는 세가지 접근방법 : fake it, trangulation, obvious implementation
- 테스트와 코드 사이에 duplication 제거
- 테스트들 사이의 캡을 컨트롤할 수 있는 능력???
