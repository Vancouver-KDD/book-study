# [Subin] Composite

- 여러 개의 객체들로 구성된 복합 객체와 단일 객체를 클라이언트에서 구별 없이 다루게 해주는 패턴
  - 전체-부분의 관계 (e.g. Directory-File)를 갖는 객체들 사이의 관계를 정의할 때 유용
  - 클라이언트 또한 전체와 부분을 구분하지 않고 동일한 인터페이스를 사용할 수 있음

~~~
import java.util.ArrayList;
import java.util.List;
  
  
// A common interface for all employee
interface Employee
{
    public void showEmployeeDetails();
}
  
class Developer implements Employee
{
    private String name;
    private long empId;
    private String position;
      
    public Developer(long empId, String name, String position)
    {
        // Assign the Employee id,
        // name and the position
        this.empId = empId;
        this.name = name;
        this.position = position;
    }
      
    @Override
    public void showEmployeeDetails() 
    {
        System.out.println(empId+" " +name+ " " + position );
    }
}
  
class Manager implements Employee
{
    private String name;
    private long empId;
    private String position;
   
    public Manager(long empId, String name, String position)
    {
        this.empId = empId;
        this.name = name;
        this.position = position;
    }
       
    @Override
    public void showEmployeeDetails() 
    {
        System.out.println(empId+" " +name+ " " + position );
    }
}
  
  
// Class used to get Employee List
// and do the opertions like 
// add or remove Employee
  
class CompanyDirectory implements Employee
{
    private List<Employee> employeeList = new ArrayList<Employee>();
        
    @Override
    public void showEmployeeDetails() 
    {
        for(Employee emp:employeeList)
        {
            emp.showEmployeeDetails();
        }
    }
        
    public void addEmployee(Employee emp)
    {
        employeeList.add(emp);
    }
        
    public void removeEmployee(Employee emp)
    {
        employeeList.remove(emp);
    }
}
  
// Driver class
public class Company
{
    public static void main (String[] args)
    {
        Developer dev1 = new Developer(100, "Lokesh Sharma", "Pro Developer");
        Developer dev2 = new Developer(101, "Vinay Sharma", "Developer");
        CompanyDirectory engDirectory = new CompanyDirectory();
        engDirectory.addEmployee(dev1);
        engDirectory.addEmployee(dev2);
           
        Manager man1 = new Manager(200, "Kushagra Garg", "SEO Manager");
        Manager man2 = new Manager(201, "Vikram Sharma ", "Kushagra's Manager");
           
        CompanyDirectory accDirectory = new CompanyDirectory();
        accDirectory.addEmployee(man1);
        accDirectory.addEmployee(man2);
       
        CompanyDirectory directory = new CompanyDirectory();
        directory.addEmployee(engDirectory);
        directory.addEmployee(accDirectory);
        directory.showEmployeeDetails();
    }
}
~~~

## Source
- https://www.geeksforgeeks.org/composite-design-pattern/