package org.parosproxy.paros.db;

import java.io.IOException;
import java.net.ServerSocket;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.hsqldb.Server;

public class DatabaseServer {

    public static final int DEFAULT_SERVER_PORT = 9001;
    
    /*
     * url�� �����ϴ� �κ��ε�, �ϰ� ���ϴ� db ��ġ�� �̸��� �ݿ��ؼ� url�� �����Ѵٰ� �����
     */
	String  mUrl;
	String  mUser     = "sa";
	String  mPassword = "";
	Server  mServer = null;
	Connection mConn = null;
	


	DatabaseServer(String dbname) throws ClassNotFoundException, Exception {
		start(dbname);
	}
    private void start(String dbname) throws ClassNotFoundException, Exception{
    	// '/'�� ������ ��θ� ����, hsqldb������ ������.
        dbname = dbname.replaceAll("\\\\", "/");
        
        mUrl         = "jdbc:hsqldb:file:" + dbname;
        
        Class.forName("org.hsqldb.jdbcDriver");

        mConn = DriverManager.getConnection(mUrl, mUser, mPassword);
        

        
    }
    
    void shutdown(boolean compact) throws SQLException {
        Connection conn = getSingletonConnection();
        CallableStatement psCompact = null;
        
        if (compact) {
        	// db�� ���Ű� �ƴ϶� ���߿� ���� ���ɼ��� �ִ� ���̸� �����Ѵٰ� ���� ��
            psCompact = conn.prepareCall("SHUTDOWN COMPACT");

        } else {
        	// ����� database�� �ʿ��ѵ�, �� ���ዚ�� ������
            psCompact = conn.prepareCall("SHUTDOWN");

        }
        
        psCompact.execute();
        mConn.close();
        mConn = null;
    }
	
	
	public Connection getNewConnection() throws SQLException {
		
		
		Connection conn = null;
		for (int i=0; i<5; i++) {
			try {
				conn = DriverManager.getConnection(mUrl, mUser, mPassword);
				return conn;
			} catch (SQLException e) {
				e.printStackTrace();
				if (i==4) {
					throw e;
				}
				System.out.println("Recovering " + i + " times.");
			}
			
			try {
				Thread.sleep(500);
			} catch (Exception e) {
				
			}
		}
		return conn;
	}
	
	public Connection getSingletonConnection() throws SQLException {
		if (mConn == null) {
			mConn = getNewConnection();
		}
		
		return mConn;
	}
}
