# Chapter 17. Money Retrospective

> What's Next?
- Metaphor - The dramatic effect metaphor has on the structure of the design. 
- JUnit Usage - When we ran tests and how we used JUnit.
- Code Metrics - A numerical abstract of the resulting code.
- Process - We say red/green/refactor, but how much work goes into each step? 
- Test Quality - How do TDD tests stack up against conventional test metrics?

### What's Next?
Is the code finished? No. There is that nasty duplication between Sum.plus() and Money.plus(). If we made Expression a class instead of an interface (not the usual direction, as classes more often become interfaces), we would have a natural home for the common 
code.

I don't believe in "finished." TDD can be used as a way to strive for perfection, but that isn't its most effective use. If you have a big system, then the parts that you touch all the time should be absolutely rock solid, so you can make daily changes confidently. As you drift out to the periphery of the system, to parts that don't change often, the tests can be spottier and the design uglier without interfering with your confidence.

모두 수행한 후에는 SmallLint for Smalltalk와 같은 코드 평론을 진행하는것도 좋은 방법이다. 많은 제안들로 부터 배울 수 있다.

또 다른 "다음은 무엇입니까?" 질문은 "어떤 추가 테스트가 필요합니까?"이다. 작동하지 않아야 하는 테스트가 실제로 작동하지 않는 경우가 있다면 이유를 알아야 하고 이를 to-do리스트에 계속적으로 업데이트 하며 관리한다.

Finally, when the list is empty is a good time to review the design. Do the words and concepts 
play together? Is there duplication that is difficult to eliminate given the current design? 
(Lingering duplication is a symptom of latent design.)


### Metaphor

Th e expression metaphor freed me from a bunch of nasty issues about merging duplicated currencies. The code came out cleaner and clearer than I've ever seen it before. I'm concerned about the performance of expressions, but I'm happy to wait until I see some usage statistics before I start optimizing


### One Last Review
 - The three items that come up time and again as surprises when teaching TDD are:
 - The three approaches to making a test work cleanly - fake it, triangulation, and obvious 
implementation
 - Removing duplication between test and code as a way to drive the design
 - The ability to control the gap between tests to increase traction when the road gets slippery and cruise faster when conditions are clear
