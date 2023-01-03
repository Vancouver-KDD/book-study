package me.whiteship.designpatterns._02_structural_patterns._07_bridge.brian;

public interface QueryExecutor {
    boolean create(String table, String value);
    String read(String table);
    boolean update(String table, String value);
    boolean delete(String table);
}
