# Loops
Loop has been used a lot for list processing because there is no alterantive approach.
However, these days, first-class functions are widely supported in many languages, so replace loop with pipeline.

first-class funcion: the function which are treated like any other variables.

## Replace Loop with Pipeline
### Motivation
Collection pipelines aloow programmers to describe iterable processing as a series of operations. 
* map: to transform each element of the input collection T(n) -> A(n)
* filter: select a subset of the input collection for later steps in the pipeline T(n) -> T(k) k <= n 

```ts
interface IUser {
    name: string,
    isSubscriber: boolean,
    totalPayment: number
}

const getVIPUserNames = (users: Array<IUser>): Array<string> => {
    const vipUser: Array<string> = [];
    for (const user of users) {
        if (user.isSubscriber && user.totalPayment > 100) {
            vipUser.concat(user.name);
        }
    }
    return vipUser;
}

const isSubscriber = (user: IUser) => {return user.isSubscriber}
const isTotalPatmentExceedHundred = (user: IUser) => {return (user.totalPayment > 100)}
const transfromName = (user: IUser) => {return user.name}
const getVIPUsersPiepe = (users: Array<IUser>):Array<string> => {
    return users
            .filter(isSubscriber)
            .filter(isTotalPatmentExceedHundred)
            .map(transfromName)
}
```