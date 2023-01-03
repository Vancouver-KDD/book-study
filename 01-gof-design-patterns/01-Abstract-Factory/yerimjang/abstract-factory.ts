// Reference: https://refactoring.guru/design-patterns/abstract-factory/typescript/example
interface AbstractFactory {
  createProductA(): AbstractProductA

  createProductB(): AbstractProductB
}

class ConcreteFactory1 implements AbstractFactory {
  public createProductA(): AbstractProductA {
      return new ConcreteProductA1()
  }

  public createProductB(): AbstractProductB {
      return new ConcreteProductB1()
  }
}

class ConcreteFactory2 implements AbstractFactory {
  public createProductA(): AbstractProductA {
      return new ConcreteProductA2()
  }

  public createProductB(): AbstractProductB {
      return new ConcreteProductB2()
  }
}

interface AbstractProductA {
  usefulFunctionA(): string
}

class ConcreteProductA1 implements AbstractProductA {
  public usefulFunctionA(): string {
      return 'The result of the product A1.'
  }
}

class ConcreteProductA2 implements AbstractProductA {
  public usefulFunctionA(): string {
      return 'The result of the product A2.'
  }
}

interface AbstractProductB {
  usefulFunctionB(): string
  anotherUsefulFunctionB(collaborator: AbstractProductA): string
}

/**
* These Concrete Products are created by corresponding Concrete Factories.
*/
class ConcreteProductB1 implements AbstractProductB {

  public usefulFunctionB(): string {
      return 'The result of the product B1.'
  }

  public anotherUsefulFunctionB(collaborator: AbstractProductA): string {
      const result = collaborator.usefulFunctionA()
      return `The result of the B1 collaborating with the (${result})`
  }
}

class ConcreteProductB2 implements AbstractProductB {

  public usefulFunctionB(): string {
      return 'The result of the product B2.'
  }

  public anotherUsefulFunctionB(collaborator: AbstractProductA): string {
      const result = collaborator.usefulFunctionA()
      return `The result of the B2 collaborating with the (${result})`
  }
}

function test1(factory: AbstractFactory) {
  const productA = factory.createProductA()
  const productB = factory.createProductB()

  console.log(productB.usefulFunctionB())
  console.log(productB.anotherUsefulFunctionB(productA))
}

console.log('ConcreteFactory1:')
test1(new ConcreteFactory1())

console.log('')

console.log('ConcreteFactory2:')
test1(new ConcreteFactory2())
