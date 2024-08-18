package com.pc.tasker;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;

import java.sql.Timestamp;

public class AlertBean {
	String	ALERT_ID     ;
public String getALERT_ID() {
		return ALERT_ID;
	}









	public void setALERT_ID(String aLERT_ID) {
		ALERT_ID = aLERT_ID;
	}



String	ALERT_GROUP     ;
String	ALERT_NAME     ;
String	ALERT_SCRIPT         ;
String	IS_VALID              ;
String	THRESHOLD_condition   ;
int	NOTIFY_THRESHOLD   ;
int	WARNING_THRESHOLD ; 
int	CRITICAL_THRESHOLD  ;      

String ALERT_TYPE           ;
String ALERT_USER_HOST           ; 
String APPLICATION_DB ;
	int PRIORITY     ;
	long alertResult=-2147483648;
	String ErrorMsg ;
	String db_host ;
	String	ALERT_WINDOW              ;
	String	NEXT_STEP   ;
	String	Executionid   ;
	
	String	Owner   ;
	
	



	public String getOwner() {
		if(Owner==null) Owner=" ";
		return Owner;
	}









	public void setOwner(String owner) {
		Owner = owner;
	}









	public String getDb_host() {
		return db_host;
	}









	public String getExecutionid() {
		return Executionid;
	}









	public void setExecutionid(String executionid) {
		Executionid = executionid;
	}









	public void setDb_host(String db_host) {
		this.db_host = db_host;
	}









	public String getALERT_WINDOW() {
		return ALERT_WINDOW;
	}









	public void setALERT_WINDOW(String aLERT_WINDOW) {
		ALERT_WINDOW = aLERT_WINDOW;
	}









	public String getNEXT_STEP() {
		if(NEXT_STEP==null) NEXT_STEP=" ";
		
		return NEXT_STEP;
	}









	public void setNEXT_STEP(String nEXT_STEP) {
		NEXT_STEP = nEXT_STEP;
	}









	public AlertBean(String aLERT_GROUP, String aLERT_NAME, String aLERT_SCRIPT, String iS_VALID,
			String tHRESHOLD_condition, int nOTIFY_THRESHOLD, int wARNING_THRESHOLD, int cRITICAL_THRESHOLD,
			String aLERT_TYPE, String aLERT_USER_HOST, String aPPLICATION_DB, int pRIORITY,String alert_Window,String nextStep,String executionid,String aLERT_ID,String owner) {
		super();
		ALERT_GROUP = aLERT_GROUP;
		ALERT_NAME = aLERT_NAME;
		ALERT_SCRIPT = aLERT_SCRIPT;
		IS_VALID = iS_VALID;
		THRESHOLD_condition = tHRESHOLD_condition;
		NOTIFY_THRESHOLD = nOTIFY_THRESHOLD;
		WARNING_THRESHOLD = wARNING_THRESHOLD;
		CRITICAL_THRESHOLD = cRITICAL_THRESHOLD;
		ALERT_TYPE = aLERT_TYPE;
		ALERT_USER_HOST = aLERT_USER_HOST;
		APPLICATION_DB = aPPLICATION_DB;
		PRIORITY = pRIORITY;
		ALERT_WINDOW=alert_Window;
		NEXT_STEP= nextStep;
		Executionid=executionid;
		ALERT_ID=aLERT_ID;
		Owner=owner;
		
		
	}









	public String getALERT_GROUP() {
		return ALERT_GROUP;
	}









	public void setALERT_GROUP(String aLERT_GROUP) {
		ALERT_GROUP = aLERT_GROUP;
	}









	public String getALERT_NAME() {
		return ALERT_NAME;
	}









	public void setALERT_NAME(String aLERT_NAME) {
		ALERT_NAME = aLERT_NAME;
	}









	public String getALERT_SCRIPT() {
		return ALERT_SCRIPT;
	}









	public void setALERT_SCRIPT(String aLERT_SCRIPT) {
		ALERT_SCRIPT = aLERT_SCRIPT;
	}









	public String getIS_VALID() {
		return IS_VALID;
	}









	public void setIS_VALID(String iS_VALID) {
		IS_VALID = iS_VALID;
	}









	public String getTHRESHOLD_condition() {
		return THRESHOLD_condition;
	}









	public void setTHRESHOLD_condition(String tHRESHOLD_condition) {
		THRESHOLD_condition = tHRESHOLD_condition;
	}









	public int getNOTIFY_THRESHOLD() {
		return NOTIFY_THRESHOLD;
	}









	public void setNOTIFY_THRESHOLD(int nOTIFY_THRESHOLD) {
		NOTIFY_THRESHOLD = nOTIFY_THRESHOLD;
	}









	public int getWARNING_THRESHOLD() {
		return WARNING_THRESHOLD;
	}









	public void setWARNING_THRESHOLD(int wARNING_THRESHOLD) {
		WARNING_THRESHOLD = wARNING_THRESHOLD;
	}









	public int getCRITICAL_THRESHOLD() {
		return CRITICAL_THRESHOLD;
	}









	public void setCRITICAL_THRESHOLD(int cRITICAL_THRESHOLD) {
		CRITICAL_THRESHOLD = cRITICAL_THRESHOLD;
	}









	public String getALERT_TYPE() {
		return ALERT_TYPE;
	}









	public void setALERT_TYPE(String aLERT_TYPE) {
		ALERT_TYPE = aLERT_TYPE;
	}









	public String getALERT_USER_HOST() {
		return ALERT_USER_HOST;
	}









	public void setALERT_USER_HOST(String aLERT_USER_HOST) {
		ALERT_USER_HOST = aLERT_USER_HOST;
	}









