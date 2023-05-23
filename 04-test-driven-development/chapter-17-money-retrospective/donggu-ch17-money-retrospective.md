# Chapter 17. Money Restrospective

## Restrospective
1. There is still duplication between Sum.plus() and Money.plus(). We also implemented Expression class instead of an interface.
2. If you have a big system, it is always nice to have parts that are rock soild. So that, we can move on to next parts confidently.
3. When the test list is empty, it is a good time to review the design
4. TDD cycle
    - Add a little test.
    - Run all tests and fail.
    - Make a change.
    - Run the tests and succeed. 
    - Refactor to remove duplication.

### Test Quality
- Performance
- Stress
- Usability
- Statement coverage: TDD followed religiously should result in 100 percent statement coverage.
- Defect insertion is another way of evaluating test quality. Change the meaning of a line of code and a test should break.
- To take a fixed set of tests and simplify the logic of the progam is one of ways to improve coverage. (or instead of increasing the test coverage to walk all permutations of input)

### One Last Review from the book
1. The three approaches to making a test work cleanly - fake it, triangulation, and obvious implementation.
2. Removing duplication between test and code as a way to drive the design.
3. The ability to control the gap between tests to increase traction when the road gets slippery and cruise faster when conditions are clear.