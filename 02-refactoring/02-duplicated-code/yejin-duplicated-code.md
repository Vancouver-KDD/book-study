# Duplicated code

## Quick preview
If you find the same code structure more than once, consider unifying them.

## How to refactor it
### Extract function 
To extract the same code and make it to new function.
You can reuse that function by calling it.

### Slide statement
It is much easier to read when related statements are together.

### Pull up method
If you have functions which have same code structure and function,
consider making a super class and implement it in the super class.   
Ex) If you have any common function in child class, you can declare it in the upper class
And you can use it in inheritance class.

### Example
Here is an example of refactoring duplicated code by those mechanics. 

```java
//before & refactoring goals
// Given an integer array nums, move all 0's to the end of it while maintaining the relative order of the non-zero elements.
//1.  Remove duplicated code in increasing index variable.  
//    => Use slide statement mechanic & cut off unnecessary "else -if" condition
//2.  Extract fragment of code that swap array's order into new function

public void moveZeroes(int[] nums) {
        if (nums == null || nums.length == 0) return;
        
        int firstIndex =0;
        int secondIndex =1; 
        int temp =0;
        
        while(secondIndex < nums.length){
            if(nums[firstIndex]==0 && nums[secondIndex]!=0){  // if zero is in front of non-zero value, switch
                temp = nums[firstIndex];
                nums[firstIndex]=nums[secondIndex];
                nums[secondIndex] = temp;
                ++firstIndex;
                ++secondIndex;
            }else if(nums[firstIndex]!=0){  // if non zero is in front of zero, you don't have to switch. Just move two pointers forward. 
                ++firstIndex;
                ++secondIndex;
            }else{  // if you compare two zero numbers, move second pointer forward to remember first zero number's index. 
                ++secondIndex;
            }
        }
           
    }

    
// after
public void moveZeroes(int[] nums) {
        if (nums == null || nums.length ==0) return;
        
        int firstIndex =0;
        int secondIndex =1;

        while(secondIndex < nums.length){
            if(nums[firstIndex]==0 && nums[secondIndex]!=0){  // if zero is in front of non-zero value, swap
                nums = swapOrder(nums, firstIndex, secondIndex);
            }
            
            if(nums[firstIndex]!=0) ++firstIndex;
            
            ++second;
        }

}

public int[] swapOrder(int[] nums, int firstIndex, int secondIndex){
        int temp=0;
        
        temp = nums[firstIndex];
        nums[firstIndex]=nums[secondIndex];
        nums[secondIndex] = temp;

        return nums;
}
        



```

