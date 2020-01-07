package org.parosproxy.paros.db;

import java.sql.SQLException;

public interface DatabaseListener {

    public void databaseOpen(DatabaseServer dbServer) throws SQLException;
    
}
