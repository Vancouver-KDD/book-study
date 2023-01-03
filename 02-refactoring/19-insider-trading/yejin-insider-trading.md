#insider trading

## quick preview
한 모듈이 다른 모듈의 메서드, 정보에 너무 많이 접근할때를 말한다. 
같은 모듈 관련해서는 같이 일할 수 있도록 move function 등을 이용해서 조정해준다. 

~~~java
class Enrolled {
    public boolean isPass(Student student) {
        return (student.getTotalScore() > 80 && validation)? true : false; 
    }
}

class Student {
    private int totalScore;
    private boolean validation;
//...
}

// move function 으로 해결

class Student {
    private int totalScore;
    private boolean validation;
    public boolean isPass() {
        return (this.totalScore> 80 && this.validation)? true : false;
    }
//...
}
~~~