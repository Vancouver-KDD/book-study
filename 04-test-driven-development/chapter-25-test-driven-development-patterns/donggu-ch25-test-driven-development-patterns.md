# Chapter 25. Test-Driven Development Patterns

# Test 
- The definitin of test is "A proceducre leading to acceptance or rejection".

## Isloated Test
- Make the tests so fast to run that we can run them ourself, and run them often.
- If just one test faiulre, that could cause multiple test faiulres, so we need to make test isolated.
- Make tests at a smaller scale than the whole applcation.

### Impilcation
- Tests's order should be independent
- Need to break our problem into littel orthogonal dimensions, so setting up the environment for each test is easy and quick.

## Test List
- Write a list of all the tests you know you will have to write. And the more experience we accumaulated, the more things we knew that might need to be done.
- If we have discovered larger refactoring that are out of scope for the moment, then move them to the "later" list.

## Test First
- When we test first, we reduce the stress, which makes us more likely to test.

## Assert First
```
testCompleteTransaction() {
    Server writer = Server(defaultPort(), "abc");
    Socket reader = Socker("localhost", defaultPort());
    Buffer reply = reader.contents();
    asswerTrue(reader.isClosed());
    assertEquals("abc", reply.contents());
}
```

## Test Data
- Use data that makes the tests easy to read and follow.
- Try never to use the same constant to mean more than one thing.
- The alternative to Test Data is Realistic Data, in which you use data from the real world.

## Evident Data
- Include expected and actual results in the test itself, and try to make their relationship apparent.
- Evident data can make programming easier.
