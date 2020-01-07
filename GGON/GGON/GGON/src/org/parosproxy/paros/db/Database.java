package org.parosproxy.paros.db;

import java.sql.SQLException;
import java.util.Vector;

import org.parosproxy.paros.core.spider.SpiderListener;

public class Database {
	

	
	private static Database database = null;
	
	private DatabaseServer databaseServer = null;
	private TableHistory tableHistory = null;
	private TableSession tableSession = null;
	private TableAlert tableAlert = null;
	private TableScan tableScan = null;
	
	private Vector listenerList = new Vector();

	public Database() {
	    tableHistory = new TableHistory();
	    tableSession = new TableSession();
	    tableAlert = new TableAlert();
	    tableScan = new TableScan();
	    addDatabaseListener(tableHistory);
	    addDatabaseListener(tableSession);
	    addDatabaseListener(tableAlert);
	    addDatabaseListener(tableScan);

	}
	
	public DatabaseServer getDatabaseServer() {
		return databaseServer;
	}
	
	private void setDatabaseServer(DatabaseServer databaseServer) {
		this.databaseServer = databaseServer;
	}
	
	private void setTableHistory(TableHistory tableHistory) {
		this.tableHistory = tableHistory;
	}
		
	public TableHistory getTableHistory() {
		return tableHistory;		
	}

    public TableSession getTableSession() {
        return tableSession;
    }
    
    private void setTableSession(TableSession tableSession) {
        this.tableSession = tableSession;
    }
    
    public static Database getSingleton() {
        if (database == null) {
            database = new Database();
        }
        
        return database;
    }
    
	public void addDatabaseListener(DatabaseListener listener) {
		listenerList.add(listener);
		
	}
	
	public void removeDatabaseListener(SpiderListener listener) {
		listenerList.remove(listener);
	}
	
	private void notifyListenerDatabaseOpen() throws SQLException {
	    DatabaseListener listener = null;
	    
	    for (int i=0;i<listenerList.size();i++) {
	        listener = (DatabaseListener) listenerList.get(i);
	        listener.databaseOpen(getDatabaseServer());	        
	    }
	}

	public void open(String path) throws ClassNotFoundException, Exception {
	    setDatabaseServer(new DatabaseServer(path));
	    notifyListenerDatabaseOpen();
	}
	
	public void close(boolean compact) {
	    if (databaseServer == null) return;
	    
	    try {
	    	// 청소 시작
	        getTableHistory().deleteTemporary();
	        // 종료 부분
	        getDatabaseServer().shutdown(compact);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
    public TableAlert getTableAlert() {
        return tableAlert;
    }
    
    public void setTableAlert(TableAlert tableAlert) {
        this.tableAlert = tableAlert;
    }
    
    public TableScan getTableScan() {
        return tableScan;
    }

    public void setTableScan(TableScan tableScan) {
        this.tableScan = tableScan;
    }
}
