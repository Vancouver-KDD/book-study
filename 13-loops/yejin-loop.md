# loop

## Quick preview 
반복문을 파이프라인으로 대체해 처리과정을 연산으로 처리한다.  

### example

```java
//before
 List<Integer> list = Arrays.asList(3, 4, 6, 12, 20);

 for(int i=0; i< list.size(); i++){
     if(list.get(i) % 5 == 0) {
         System.println.out(list.get(i));
     }
 }

 
//after

 list.stream()
            .filter(num -> num % 5 == 0)
            .forEach(System.out::println);
```