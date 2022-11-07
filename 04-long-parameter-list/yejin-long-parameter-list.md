# Long parameter list

## Quick preview
It is confusing when you call some function and you have to fill out tons of parameter list. 

### 1) Remove flag argument
If there is a flag argument in your function, remove the flag argument and seperate each execution by own function.   
I used to do this if there's a lot of options for one function.  
It makes developers confused and also code analysis tools confused.
It is worse if the flag argument is boolean, because it's harder to get the meaning of it at a glance.

##Example
```java
//before
// Use boolean variable to check if the baby is boy and change the formula
public int expectedHeight(boolean isBoy, double momHeight,double dadHeight ){
        if(isBoy){
            return(momHeight+dadHeight+13)/2;
        }
        return(momHeight+dadHeight-13)/2;
}

//after
public int boyExpectedHeight(double momHeight,double dadHeight ){
        return(momHeight+dadHeight+13)/2;
}

public int girlExpectedHeight(double momHeight,double dadHeight ){
        return(momHeight+dadHeight-13)/2;
}

```

### 2) Combine functions into class
If some functions operate similarly or closely and have a common arguments, consider of making it into a class.

##Example
```java
//before
// Those functions have similar structure and the same argument.
public int boyExpectedHeight(double momHeight,double dadHeight ){
        return(momHeight+dadHeight+13)/2;
}

public int girlExpectedHeight(double momHeight,double dadHeight ){
        return(momHeight+dadHeight-13)/2;
}

//after
class Parent {
    private double momHeight;
    private double dadHeight; 
    
    double boyExpectedHeight(){
        return (this.momHeight+this.dadHeight+13) / 2; 
    }

    double girlExpectedHeight(){
        return (this.momHeight+this.dadHeight-13) / 2;
    }
}
```
