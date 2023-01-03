package me.whiteship.designpatterns._02_structural_patterns._07_bridge.brian;

public class ClientApp {
    public static void main(String[] args) {
        QueryExecutor queryExecutor = new JDBCQueryExecutor(new OracleDBConnector(), "192.168.0.1", "id", "pwd");
        System.out.println("Create");
        System.out.println("=> " + queryExecutor.create("StringTable", "First_Value"));
        System.out.println("Read");
        System.out.println("=> " + queryExecutor.read("StringTable"));
        System.out.println("Update");
        System.out.println("=> " + queryExecutor.update("StringTable", "Second_Value"));
        System.out.println("Read");
        System.out.println("=> " + queryExecutor.read("StringTable"));
        System.out.println("Delete");
        System.out.println("=> " + queryExecutor.delete("StringTable"));
        System.out.println("Read");
        System.out.println("=> " + queryExecutor.read("StringTable"));
    }
}
