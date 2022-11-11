1. Mysterious Name

코드를 짤때 가장 힘든것이 바로 이름짓기 라는 말이 있을정도로, 좋은 이름을 짓는것은 쉽지 않은 여정이다.

가장 좋은 이름은, 사람들이 알아들을수있는 선에서 가장 짧은게 좋다, 즉 코드를 누가 언제 읽느냐에 따라서 가장 좋은 이름이 달라지게 된다. 대표적으로 사람들이 동의하는 더 나은 이름짓기에 대해서 알아보자.

​

Tip1: camcel case for variable and function / pascal case for class/component

Before: const FOO = 'bar'

After: const foo = 'bar'

​

Before: const hello_world = 'ee'

After: const helloWorld = 'ee'

​

Before: class HELLLO_WORLD

After: class HelloWorld

​

Before: function APPBAR() {}

After: function AppBar() {}

 

Tip2: start private variables, functions with underscore 예: const _secretVar

Tip3: start event handler functions with on or handle

Example: onButtonClick(), handleSubmit()

​

Tip4: As short as possible without using abbreviations. (Abbreviation that everyone understands is OK)

Before: calculateOrderItemsTotal

After: getTotal, getItemsTotal, getOrderTotal, calcTotal
