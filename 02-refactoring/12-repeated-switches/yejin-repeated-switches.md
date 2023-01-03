# Repetitive switches 

## Quick preview
Repetitive swithches can be replaced with polymorphism. 

## Example 

```java

//before
    switch(type){
        case 1:  return height * width; 
        case 2:  return 1/2 * (height * width);
        ...
        
    }

//after
public class Rectangle implements Shapes {
    public double getArea(int height, int width){
        return height*width; 
    }
}

public class Triangle implements Shapes {
    public double getArea(int height, int width){
        return 1/2* (height*width);
    }
}

Shape shape ; 
switch(type){
    case 1 :  shape = new Rectangle();
    case 2:   shape = new Triangle(); 
}

double area = shape.getArea(3, 20);


```