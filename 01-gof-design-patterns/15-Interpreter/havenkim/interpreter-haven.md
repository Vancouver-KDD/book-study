# Interpreter

### In One Liner

Make a state machine that understands what you are talking about.

It's pretty rare to find this pattern in action but still powerful approach if there are lots of highly repetited and mixed object behaviors. 

### Pros 

- If the behaviors of the program can be described in sentence, Interpreter pattern can maximize its usability and compatibility.

### Cons

- Do you really need to make your own language?

### Example

[Reference](https://en.wikipedia.org/wiki/Interpreter_pattern)

```c#
using System;
using System.Collections.Generic;

class Context
{
    public Stack<string> Result = new Stack<string>();
}

interface Expression
{
    void Interpret(Context context);
}
abstract class OperatorExpression : Expression
{
    public Expression Left { private get; set; }
    public Expression Right { private get; set; }

    public void Interpret(Context context)
    {
        Left.Interpret(context);
        string leftValue = context.Result.Pop();

        Right.Interpret(context);
        string rightValue = context.Result.Pop();

        DoInterpret(context, leftValue, rightValue);
    }

    protected abstract void DoInterpret(Context context, string leftValue, string rightValue);
}
class EqualsExpression : OperatorExpression
{
    protected override void DoInterpret(Context context, string leftValue, string rightValue)
    {
        context.Result.Push(leftValue == rightValue ? "true" : "false");
    }
}
class OrExpression : OperatorExpression
{
    protected override void DoInterpret(Context context, string leftValue, string rightValue)
    {
        context.Result.Push(leftValue == "true" || rightValue == "true" ? "true" : "false");
    }
}
class AndExpression : OperatorExpression
{
    protected override void DoInterpret(Context context, string leftValue, string rightValue)
    {
        context.Result.Push(leftValue == "true" && rightValue == "true" ? "true" : "false");
    }
}
class XORExpression : OperatorExpression
{
    protected override void DoInterpret(Context context, string leftValue, string rightValue)
    {
        if (leftValue == rightValue) 
            context.Result.Push("false");
        else
            context.Result.Push("true");
    }
}
class ValueExpression : Expression
{
    public string Value { private get; set; }

    public void Interpret(Context context)
    {
        context.Result.Push(Value);
    }
}
```

```C#
using System;
using Context;
using Expression;

class Program
{
    static void Main()
    {
        var context = new Context();
        var input = new ValueExpression();

        var expression = new OrExpression
        {
            Left = new EqualsExpression
            {
                Left = input,
                Right = new ValueExpression { Value = "4" }
            },
            Right = new EqualsExpression
            {
                Left = input,
                Right = new ValueExpression { Value = "four" }
            }
        };

        input.Value = "four";
        expression.Interpret(context);
        // Output: "true" 
        Console.WriteLine(context.Result.Pop());

        input.Value = "44";
        expression.Interpret(context);
        // Output: "false"
        Console.WriteLine(context.Result.Pop());
    }
}
```