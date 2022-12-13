# Comments

Comments are good themselves. 
However, if the comment exists because the code is bad, we should do refactoring the code and see the comments is superfluous.

Instead of writing comments for bad code, we can write descriptive code.

For describing the code block, 

1. Try Extract Function to extract code block which can be described.
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
2. Change Function Declaration as a appropriate name
3. Add Assertion to represent required state of the system.
```ts
const getTotalIncome = (invoices: Invoice[]) => {
    let total = 0;
    for (const invoice of invoices) {
        total += invoice.amount
    }
    return total
}
```