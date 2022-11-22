4. Long Parameter List

함수를 만들때, 함수가 필요한 데이터를 파라미터로 패스를 하는것이 좋다고 합니다.

만약, 함수가 필요한 모든 데이터가 파라미터로 들어온다고 하면, 그 함수를 pure function 이라고 부르죠.

그러면 그 함수는 global variable 에 영향을 받지 않기 때문에 다른곳에서 사용할때 좀더 쉽게 사용할수 있게 됩니다.

그리고 pure function 같은 경우에 cache 를 할수 있기때문에 속도가 많이 걸리는경우 장점이 크죠.

​

하지만 이렇게 필요한 모든 데이터를 따로따로 파라미터로 넣을경우 함수가 아주 길어지고 복잡해지는 부작용이 있습니다.

예: isItWorthIt(price, income, savings, age, wantedPeriod, nextSaleTimeInSeconds, utility, returnOnInvestment)

이렇게 되면 파라미터가 나중에 변경되게 되면 그걸 사용하는 모든 function 을 변경해야 하는 사태가 발생하게 됩니다.

더 나은 방법은 Pass by Object 입니다. 

const isItWorthParams = {price, income, savings, age, wantedPeriod, nextSaleTimeInSeconds, utility, returnOnInvestment}

예: isItWorthIt(isItWorthParams)

이렇게 할 경우, 파라미터를 변경하기 훨씬더 쉬워지게 됩니다.
