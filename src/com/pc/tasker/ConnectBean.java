package com.pc.tasker;

public class ConnectBean {
	String connstr=null; String DBuser=null ;String DBPAss=null;



	public String getConnstr() {
		return connstr;
	}



	public void setConnstr(String connstr) {
		this.connstr = connstr;
	}



	public String getDBuser() {
		return DBuser;
	}



	public void setDBuser(String dBuser) {
		DBuser = dBuser;
	}



	public String getDBPAss() {
		return DBPAss;
	}



	public void setDBPAss(String dBPAss) {
		DBPAss = dBPAss;
	}



	public ConnectBean(String connstr, String dBuser, String dBPAss) {
		super();
		this.connstr = connstr;
		DBuser = dBuser;
		DBPAss = dBPAss;
	}
	
	
	
}
