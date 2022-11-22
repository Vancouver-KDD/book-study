Divergent Change 와 비슷하지만 반대이다. shotgun surgery 같은경우에는 코드를 한부분만 변경할수있는것을 여러군데 덕지덕지 변경할때를 일컫는 말이다. 코드를 여러군데 변경할경우 한곳만 변경하는것보다 훨씬더 이해하기 힘들어지고, 그래서 버그가 생길 가능성도 높아진다.

​

예:

Before: 

function getOrderData() {

return total

}

const total = getOrderData()

const total = getOrderData()

const total = getOrderData()

const total = getOrderData()

​

Shotgun Surgery:

function getOrderData() {

return {total, tax}

}

const {total, tax} = getOrderData()

const {total} = getOrderData()

const {total} = getOrderData()

const {total} = getOrderData()

​

Better Surgery:

function getOrderDataWithTax() {

return {total, tax}

}

​

const {total, tax} = getOrderDataWithTax()

const total = getOrderData()

const total = getOrderData()

const total = getOrderData()
