package com.pc.tasker;

public class Query {
	public String query;
	public boolean sendmail;
	public String db;
	
	public Query() {
		this.query="";
		this.sendmail=false;
		this.db=CONSTANTS.MAINDB;
	}
}
