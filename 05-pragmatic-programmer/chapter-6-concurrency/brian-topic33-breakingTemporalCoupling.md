# Braking Temporal Coupling
1. Concurrency = Code
   - (This is often implemented using things such as fibers, threads, and processes)
2. Parallelism =  Need Hardware
   - (This might be multiple cores in a CPU, multiple CPUs in a computer, or multiple computers connected together)

> “What is temporal coupling all about?”, you may ask. It’s about time.
- There are two aspects of time that are important to us: 
  - concurrency (things happening at the same time) and 
  - ordering (the relative positions of things in time).
- Temporal coupling: Coupling in time
  1.  Method A must always be called before method B;
  2. only one report can be run at a time;

## LOOKING FOR CONCURRENCY 
- We’d like to find out what can happen at the same time, and what must happen in a strict order. 
- One way to do this is to capture the workflow using a notation such as the activity diagram.

> Tip 56 - analyze Workflow to Improve Concurrency

![pina-coladas.jpg](images%2Fpina-coladas.jpg)
- Steps of a robotic pina colada maker.
![robotic_pina_colada_maker.jpg](images%2Frobotic_pina_colada_maker.jpg)
- Active Diagram
![active_diagram_for_robotic_pina_colada_maker.jpg](images%2Factive_diagram_for_robotic_pina_colada_maker.jpg)

## OPPORTUNITIES FOR CONCURRENCY
- A bartender would need five hands to be able to run all the potential initial tasks at once.
- And that’s where the design part comes in. When we look at the activities, 
  - we realize that number 8, liquify, will take a minute. 
  - During that time, our bartender can get the glasses and umbrellas (activities 10 and 11) 
    - and probably still have time to serve another customer.

## OPPORTUNITIES FOR PARALLELISM
- A common pattern is to take a large piece of work, split it into independent chunks, 
  - process each in parallel, then combine the results.

> Example of the Elixir language works
> 1. When it starts, it splits the project it is building into modules, and compiles each in parallel.
> 2. Sometimes a module depends on another, in which case its compilation pauses until the results of the other module’s build become available.
> 3. When the top-level module completes, it means that all dependencies have been compiled.

## IDENTIFYING OPPORTUNITIES IS THE EASY PART 
- Back to your applications. We’ve identified places where it will benefit from concurrency and parallelism. 
- Now for the tricky part: 
  - how can we implement it safely. That’s the topic of the rest of the chapter.