# Shell Games

### Woodworker
- Every woodworker need a goold, solid, reliable workbench.
- Somewhere to hold work pieces at a conventient height while there're being shaped. 

### Programmer
- Manipulating files of text
- Workbench is the command shell.
    - Invoke full repertoire of tools, using pipes to combine them
    - Launch application / debugger / browsers, editors and utilities
    - Search files
    - Query the status of the system and filter output.
    - Build complex macro commands for activities, perform often

### GUI interfaces
#### pros
- Wonderful and can be faster and more convenient for some simple operation
  - Moving files, reading and writing email, building and deploying project

#### cons
- Might not use all the full capabilities of the environment.
  - Automate command tasks
  - Cannot combine tools to create customized macro tools
  - A benefit of GUI is WYSIWYG - what you see is what you get
  - The disadvantage is WYSIAYG - what you see is all you get.

- GUI environments are normally limited to the capabilities that their designers intented.
- To integrate a code preprocessor into the IDE, the hooks for this capability should provided by designer of the IDE

> Tip 26 - Use the Power of Command Shells

Gain familiarity with the shell, and you'll find your productivity soaring. 

### Example Shell script
```
grep '^import ' *.java |
sed -e's/.*import *//' -e's/;.*$//' |
sort -u >list
```
- To create a list of all the unique packages names explicitly imported in Java Code.
- The list is stored in a file called 'list'
- It looks difficult but invest some energy in becoming familiar with shell then things will soon start falling into place.

## A Shell of Your Own
In the same way that a woodworker will customize their workspace, a developer should customize their shell. Common changes include setting color themes, configuration a prompt and aliases and shell functions.

### Setting color themes
### Configuring a prompt
- Prompt can be configured to display just about any information you might want.
- Simple Prompts / a shortened current directoy name / version control status / time

### Aliases and shell functions
- Simplify your workflow by turnning commands you use a lot into simple aliases.
```
alias apt-up='sudo apt-get update && sudo apt-get upgrade'
```
- To avoid to delete files accidentally, write an alias so taht it will always prompt.
```
alias rm ='rm -iv'
```

### Command completion
- Most shell will complete the names of commands and files
- Type the first few characters, hit tab, and it'll fill in what it can. 

## Challenges
- Are there things that you're currently doing manually in a GUI? Do you ever pass instructions to colleagues that involve a number of individual "clic this button", "select this item" steps? Could these be automated?
- Whenever your move to a new environment, make a point finding out what shell are available. See if you can bring your current shell with you.
- Investing alternatives to your current shell. If you come across a problem your shell can't address, see if an alternative shell would cope better.