# [Subin] Chain of Responsibility

**Chain.java**
```
public class Chain {
    Processor chain;

    public Chain() {
        buildChain();
    }

    private void buildChain() {
        chain = new NegativeProcessor(new ZeroProcessor (new PositiveProcessor(null)));
    }

    public void process(Number request) {
        chain.process(request);
    }
}
```

**Processor.java**
```
abstract class Processor {
    private Processor nextProcessor;
    
    public Processor(Processor nextProcessor) {
        this.nextProcessor = nextProcessor;
    }

    public void process(Number request) {
        if (nextProcessor != null) {
            nextProcessor.process(request);
        }
    }
}
```

**Number.java**
```
class Number {
    private int number;

    public Number(int number) {
        this.number = number;
    }

    publi int getNumber() {
        return number;
    }
}
```

**NegativeProcessor.java**
```
class NegativeProcessor extends Processor {
    public NegativeProcessor(Processor nextProcessor) {
        super(nextProcessor);
    }

    public void process(Number request) {
        if (requst.getNumber() < 0) {
            System.out.println("NegativeProcessor: " + request.getNumber());
        } else {
            super.process(request);
        }
    }
}
```


**ZeroProcessor.java**
```
class ZeroProcessor extends Processor {
    public ZeroProcessor(Processor nextProcessor) {
        super(nextProcessor);
    }

    public void process(Number request) {
        if (request.getNumber() == 0) {
            System.out.println("Zero Processor: " + requst.getNumber());
        } else {
            super.process(request);
        }
    }
}
```

**PositiveProcessor.java**
```
class PositiveProcessor extends Processor {
    public PositiveProcessor (Processor nextProcessor) {
        super(nextProcessor);
    }
    public void process(Number request) {
        if (reqeust.getNumber() > 0) {
            System.out.println("PositiveProcessor: " + request.getNumber());
        } else {
            super.process(request);
        }
    }
}
```

**TestChain.java**
```
class TestChain {
    public static void main(String[] args) {
        Chain chain = new Chain();

        chain.process(new Number(90));
        chain.process(new Number(-50));
        chain.process(new Number(0));
        chain.process(new Number(91));
    }
}
```