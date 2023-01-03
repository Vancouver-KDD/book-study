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
