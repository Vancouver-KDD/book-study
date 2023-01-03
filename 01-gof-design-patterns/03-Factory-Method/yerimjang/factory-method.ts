// Reference: https://refactoring.guru/design-patterns/factory-method/typescript/example
abstract class Creator {
  public abstract factoryMethod(): Product

  public someOperation(): string {
      const product = this.factoryMethod()
      return `Creator: The same creator's code has just worked with ${product.operation()}`
  }
}

class ConcreteCreator1 extends Creator {
  public factoryMethod(): Product {
      return new ConcreteProduct1()
  }
}

class ConcreteCreator2 extends Creator {
  public factoryMethod(): Product {
      return new ConcreteProduct2()
  }
}

interface Product {
  operation(): string
}

class ConcreteProduct1 implements Product {
  public operation(): string {
      return '{Result of the ConcreteProduct1}'
  }
}

class ConcreteProduct2 implements Product {
  public operation(): string {
      return '{Result of the ConcreteProduct2}'
  }
}

function test3(creator: Creator) {
  console.log('test3: I\'m not aware of the creator\'s class, but it still works.')
  console.log(creator.someOperation())
}

console.log('ConcreteCreator1:')
test3(new ConcreteCreator1())
console.log('')

console.log('ConcreteCreator2:')
test3(new ConcreteCreator2())
