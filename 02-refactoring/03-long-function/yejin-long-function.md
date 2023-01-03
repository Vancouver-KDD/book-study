# Long function

## Quick preview
The longer a function is, the harder it is to understand.
If you have to make comment about your function, consider decomposing it by extracting function.

## How to refactor it
The most common method is extracting function.    
But there are cases that you need to cope with many parameters or temporary variables. 
### 1) Replace temporary variables into own function.     
  Especially when you find out variables are calculated in the same way in different places.
* Variables have to calculate once and reuse it only by reading.
## Example
```java
//before
//Use temporary variable that calculates total battery
class Airpods {
    private int leftBattery;
    private int rightBattery;
    
    boolean isNeedCharging(){
        int totalBattery = this.leftBattery + this.rightBattery;
        return totalBattery < 40 ? true : false;
    }
    
}

//after 
class Airpods {
    private int leftBattery;
    private int rightBattery;
    
    int getTotalBattery(){
        return this.leftBattery+this.rightBattery;
    }

    boolean isNeedCharging(){
        return getTotalBattery() <40 ? true : false;
    }

}

```

### 2) Preserve whole object 
If you see some function that require parameters from a single record, 
pass the whole object.
You don't have to alter the parameter lists evne it the function needs more data from that object.

## Example
```java
//before
//caller
Rectangular rectangular =new Rectangular();
int row = rectangular.row;
int col = rectangular.col; 
        ...
int blueOne = calculate(row, col); 

//after 
int blueOne = calculate(rectangular);
```

### 3) Replace Function with Command
When it is useful to make function encapsulate in Command object, make a class for a function. 
You can add undo() method or getter method in that class.  

## example 
```java
// before
int shiftedArray = shiftArray(new int[]{1,2,3,4,5},3);
Arrays.stream(shiftedArray.getArray()).forEach(num -> System.out.println(num));

//after
// Long function that execute shifting array => make it a class 
// put main logic in execute method & add print and getter method

ShiftArray shiftedArray = new ShiftArray(new int[]{1,2,3,4,5},   3);
shiftedArray.execute();
shiftedArray.print();

```



