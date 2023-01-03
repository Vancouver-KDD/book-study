# [Subin] Interpreter

**Expression.java**
```
interface Expression {
    boolean interpreter(Stringn con);
}
```

**TerminalExpression.java**
```
class TerminalExpression implements Expression {
    String data;

    public TerminalExpression(String data) {
        this.data = data;
    }

    public boolean interpreter(String con) {
        if(con.contains(data)) {
            return true;
        } else {
            return false;
        }
    }
}
```

**OrExpression.java**
```
class OrExpression implements Expression 
{
    Expression expr1;
    Expression expr2;
  
    public OrExpression(Expression expr1, Expression expr2) 
    {
        this.expr1 = expr1;
        this.expr2 = expr2;
    }
    public boolean interpreter(String con) 
    {        
        return expr1.interpreter(con) || expr2.interpreter(con);
    }
}
```

**AndExpression.java**
```
class AndExpression implements Expression 
{
    Expression expr1;
    Expression expr2;
  
    public AndExpression(Expression expr1, Expression expr2) 
    { 
        this.expr1 = expr1;
        this.expr2 = expr2;
    }
    public boolean interpreter(String con) 
    {        
        return expr1.interpreter(con) && expr2.interpreter(con);
    }
}
```

**InterpreterPattern.java**
```
class InterpreterPattern
{
  
    public static void main(String[] args) 
    {
        Expression person1 = new TerminalExpression("Kushagra");
        Expression person2 = new TerminalExpression("Lokesh");
        Expression isSingle = new OrExpression(person1, person2);
          
        Expression vikram = new TerminalExpression("Vikram");
        Expression committed = new TerminalExpression("Committed");
        Expression isCommitted = new AndExpression(vikram, committed);    
  
        System.out.println(isSingle.interpreter("Kushagra"));
        System.out.println(isSingle.interpreter("Lokesh"));
        System.out.println(isSingle.interpreter("Achint"));
          
        System.out.println(isCommitted.interpreter("Committed, Vikram"));
        System.out.println(isCommitted.interpreter("Single, Vikram"));
  
    }
}
```