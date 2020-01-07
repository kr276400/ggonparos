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
     * url을 변경하는 부분인데, 니가 원하는 db 위치나 이름을 반영해서 url를 변경한다고 보면됨
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
    	// '/'로 지정된 경로만 받음, hsqldb에서는 오로지.
        dbname = dbname.replaceAll("\\\\", "/");
        
        mUrl         = "jdbc:hsqldb:file:" + dbname;
        
        Class.forName("org.hsqldb.jdbcDriver");

        mConn = DriverManager.getConnection(mUrl, mUser, mPassword);
        

        
    }
    
    void shutdown(boolean compact) throws SQLException {
        Connection conn = getSingletonConnection();
        CallableStatement psCompact = null;
        
        if (compact) {
        	// db가 새거가 아니라 나중에 사용될 가능성이 있는 놈이면 압축한다고 보면 됨
            psCompact = conn.prepareCall("SHUTDOWN COMPACT");

        } else {
        	// 압축된 database가 필요한데, 걍 종료떄려 박으셈
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
