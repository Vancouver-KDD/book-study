하나의 기능을 변경하고싶을때 2곳이상의 코드를 변경해야한다면 Divergent Change 일 가능성이 크다.

다른말로 Code Fragmentation 이라고도 불리며, 비슷한 기능을 하는 코드를 같이 묶는것에 실패할때 생긴다.

비슷한 기능을 하는것을 보통 함수로 분리를 시키지만 꼭 그렇게 하지 않고 block encapsulation 또는 단순히 바로 옆으로 옮기는것만으로도 도움이 된다.

​

예: React 에서는 hooks 가 나오기 전에 Divergent Change 의 문제가 심각했다.

class Example extends Component {

let timeoutID1

let intervalID2

componentDidMount(){

   this.timeoutID1 = setTimeout(()=> doFunctionA(), 1000)

   this.intervalID2 = setInterval(()=> doFunctionB(), 5000)

}

componentWillUnmount() {

   clearTimeout(this.timeoutID1)

   clearInterval(this.intervalID2)

}

​

}

​

이렇게 functionA 와 functionB 가 섞여있지만 이것을 분리하기 위해서는 아무것도 render 되지 않는 새로운 renderless Component 를 만들어야 했기때문이다.

​

React hooks 가 나오고나서는 이 문제가 해결되었다.

​

function Example() {

   useEffect(()=>{

     const timeoutID1 = setTimeout(()=> doFunctionA(), 1000)

     return ()=>clearTimeout(timeoutID1)

   }

​

   useEffect(()=>{

     const intervalID2 = setInterval(()=> doFunctionB(), 5000)

     return ()=>clearInterval(this.intervalID2)

   }

}

​
