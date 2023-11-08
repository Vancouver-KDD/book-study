# Algorithm Speed
- estimating the resources that algorithms use—time, processor, memory, and so on.

## WHAT DO WE MEAN BY ESTIMATING ALGORITHMS? 
- Most nontrivial algorithms handle some kind of variable input— 
  - sorting  n strings, inverting an m x n matrix, 
  - or decrypting a message with an n-bit key.
- If the relationship were always linear
  - However, most significant algorithms are not linear. 
  - The good news is that many are sublinear.
  - ![lines.jpg](images%2Flines.jpg)
    - Ex) Binary search
  - The bad news is that other algorithms are considerably worse than linear.

## BIG-O NOTATION 
- The Big-O notation, written O(), is a mathematical way of dealing with approximations.
- The O() notation puts an upper bound on the value of the thing we’re measuring (time, memory, and so on).
> O(n2/2+3n) == O(n2/2) == O(n2)

1. O(1): Constant (access element in array, simple statements)
2. O(lg n): Logarithmic (binary search). The base of the logarithm doesn’t matter, so this is equivalent O(logn). 
3. O(n): Linear (sequential search)
4. O(n lg n): Worse than linear, but not much worse. (Average runtime of quicksort, heapsort)
5. O(n2): Square law (selection and insertion sorts)
6. O(n3): Cubic (multiplication of two  matrices)
7. O(Cn): Exponential (traveling salesman problem, set partitioning)

![big-o.jpg](images%2Fbig-o.jpg)
## COMMON SENSE ESTIMATION
- Simple loops
- Nested loops
- Binary chop
- Divide and conquer
- Combinatoric

## ALGORITHM SPEED IN PRACTICE
> Tip 63 - Estimate the Order of Your Algorithms
- an algorithm thiat is O(n2) -> find a divide-and-conquer approach == O(n lg n) 
- Measure the time or space and draw a grap
- O(n2) VS O(n lg n) <- complex? input values?
> Tip 64 - Test Your Estimates
- If it’s tricky getting accurate timings, 
  - use code profilers to count the number of times the different steps 
    - in your algorithm get executed, and plot these figures against the size of the input.


### Best Isn’t Always Best 
- You also need to be pragmatic about choosing appropriate algorithms
  - — the fastest one is not always the best for the job.
- Also be wary of premature optimization. 
  - It’s always a good idea to make sure an algorithm really is a bottleneck 
    - before investing your **precious** time trying to improve it.