	public String getAPPLICATION_DB() {
		return APPLICATION_DB;
	}









	public void setAPPLICATION_DB(String aPPLICATION_DB) {
		APPLICATION_DB = aPPLICATION_DB;
	}









	public int getPRIORITY() {
		return PRIORITY;
	}









	public void setPRIORITY(int pRIORITY) {
		PRIORITY = pRIORITY;
	}









	public long getAlertResult() {
		return alertResult;
	}









	public void setAlertResult(long alertResult) {
		this.alertResult = alertResult;
	}









	public String getErrorMsg() {
		return ErrorMsg;
	}









	public void setErrorMsg(String errorMsg) {
		ErrorMsg = errorMsg;
	}



	Timestamp START_TIME;
	Timestamp END_TIME;




	public Timestamp getSTART_TIME() {
	return START_TIME;
}









public void setSTART_TIME(Timestamp sTART_TIME) {
	START_TIME = sTART_TIME;
}









public Timestamp getEND_TIME() {
	return END_TIME;
}









public void setEND_TIME(Timestamp eND_TIME) {
	END_TIME = eND_TIME;
}









	
	
	







	@Override
public String toString() {
	return "AlertBean [ALERT_ID=" + ALERT_ID + ", ALERT_GROUP=" + ALERT_GROUP + ", ALERT_NAME=" + ALERT_NAME
			+ ", ALERT_SCRIPT=" + ALERT_SCRIPT + ", IS_VALID=" + IS_VALID + ", THRESHOLD_condition="
			+ THRESHOLD_condition + ", NOTIFY_THRESHOLD=" + NOTIFY_THRESHOLD + ", WARNING_THRESHOLD="
			+ WARNING_THRESHOLD + ", CRITICAL_THRESHOLD=" + CRITICAL_THRESHOLD + ", ALERT_TYPE=" + ALERT_TYPE
			+ ", ALERT_USER_HOST=" + ALERT_USER_HOST + ", APPLICATION_DB=" + APPLICATION_DB + ", PRIORITY=" + PRIORITY
			+ ", alertResult=" + alertResult + ", ErrorMsg=" + ErrorMsg + ", db_host=" + db_host + ", ALERT_WINDOW="
			+ ALERT_WINDOW + ", NEXT_STEP=" + NEXT_STEP + ", Executionid=" + Executionid + ", Owner=" + Owner
			+ ", START_TIME=" + START_TIME + ", END_TIME=" + END_TIME + ", getALERT_ID()=" + getALERT_ID()
			+ ", getOwner()=" + getOwner() + ", getDb_host()=" + getDb_host() + ", getExecutionid()=" + getExecutionid()
			+ ", getALERT_WINDOW()=" + getALERT_WINDOW() + ", getNEXT_STEP()=" + getNEXT_STEP() + ", getALERT_GROUP()="
			+ getALERT_GROUP() + ", getALERT_NAME()=" + getALERT_NAME() + ", getALERT_SCRIPT()=" + getALERT_SCRIPT()
			+ ", getIS_VALID()=" + getIS_VALID() + ", getTHRESHOLD_condition()=" + getTHRESHOLD_condition()
			+ ", getNOTIFY_THRESHOLD()=" + getNOTIFY_THRESHOLD() + ", getWARNING_THRESHOLD()=" + getWARNING_THRESHOLD()
			+ ", getCRITICAL_THRESHOLD()=" + getCRITICAL_THRESHOLD() + ", getALERT_TYPE()=" + getALERT_TYPE()
			+ ", getALERT_USER_HOST()=" + getALERT_USER_HOST() + ", getAPPLICATION_DB()=" + getAPPLICATION_DB()
			+ ", getPRIORITY()=" + getPRIORITY() + ", getAlertResult()=" + getAlertResult() + ", getErrorMsg()="
			+ getErrorMsg() + ", getSTART_TIME()=" + getSTART_TIME() + ", getEND_TIME()=" + getEND_TIME()
			+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
}









	public void insertalertLog(String EXECUTIONID,String ALERT_GROUP,String ALERT_NAME,String ALERT_ID,Timestamp START_TIME,Timestamp END_TIME,long value,String ALERT_RESULT,String STATUS) throws Exception {

		String q="insert into TASKER_ALERT_AUDIT_LOG (EXECUTION_ID,ALERT_GROUP,ALERT_NAME,ALERT_ID,START_TIME,END_TIME,ALERT_VALUE,ALERT_RESULT,STATUS) values(?,?,?,?,?,?,?,?,?)";
        final Connection connection = Util.getConnection(CONSTANTS.MAINDB);
        PreparedStatement  stmt=null;
        try {
           
        	stmt = connection.prepareStatement(q);
        	stmt.setLong(1, Long.parseLong(EXECUTIONID));
        	stmt.setString(2, ALERT_GROUP);
        	stmt.setString(3, ALERT_NAME);
        	stmt.setInt(4, Integer.parseInt(ALERT_ID));
        	stmt.setTimestamp(5, START_TIME);
        	stmt.setTimestamp(6, END_TIME);
        	if (value!=-2147483648) {
        		stmt.setLong(7, value);
        	}else {
        		stmt.setString(7, null);
			}
        	
        	
        	
        	
        	stmt.setString(8, ALERT_RESULT);
        	if (STATUS!=null&& STATUS.length()>1990)
        		STATUS=	 STATUS.substring(0,1990);
        	stmt.setString(9, STATUS);
            final int rs = stmt.executeUpdate();
         
           
        }
        catch (Exception e) {
            throw e;
        }
        finally {
        	
            	if(stmt!=null)
            	stmt.close();
            	if(connection!=null)
                connection.close();
        }
     
		
	}









	
	
	
}
