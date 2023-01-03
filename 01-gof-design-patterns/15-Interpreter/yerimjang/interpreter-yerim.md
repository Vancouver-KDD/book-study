# Interpreter

### Intent
- Define a **represention for its grammar** along with an interpreter that uses the representation to interpret sentences in the language.

### Motivation
- If a particular kind of problem **occurs often enough**, then it might be worthwhile to express instances of the problem as sentences in a simple language.
      - *For example, Searching for strings that match a pattern*

### Applicability
- When the grammar is **simple**
      - *If not, tools such as parser generators are a better alternative in such cases.*
- when efficiency is not a critical concern

### Consequences
- It's easy to change and extend the grammar.
- Implementing the grammar is easy, too.
- Complex grammars are hard to maintain.
- Adding new ways to interpret expressions.
      - *If you keep creating new ways of interpreting and expression, then consider using the Visitor pattern to avoid changing the grammar classes.*

### Interpreter VS Composite
- Creating the abstract syntax tree.
      - *The Interpreter pattern doesn't explain how to create an abstract syntax tree.*
- Defining the Interpret operation.
      - *You don't have to define the Interpret operation in the expression classes.*
- Sharing terminal symbols with the Flyweight pattern.


### Implementation
- Structure
![alt text](https://johngrib.github.io/wiki/pattern/interpreter/interpreter.svg "Interpreter Structure")
<Br>
- Sample Code Structure
![alt text](https://velog.velcdn.com/images%2Fhoit_98%2Fpost%2Fd00ed04e-25ef-4640-b55d-6724f88ac4c7%2Fimage.png "Evaluator Diagram")
<Br>
- Sample Code (TypeScript)
~~~typescript
interface Expression {
  interpret(variables: Map<String, Expression>): number
}

class Value implements Expression {
  public value: number

  constructor(value: number) {
    this.value = value
  }

  interpret(variables:  Map<String, Expression>): number {
    return this.value
  }
}

class Plus implements Expression {
  leftOperand: Expression
  rightOperand: Expression

  constructor(left: Expression, right: Expression) {
    this.leftOperand = left
    this.rightOperand = right
  }

  interpret(variables:  Map<String, Expression>): number {
      return this.leftOperand.interpret(variables) + this.rightOperand.interpret(variables)
  }
}

class Minus implements Expression {
  leftOperand: Expression
  rightOperand: Expression

  constructor(left: Expression, right: Expression) {
    this.leftOperand = left
    this.rightOperand = right
  }

  interpret(variables:  Map<String, Expression>): number {
      return this.leftOperand.interpret(variables) - this.rightOperand.interpret(variables)
  }
}

class Variable implements Expression {
  private name: String

  constructor(name: String) {
    this.name = name
  }

  interpret(variables:  Map<String, Expression>): number {
    const expression = variables.get(this.name)
    if ( expression == null) {
      return 0
    } else {
      return expression.interpret(variables)
    }
  }
}

class Evaluator implements Expression {
  private syntaxTree: Expression

  constructor(expression: String) {
    const expressionStack: Expression[] = []
    const expressionSplitList: String[] = expression.split(" ")

    expressionSplitList.forEach(value => {
      if (value === "+") {
        const left = expressionStack.pop()
        const right = expressionStack.pop()
        if (right == undefined || left == undefined) {
          throw Error("Expression does not exist")
        }
        const subExpression = new Plus(right, left)
        expressionStack.push(subExpression)
      } else if (value === "-") {
        const left = expressionStack.pop()
        const right = expressionStack.pop()
        if (right == undefined || left == undefined) {
          throw Error("Expression does not exist")
        }
        const subExpression = new Minus(left, right)
        expressionStack.push(subExpression)
      } else {
        expressionStack.push(new Variable(value))
      }
    })

    const lastExpression = expressionStack.pop()
    if (lastExpression == undefined) {
      throw Error("Expression does not exist")
    }
    this.syntaxTree = lastExpression
  }

  interpret(variables: Map<String, Expression>): number {
    return this.syntaxTree.interpret(variables)
  }
}


function clientCode() {
  const expression: String = "w x z - +"
  const customCalculator: Evaluator = new Evaluator(expression)
  const variables: Map<String, Expression> = new Map<String, Expression>()
  variables.set("w", new Value(5))
  variables.set("x", new Value(10))
  variables.set("z", new Value(42))
  const result: number = customCalculator.interpret(variables)
  console.log(result)
}

clientCode()
~~~


### Reference
- Design Patterns, Elements of Reusable Object-Oriented Software
- https://velog.io/@hoit_98/%EB%94%94%EC%9E%90%EC%9D%B8-%ED%8C%A8%ED%84%B4-Interpreter-%ED%8C%A8%ED%84%B4
- https://zetawiki.com/wiki/%EC%9D%B8%ED%84%B0%ED%94%84%EB%A6%AC%ED%84%B0_%ED%8C%A8%ED%84%B4
