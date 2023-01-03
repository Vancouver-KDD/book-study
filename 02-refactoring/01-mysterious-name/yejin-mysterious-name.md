# Mysterious name

## Quick preview : What is "good name" in programming?
It gives us a hint of what it means and how it functions.   
If you find out wrong name of function,  it is crucial to change the name immediately so that you don't have to struggle with it again.

## How to refactor it
## 1)  Change the declaration
Change the name of function or parameters.
It is important to write code that easy to understand and transform.

-  Simple mechanic : find all references and change them into new one.

- Migrate mechanic :
1) Extract body part into new function
2) insert new function into old one with inline function
3) Once you ensure new function is working, remove old one.

## Example
 ``` java
  //before 
  class Student {
      private String name; 
      private int mathScore; 
      private int engScore; 

     // it is the sum of score but if you call this method outside of class, you can't be sure about what it means. 
      public int sum(){  
          return this.mathScore + this.engScore; 
      }
  }

  //after
      public int totalScore(){  
          return this.mathScore + this.engScore; 
      }
  
  // after 2 
  // if total score of student is widely used value, make it into class variable and calculate it in constructor. 
   
   class Student {
      private String name; 
      private int mathScore; 
      private int engScore; 
      private int totalScore; 

      Student(){
          this.totalScore = this.mathScore+ this.engScore; 
      }

  }


```

## 2) Rename variables
If variable is not widely used (ex. variable in one line lambda expression), it's okay to use simple letter.

But if it's widely used, consider encapsulating variables.
It makes you rename variable easily.


### Example
```java
//before
// I want to test board update function ,and names of "title", "content" variable are not clear. 
    @Test
    @DisplayName("board update test")
    public void postsUpdate() throws Exception{
        String title ="updated";
        String content = "updated_content";
        Long id = postsRepository.findAll().get(0).getId();
        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder().title(title).content(content).build();
    ...

// after
// Change name of variable so that I can be sure about it 
    @Test
    @DisplayName("board update test")
    public void postsUpdate() throws Exception{
        String updateTitle ="updated";
        String updateContent = "updated_content";
        Long id = postsRepository.findAll().get(0).getId();
        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder().title(updateTitle).content(updateContent).build();
    ...

```

## 3) Rename fields

### Example
```java
//before
// "name" and "title" fields make me confused.
  public class PostsResponseDto {
    private String title;
    private String content;
    private String name;
    private Long id;
  ...


//after
// change field name to more meaningful one
  public class PostsResponseDto {
    private String title;
    private String content;
    private String author;
    private Long id;
  ...

```



