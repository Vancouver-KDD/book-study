# Duplicated Code

```ts
interface IUser {
  name: string
  weekExpense: number
  monthExpense: number
}

class Records {
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
```

```ts
class Records {
  users: Array<IUser>
  
  // extract sum logic
  getSum(expenses: Array<number>) {
    const sum = expenses.reduce((prev, cur) => prev + cur)
    return sum  
  }
  // extract avergae logic
  getAvg(total: number, numOfElements: number) { return total / numOfElements}
  // extract get number of elements logic
  getNumOfEles(users: Array<IUser>) { return users.length}

  // extract print logic
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