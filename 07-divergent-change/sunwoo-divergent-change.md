# Divergent Change
Divergent change occurs when one module is often changed in different ways for different reasons. 

## Solution
### Split Phase
If we split the function into different phases, we can minimize the module/function to change when it requires. Moreover, it gives us advantages minimize amount of cognitive resources.

1. Extract the second phase code into its own function
2. Test
3. Introduce intermidate data structure as an additional argument to the extracted function
4. Test
```ts
const getTweetReport = (tweet: ITweet) => {
    const context:string = tweet.text;
    const words: string[] = context.split(' ');
    const tokens: string[] = tokenize(words);
    const cleanTokens: string[] = cleanTokens(tokens);
    const result: ITweetResult = tweetModel(cleanTokens);
    createReport(result);
}

const getTweetReport = (tweet: ITweet) => {
    const tokens: ITweetTokens = tokenize(tweet);
    
    const result: ITweetResult = tweetModel(tokens);
    createReport(result);
}

const tokenize = (tweet: ITweet): ITweetTokens => {
    const context:string = tweet.text;
    const words: string[] = context.split(' ');
    const tokens: string[] = tokenize(words);
    const cleanTokens: ITweetTokens[] = cleanTokens(tokens);
    return cleanTokens;
}
```
