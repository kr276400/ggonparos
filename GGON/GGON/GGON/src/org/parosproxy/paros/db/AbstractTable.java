package org.parosproxy.paros.db;

import java.sql.Connection;
import java.sql.SQLException;

 public abstract class AbstractTable implements DatabaseListener {

    private Connection connection = null;
    private DatabaseServer server = null;
    
    public AbstractTable() {
    }
    
    public void databaseOpen(DatabaseServer server) throws SQLException {
        this.server = server;
        connection = null;
        reconnect(getConnection());
    }
    
    protected Connection getConnection() throws SQLException {
        if (connection == null) {
            connection = server.getNewConnection();            
        }
        return connection;
    }
    
    abstract protected void reconnect(Connection connection) throws SQLException;

}
