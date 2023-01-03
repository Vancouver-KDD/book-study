# Loops

The book explains that loops can be substituted by lambda functions but it would be only appliable for certain circumstances.

## Replace Loop with Pipeline

C++ supports lambda function call over collection type since c++11 as well. Most of modern languages have lambda function or functional programming paradigm as default.

The one thing to mind is that with concatenated & self-return lambda calls, it could be really hard to debug if any problem or exception happens. You will end up unwrapping the whole code again.  

### Example

```js
//Before
function acquireData(input) {
    const lines = input.split("\n");
    let firstLine = true;
    const result = [];
    for (const line of lines) {
        if (firstLine) {
            firstLine = false;
            continue;
        }
        if (line.trim() === "") continue;

        const record = line.split(",");
        if (record[1].trim() === "India") {
            result.push({city: record[0].trim(), phone: record[2].trim()});
        }
    }
    return result;
}

// After
function acquireData(input) {
    const lines = input.split("\n");
    return lines
        .slice  (1)
        .filter (line   => line.trim() !== "")
        .map    (line   => line.split(","))
        .filter (fields => fields[1].trim() === "India")
        .map    (fields => ({city: fields[0].trim(), phone: fields[2].trim()}))
        ;
}
```