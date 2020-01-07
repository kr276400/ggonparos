package org.parosproxy.paros.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.parosproxy.paros.network.HttpMalformedHeaderException;



public class TableSession extends AbstractTable {

    private static final String SESSIONID	= "SESSIONID";
    private static final String SESSIONNAME	= "SESSIONNAME";
    private static final String LASTACCESS	= "LASTACCESS";
    
    private PreparedStatement psReadDate = null;
    private PreparedStatement psReadAll = null;
    private PreparedStatement psInsert = null;
    private PreparedStatement psUpdate = null;
        
    public TableSession() {
        
    }
        
    protected void reconnect(Connection conn) throws SQLException {
        psReadDate = conn.prepareStatement("SELECT * FROM SESSION WHERE " + LASTACCESS + " < ?");
        psReadAll = conn.prepareStatement("SELECT * FROM SESSION");
        psInsert = conn.prepareStatement("INSERT INTO SESSION ("
                + SESSIONID + "," + SESSIONNAME
                + ") VALUES (?, ?)");
        psUpdate = conn.prepareStatement("UPDATE SESSION SET "
                + SESSIONNAME + " = ?,"
                + LASTACCESS + " = NOW "
                + "WHERE " + SESSIONID + " = ?");
        
    }

    public synchronized void insert(long sessionId, String sessionName) throws SQLException {
        psInsert.setLong(1, sessionId);
        psInsert.setString(2, sessionName);
        psInsert.executeUpdate();
    }
    
    public synchronized void update(long sessionId, String sessionName) throws SQLException {
        
        psUpdate.setLong(2, sessionId);
        psUpdate.setString(1, sessionName);
        psUpdate.executeUpdate();
        
    }
    
    private RecordSession build(ResultSet rs) throws HttpMalformedHeaderException, SQLException {
        RecordSession session = null;
        if (rs.next()) {
            session = new RecordSession(
                    rs.getLong(SESSIONID),
                    rs.getString(SESSIONNAME),
                    rs.getDate(LASTACCESS)
            );
            
        }
        return session;
        
    }    
    
}
