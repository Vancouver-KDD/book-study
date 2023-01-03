package me.whiteship.designpatterns._02_structural_patterns._07_bridge.brian;

import java.util.HashMap;
import java.util.Map;

public class OracleDBConnector implements DBConnector {

    private final Map<String, String> tableValues;

    public OracleDBConnector() {
        this.tableValues = new HashMap<>();
    }

    @Override
    public boolean connectDB(String ip, String id, String password) {
        System.out.println("====Oracle Connected!====");
        return true;
    }

    @Override
    public boolean cudQuery(String query) {
        String[] querySplit = query.split(" ");
        if(querySplit[0].equalsIgnoreCase("INSERT")) {
            String table =  querySplit[2];
            String value =  querySplit[querySplit.length-1];
            this.tableValues.put(table, value);
        } else if(querySplit[0].equalsIgnoreCase("UPDATE")) {
            String table =  querySplit[1];
            String value =  querySplit[querySplit.length-1];
            if(this.tableValues.containsKey(table))
                this.tableValues.put(table, value);
        } else if(querySplit[0].equalsIgnoreCase("DELETE")) {
            String table =  querySplit[querySplit.length-1];
            if(this.tableValues.containsKey(table))
                this.tableValues.remove(table);
        }
        return true;
    }

    @Override
    public String readQuery(String query) {
        String[] querySplit = query.split(" ");
        String table = querySplit[querySplit.length-1];
        return this.tableValues.get(table);
    }

    @Override
    public void disconnectDB() {
        System.out.println("====Oracle Disconnected!====");
    }
}
