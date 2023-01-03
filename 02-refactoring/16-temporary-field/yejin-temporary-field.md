# Temporary field

## Quick preview
특정 상황에만 사용하는 필드는 코드를 해석하기 어렵게 한다.  temporary field 가 필요한 상황을 따로 클래스로 빼던가해서 필요한 애들끼리 묶어준다.   
특히 validation 이 필요한 경우는 따로 class 로 빼서 처리하는  Introduce special case 사용한다.  

## Example

```java
class Semester {
    private String type;
    private int duration;
    private boolean isMandatory = true;
    ...

    public void setMandatory() {
       if(type.equals("summer")) {
           isMandatory = false; 
       }
    }
}

Semester semester = new Semester();

int tuition; 
if(semester.getIsMandatory()){
    tuition = 5000;     
}else{
    tuition = 3000;
}

// after

class summerSession extends Semester{
    public int getTuition(){
        return 3000; 
    }
}
tuition = semester.getTuition();

```

