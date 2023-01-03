# Mutable Data
Modifying data always brings some risk. It is hard to figure out the system failure because of unexpected changes to data. It's unavoidable to change the variables in the code, so we need to put more attension for changes to data.

## Solution
### Encapsulate Variable
Encapsulating variable delegates the changes to the encapsulating functions. Therefore, it is easy to monitor the changes and do validation for those changes

```ts
class Context {
    public ip;
    // ...
}

class Context {
    private ip;

    public get getIp() : string {
        return this.ip;
    }

    public set setIp(ip : string) {
        this.ip = ip;
    }

    // ...
}
```

### Split Variable
Single variable must have a single responsibilty. New values can be assigned to the variable while executing the program. (ex) index variable or accumulate variable in array looping. It's okay because those variable do a single job. However, one variable is used for multiple reasons, we should seperate them.

1. Change the name of varialbe
2. Make variable immutable if possible
3. Change the reference
4. Test

```js
const feed2AllModels = (data) => {
    // parsedData variable has responsibility to holds all parsed data
    // for different models
    let parsedData = parse4TextDectection(data);
    textDectModel.feed(parsedData);

    parsedData = parse4ObjectDectection(data);
    objDectModel.feed(parsedData);

    parsedData = parse4ImageRecognition(data);
    imgRecogModel.feed(parsedData);
}

const feed2AllModels = (data) => {
    // parsedData4text holds only parsed data for text dectection
    const parsedData4text = parse4TextDectection(data);
    textDectModel.feed(parsedData4text);

    const parsedData4Obj = parse4ObjectDectection(data);
    objDectModel.feed(parsedData4Obj);

    const parsedData4Img = parse4ImageRecognition(data);
    imgRecogModel.feed(parsedData4Img);
}
```

### Slide Statements 223
Match the appearance of lines of code with their relationship.
It makes much easier to understand the code.

1. Identify the target position to move the fragment to
2. Cut the fragement from the source and paste into the target position
3. Test
```js
const doRoutines = () => {
    let isRainning = whether.today();
    let routineBuilder = Rountine();
    let work;
    let doRunning;
    let cleaning;

    let isWeekend = date.getDay();
    if (!isRainning) {
        doRunning = true;
    }

    if (isWeekend)  {
        work = false;
        cleaning = true;
    } else {
        work = true;
        cleaning = false;
    }
    const routine = routineBuilder.run(doRunning).work(work).clean(cleaning).build();
    routine.execute();
}

const doRountines = () => { 
    // chage activity based on whether
    let isRainning = whether.today();
    let doRunning = false;
    if (!isRainning) {
        doRunning = true;
    }

    // chage activity based on the date
    let isWeekend = date.getDay();
    let work;
    let cleaning
    if (isWeekend) {
        work = false;
        cleaning = true;
    } else {
        work = true;
        cleaning = false;
    }

    // build rountine
    let routineBuilder = Rountine();
    const routine = routineBuilder.run(doRunning).work(work).clean(cleaning).build();
    routine.execute();
}
```

### Separate Query from Modifier 306
If we have a pure function with no observable side-effect, seperate them from the function with side effects

1. copy the function, name it as a query
2. remove codes with side effect and return the query result
3. add new query function to the all original methods.
4. remove return values from original
5. Test

```js
const getParseDataAndfeedModel = () => {
    const parsedData = data.map(el => parseData(data))
    feed2AllModels(parsedData);
    return parsedData;
}

// seperate pure query function
const queryParsedData = () => {
    return data.map(el => parseData(data));
}

const feedModel = () => {
    feedModel(queryParsedData(data));
}
```