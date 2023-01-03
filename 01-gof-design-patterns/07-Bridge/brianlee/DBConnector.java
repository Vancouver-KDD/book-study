package me.whiteship.designpatterns._02_structural_patterns._07_bridge.brian;

public interface DBConnector {
    boolean connectDB(String ip, String id, String password);
    boolean cudQuery(String query);
    String readQuery(String query);
    void disconnectDB();
}
