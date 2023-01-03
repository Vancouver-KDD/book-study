# Feature Envy
When we modularize a program, we try to
* maximize the interaction insdie a zone(high cohesion)
* minimize interation betwwen zones(low coupling)

Feature Envy code smell is about when a function in module A has been used more than outside of module A.

## Solution
### Extract Function
```ts
interface IUser {
  name: string
  weekExpense: number
  monthExpense: number
}

class Recordsa {
  users: Array<IUser>
    
  getWeekReport() {
    const numOfUsers: number = this.users.length
    const total: number = this.users.map(e => e.weekExpense).reduce((prev, cur) => prev + cur)
    console.log('Weekly report')
    console.log(`Total expenses for all users ${total}`)
    console.log(`The average: ${total/numOfUsers}`)
  }
  getUserMonthReport() {
    const numOfUsers: number = this.users.length
    const total: number = this.users.map(e => e.monthExpense).reduce((prev, cur) => prev + cur)
    console.log('Monthly report')
    console.log(`Total expenses for all users ${total}`)
    console.log(`The average: ${total/numOfUsers}`)
  }
}

class Records {
  users: Array<IUser>
  
  
  getSum(expenses: Array<number>) {
    const sum = expenses.reduce((prev, cur) => prev + cur)
    return sum  
  }

  getAvg(total: number, numOfElements: number) { return total / numOfElements}
  getNumOfEles(users: Array<IUser>) { return users.length}

  printReport(total: number, numOfElements: number, title: string) {
    console.log(`${title} report`)
    console.log(`Total expenses for all users ${total}`)
    console.log(`The average: ${this.getAvg(total, numOfElements)}`)
  }

  extractExpense(users: Array<IUser>, type: string): Array<number> {
    let result: Array<number> = [];
    switch (type) {
      case 'month':
        result = users.map(el => el.monthExpense)
        break;
      case 'week':
        result = users.map(el => el.weekExpense)
        break;
      default:
        break;
    }
    return result
  }

  getWeekReport() {
    const type = 'week'
    this.printReport(this.getSum(this.extractExpense(this.users, type)), this.getNumOfEles(this.users), type)
  
  }
  getUserMonthReport() {
    const type = 'month'
    this.printReport(this.getSum(this.extractExpense(this.users, type)), this.getNumOfEles(this.users), type)
  }
}
```
### Move Function
```ts
class FoodDelivery {
    function printInvoice() {
        const totalPrice = this.order.totalPrice();
        const location = this.order.location()
        console.log("Invoice Summary")
        console.log(`Total price: ${totalPrice}`);
        console.log(`Address: ${location}`);
    }
}

class Order {
    get totalPrice () {
        return this.itemList.reduce((sum, item) => sum + item.price);
    }
    get location () {
        return this.address;
    }
}

class OrderWithInvoice {
    get totalPrice () {
        return this.itemList.reduce((sum, item) => sum + item.price);
    }
    get location () {
        return this.address;
    }
    function printInvoice() {
        const totalPrice = this.totalPrice();
        const location = this.location()
        console.log("Invoice Summary")
        console.log(`Total price: ${totalPrice}`);
        console.log(`Address: ${location}`);
    }
}
```