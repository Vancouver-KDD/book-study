package me.whiteship.designpatterns._02_structural_patterns._07_bridge.brian;

public class JDBCQueryExecutor implements QueryExecutor {

    private final DBConnector dbConnector;
    private final String ip;
    private final String id;
    private final String password;

    public JDBCQueryExecutor(DBConnector dbConnector, String ip, String id, String password) {
        this.dbConnector = dbConnector;
        this.ip = ip;
        this.id = id;
        this.password = password;
    }

    @Override
    public boolean create(String table, String value) {
        if(dbConnector.connectDB(this.ip, this.id, this.password)) {
            boolean result =  dbConnector.cudQuery("INSERT INTO "+table+" VALUES "+value);
            dbConnector.disconnectDB();
            return result;
        } else {
            throw new IllegalArgumentException("Connection Error: " + ip);
        }
    }

    @Override
    public String read(String table) {
        if(dbConnector.connectDB(this.ip, this.id, this.password)) {
            String result =  dbConnector.readQuery("SELECT * FROM " + table);
            dbConnector.disconnectDB();
            return result;
        } else {
            throw new IllegalArgumentException("Connection Error: " + ip);
        }
    }

    @Override
    public boolean update(String table, String value) {
        if(dbConnector.connectDB(this.ip, this.id, this.password)) {
            boolean result =  dbConnector.cudQuery("UPDATE " + table+ " SET " + value);
            dbConnector.disconnectDB();
            return result;
        } else {
            throw new IllegalArgumentException("Connection Error: " + ip);
        }
    }

    @Override
    public boolean delete(String table) {
        if(dbConnector.connectDB(this.ip, this.id, this.password)) {
            boolean result =  dbConnector.cudQuery("DELETE Table " + table);
            dbConnector.disconnectDB();
            return result;
        } else {
            throw new IllegalArgumentException("Connection Error: " + ip);
        }
    }
}
