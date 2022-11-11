​

2. Duplicated Code

반복된 코드는 아마 프로그래머들이 절대적으로 피해야할 가장 기본중에 기본지식이다.

이유는, 코드 쓰는시간, 관리시간, 그리고 버그갯수가 코드양의 비례하기때문에 반복된 코드는 함수로 분리시키면서 코드양을 줄일수 있기 때문이다. 코드를 DRY하게 하라는 말이 있는데 Don't Repeat Yourself 의 약자이며, 그것의 반대는 WET, Write Everything Twice 이다.

예: 

Before:

let total = 0

for(arr of item){ 

total += item.price

}

let total = 0

for(arr of item){ 

total += item.price

}

​

After:

function getTotal(arr){

const total = 0

for(arr of item){ 

total += item.price

}

return total

}

let total = getTotal(arr)

let total = getTotal(arr)
