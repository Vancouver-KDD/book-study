# Feature envy

## Quick preview
If a class has too many interactions with another class, it is considered as "Feature Envy" code smell.  
다른 클래스나 인터페이스의 값이나 메소드 너무 많이 호출할때를 말한다.  
It needs many changes when another class needs to be changed.
=> too much coupling 인것임.      

## Example 

```javascript
class Rectangle {
    constructor() {
        this.height = 1;
        this.width = 1;
    }
}
class ShapeCalculator {
    constructor() {
        this.rectangle = new Rectangle();
    }
    getRectangleArea() {
        return this.rectangle.height * this.rectangle.width;
    }
}

// ShapeCalculator uses too much information of Rectangle
// Use Move function  =>  getArea() to Rectangle class 

class Rectangle {
    constructor() {
        this.height = 1;
        this.width = 1;
    }
    
    getArea(){
        return this.height*this.width;
    }
}
class ShapeCalculator {
    constructor() {
        this.rectangle = new Rectangle();
    }
    getRectangleArea() {
        return this.rectangle.getArea();
    }
}


```

[Code Smells in JavaScript](https://betterprogramming.pub/more-code-smells-in-javascript-113067dbd89f)