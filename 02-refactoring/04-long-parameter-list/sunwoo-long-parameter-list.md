# Long Parameter List
## Preserve Whole Object
```ts
interface IInvoice {
    name: string
    paymentMethod: string
    payments: {
        amount: number
        currency: string
    }
}

// derives paymentMethod, payments from IInvoice object
const printInvoice = (paymentMethod: string, amount: number, currency: string): void => {
    console.log(`method: ${paymentMethod} & amount: ${currency} ${amount}`)
}

// migration for printInvoice function
const migratedPrintInvoice = (invoice: IInvoice): void => {
    printInvoice(invoice.paymentMethod, invoice.payments.amount, invoice.payments.currency)
}

// preserve whole object function
const wholePrintInvoice = (invoice: IInvoice): void => {
    console.log(`method: ${invoice.paymentMethod} & amount: ${invoice.payments.currency} ${invoice.payments.amount}`)
}

const invoice: IInvoice = {
    name: 'Sunwoo',
    paymentMethod: 'Credit card',
    payments: {
        amount: 50,
        currency: 'CAD'
    }
}

/**
 * test output
 * method: Credit card & amount: CAD 50
 */
printInvoice(invoice.name, invoice.paymentMethod, invoice.payments.amount, invoice.payments.currency)
migratedPrintInvoice(invoice)
wholePrintInvoice(invoice)
```