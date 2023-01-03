> Decouple an abstraction from its implementation so that the two can vary independently.  
> 기능계층과 구현계층의 분리로 시스템의 확장성과 유지보수성을 높이는 패턴


# 기능 클래스 계층
```java
public class Display {
	
	// impl 필드는 Display 구현을 나타내는 인스턴스 입니다. 
	// 이 필드가 두 클래스 계층의 '다리'가 됩니다.
	private DisplayImpl impl; 

	public Display(DisplayImpl impl) {
		this.impl = impl;
	}
	
	public void open(){
		impl.rawOpen();
	}
	
	public void print(){
		impl.rawPrint();
	}
	
	public void close(){
		impl.rawClose();
	}
	
	public final void display(){
		open();
		print();
		close();
	}

}
```

# 구현클래스
```java
public abstract class DisplayImpl {
	public abstract void rawOpen();
	public abstract void rawPrint();
	public abstract void rawClose();
}

```

# 구현클래스 구현
```java
// 구현 클래스 계층
public class StringDisplayImpl extends DisplayImpl {
	
	private String string;			   // 표시해야 할 문자열
	private int width;				   // 바이트 단위로 계산할 문자열의 '길이'
	

	public StringDisplayImpl(String string) {		// 생성자에서 전달된 문자열 string을
		this.string = string;						// 필드에 기억해둔다.
		this.width = string.getBytes().length; 	    // 그리고 바이트 단위의 길이도 필드에 기억해두고 나중에 사용한다.
	}

	@Override
	public void rawOpen() {
		printLine();
	}

	@Override
	public void rawPrint() {
		System.out.println("|" + string + "|");    // 앞뒤에 "|" 를 붙여서 표시한다.
	}

	@Override
	public void rawClose() {
		printLine();

	}
	
	private void printLine() {
		System.out.print("+");		           // 테두리의 모서리를 표현하는 "+" 마크를 표시한다.
		for (int i = 0; i < width; i++) {	   // width개의 "-"를 표시해서
			System.out.print("-");			   // 테두리 선으로 이용한다.
		}
		System.out.println("+");	           // 테두리 모서리를 표시하는 "+" 마크를 표시한다.
	}

}

```

# 사용코드
```java
public class Main {
	public static void main(String[] args) {
		
		Display d1 = new Display(new StringDisplayImpl("Hello, Korea!"));
		Display d2 = new CountDisplay(new StringDisplayImpl("Hello, World!"));
		
		CountDisplay d3 = new CountDisplay(new StringDisplayImpl("Hello, Universe!"));
		
		d1.display();
		d2.display();
		d3.display();
		
		d3.multiDisplay(5);
	}
}

```