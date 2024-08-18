package com.pc.tasker;

import java.util.concurrent.*;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.*;
import org.apache.commons.mail.*;

public class ReportGenerator
{
    public static void main(final String[] args) throws NumberFormatException, Exception {
        final int SLEEP_TIME = Integer.parseInt(Util.getDBProperty(CONSTANTS.proprunInterval));
        final Thread repoter = new Thread(new Runnable() {
            @Override
            public void run() {
            	Connection con=null;
            	 Statement stmt=null,stmt1 =null;
            	  ResultSet rs=null;
                try {
                	con=Util.getConnection(CONSTANTS.MAINDB);
                	
                	stmt = con.createStatement();
                	stmt1 = con.createStatement();
                	
                	  rs = stmt.executeQuery("select ROWID,tasker_Request.* from tasker_Request where Enabled='Y' and FREQUENCY not in ('BYREQ','byreq') order by name");
                	  stmt1.executeUpdate("update tasker_Request set FREQUENCY=FREQUENCY||'_RUNNING' where Enabled='Y' and FREQUENCY not in ('BYREQ','byreq') and FREQUENCY not like '%_RUNNING' "); 
                	 while (rs.next()) {
                         String ROWID=rs.getString("ROWID");
                         String FREQUENCY=rs.getString("FREQUENCY");
                         String TIME=rs.getString("TIME");
                         String PATH=rs.getString("PATH");
                         String FILENAME=rs.getString("FILENAME");
                         String REQUESTOR=rs.getString("REQUESTOR");
                         String DESCRIPTION=rs.getString("DESCRIPTION");
                         String NAME=rs.getString("NAME");
                         String EMAIL_SUBJECT=rs.getString("EMAIL_SUBJECT");
                         String HEAD=rs.getString("EMAIL_HEAD");
                         String ifPrecon=rs.getString("Email_IF_PRE_COND_IS_TRUE");
                         
                         if (HEAD ==null){
                        	 HEAD="";
                         }
                         String EMAIL_LIST=rs.getString("EMAIL_LIST");
                         Timestamp LASTEXECUTED=rs.getTimestamp("LASTEXECUTED");
                         if (LASTEXECUTED ==null)
                         {
                        	 LASTEXECUTED=new Timestamp(0);
                         }
                         System.out.println("FILENAME : "+FILENAME);
                         System.out.println("PATH "+PATH);
                         System.out.println("EMAIL_SUBJECT "+EMAIL_SUBJECT);
                         System.out.println("EMAIL_LIST "+EMAIL_LIST);
                         Calendar lastExecutetime=Calendar.getInstance();
                         lastExecutetime.setTime(LASTEXECUTED);
                         Calendar now=Calendar.getInstance();
                        
                                                  
                        
                        
                         System.out.println(FILENAME + " Now " +now.getTime().toLocaleString());
                         System.out.println(FILENAME + " lastExecutetime  " +lastExecutetime.getTime().toLocaleString());
                        
                         System.out.println(FILENAME + " last hrs " +lastExecutetime.get(Calendar.HOUR_OF_DAY));
                         System.out.println(FILENAME + "  last min " +lastExecutetime.get(Calendar.MINUTE));
                         System.out.println(FILENAME + " day " +lastExecutetime.get(Calendar.DAY_OF_MONTH));
                         System.out.println(FILENAME + " day now " +now.get(Calendar.DAY_OF_MONTH));
                         synchronized (rs) {
                        	 if(FREQUENCY.equalsIgnoreCase("HOURLY")){
                        		 if (now.get(Calendar.HOUR_OF_DAY)!=lastExecutetime.get(Calendar.HOUR_OF_DAY)){
                        			// stmt1.executeUpdate("update tasker_Request set LastExecuted=sysdate,FREQUENCY='RUNNING' where rowid='"+ROWID+"'");
                        			 if (NAME.equalsIgnoreCase("ALERT")) {
                     				 	generateAlertReport(FILENAME,PATH,EMAIL_SUBJECT,EMAIL_LIST,NAME,DESCRIPTION,REQUESTOR,HEAD,ifPrecon,REQUESTOR);
                        			 }else {
                        			 generateReport(FILENAME,PATH,EMAIL_SUBJECT,EMAIL_LIST,NAME,DESCRIPTION,REQUESTOR,HEAD,ifPrecon);
                        			 }
                        			 
                        			 stmt1.executeUpdate("update tasker_Request set LastExecuted=sysdate where rowid='"+ROWID+"'");
                        		 }
                        		 
                        	 }
                        	 if(FREQUENCY.equalsIgnoreCase("EOD")){
                        		 if (now.get(Calendar.DAY_OF_MONTH)!=lastExecutetime.get(Calendar.DAY_OF_MONTH)){
                        			// stmt1.executeUpdate("update tasker_Request set LastExecuted=sysdate,FREQUENCY='RUNNING' where rowid='"+ROWID+"'");
                        			 if (NAME.equalsIgnoreCase("ALERT")) {
                      				 	generateAlertReport(FILENAME,PATH,EMAIL_SUBJECT,EMAIL_LIST,NAME,DESCRIPTION,REQUESTOR,HEAD,ifPrecon,REQUESTOR);
                         			 }else {
                         			 generateReport(FILENAME,PATH,EMAIL_SUBJECT,EMAIL_LIST,NAME,DESCRIPTION,REQUESTOR,HEAD,ifPrecon);
                         			 }
                        			 stmt1.executeUpdate("update tasker_Request set LastExecuted=sysdate where rowid='"+ROWID+"'");
                        		 }
                        		 
                        	 }
                        	 if(FREQUENCY.equalsIgnoreCase("SCHEDULED_HOUR")&& TIME!=null){
                        		 
                        		 String[] timearr=TIME.split(",");
                        		 for (int i = 0; i < timearr.length; i++) {
                        			 
                        			 if (now.get(Calendar.HOUR_OF_DAY)==Integer.parseInt(timearr[i])&& now.get(Calendar.HOUR_OF_DAY)!=lastExecutetime.get(Calendar.HOUR_OF_DAY)){
                        			//	 stmt1.executeUpdate("update tasker_Request set LastExecuted=sysdate,FREQUENCY='RUNNING' where rowid='"+ROWID+"'");
                        				 if (NAME.equalsIgnoreCase("ALERT")) {
                          				 	generateAlertReport(FILENAME,PATH,EMAIL_SUBJECT,EMAIL_LIST,NAME,DESCRIPTION,REQUESTOR,HEAD,ifPrecon,REQUESTOR);
                             			 }else {
                             			 generateReport(FILENAME,PATH,EMAIL_SUBJECT,EMAIL_LIST,NAME,DESCRIPTION,REQUESTOR,HEAD,ifPrecon);
                             			 }
                            			 stmt1.executeUpdate("update tasker_Request set LastExecuted=sysdate where rowid='"+ROWID+"'");
                            		 }
                        			 
								}                      		 
                        		 
                        	 }
                        	if(FREQUENCY.equalsIgnoreCase("SCHEDULED_MIN" )&& TIME!=null){
                        		 
                        		 String[] timearr=TIME.split(",");
                        		 for (int i = 0; i < timearr.length; i++) {
                        			 
                        			 if (now.get(Calendar.MINUTE)==Integer.parseInt(timearr[i])&& now.get(Calendar.MINUTE)!=lastExecutetime.get(Calendar.MINUTE)){
                        			//	 stmt1.executeUpdate("update tasker_Request set LastExecuted=sysdate,FREQUENCY='RUNNING' where rowid='"+ROWID+"'");
                        				 if (NAME.equalsIgnoreCase("ALERT")) {
                          				 	generateAlertReport(FILENAME,PATH,EMAIL_SUBJECT,EMAIL_LIST,NAME,DESCRIPTION,REQUESTOR,HEAD,ifPrecon,REQUESTOR);
                             			 }else {
                             			 generateReport(FILENAME,PATH,EMAIL_SUBJECT,EMAIL_LIST,NAME,DESCRIPTION,REQUESTOR,HEAD,ifPrecon);
                             			 }
                            			stmt1.executeUpdate("update tasker_Request set LastExecuted=sysdate where rowid='"+ROWID+"'");
                            		 }
                        			 
								}                      		 
                        		 
                        	 }
                        	
                        	if(FREQUENCY.equalsIgnoreCase("REPEAT_MIN" )&& TIME!=null){
                       		 
                       		 String time=TIME.split(",")[0];
                       		
                       	long timediff = now.getTimeInMillis()-lastExecutetime.getTimeInMillis();
                       	long timediffmin = timediff / (60 * 1000);
                       	long diffHours = timediff / (60 * 60 * 1000);  
                       			 if (timediffmin>=Long.parseLong(time)){
                       			//	 stmt1.executeUpdate("update tasker_Request set LastExecuted=sysdate,FREQUENCY='RUNNING' where rowid='"+ROWID+"'");
                       				 if (NAME.equalsIgnoreCase("ALERT")) {
                         				 	generateAlertReport(FILENAME,PATH,EMAIL_SUBJECT,EMAIL_LIST,NAME,DESCRIPTION,REQUESTOR,HEAD,ifPrecon,REQUESTOR);
                            			 }else {
                            			 generateReport(FILENAME,PATH,EMAIL_SUBJECT,EMAIL_LIST,NAME,DESCRIPTION,REQUESTOR,HEAD,ifPrecon);
                            			 }
                           			stmt1.executeUpdate("update tasker_Request set LastExecuted=sysdate where rowid='"+ROWID+"'");
                           		 }
                       			 
								                     		 
                       		 
                       	 }
                        	
                        	
                        	if(FREQUENCY.equalsIgnoreCase("REPEAT_HOUR" )&& TIME!=null){
                          		 
                          		 String time=TIME.split(",")[0];
                          		
                          	long timediff = now.getTimeInMillis()-lastExecutetime.getTimeInMillis();
                          	long timediffmin = timediff / (60 * 1000);
                          	long diffHours = timediff / (60 * 60 * 1000);  
                          			 if (diffHours>=Long.parseLong(time)){
                          			//	 stmt1.executeUpdate("update tasker_Request set LastExecuted=sysdate,FREQUENCY='RUNNING' where rowid='"+ROWID+"'");
                          				 if (NAME.equalsIgnoreCase("ALERT")) {
                            				 	generateAlertReport(FILENAME,PATH,EMAIL_SUBJECT,EMAIL_LIST,NAME,DESCRIPTION,REQUESTOR,HEAD,ifPrecon,REQUESTOR);
                               			 }else {
                               			 generateReport(FILENAME,PATH,EMAIL_SUBJECT,EMAIL_LIST,NAME,DESCRIPTION,REQUESTOR,HEAD,ifPrecon);
                               			 }
                              			stmt1.executeUpdate("update tasker_Request set LastExecuted=sysdate where rowid='"+ROWID+"'");
                              		 }
                          			 
   								                     		 
                          		 
                          	 }
    
                        	if (FREQUENCY.equalsIgnoreCase("ONCE")){
                        	//	 stmt1.executeUpdate("update tasker_Request set LastExecuted=sysdate,Enabled='N',FREQUENCY='RUNNING' where rowid='"+ROWID+"'"); 
                        		 if (NAME.equalsIgnoreCase("ALERT")) {
                  				 	generateAlertReport(FILENAME,PATH,EMAIL_SUBJECT,EMAIL_LIST,NAME,DESCRIPTION,REQUESTOR,HEAD,ifPrecon,REQUESTOR);
                     			 }else {
                     			 generateReport(FILENAME,PATH,EMAIL_SUBJECT,EMAIL_LIST,NAME,DESCRIPTION,REQUESTOR,HEAD,ifPrecon);
                     			 }
                    			 stmt1.executeUpdate("update tasker_Request set LastExecuted=sysdate,Enabled='N' where rowid='"+ROWID+"'");
                    		 }
                        	 
						}
                		  
                		 
                		 
                     }
                	 stmt1.executeUpdate("update tasker_Request set FREQUENCY=replace(FREQUENCY,'_RUNNING','') where Enabled='Y' and FREQUENCY not in ('BYREQ','byreq') "); 
                	 Util.print_log("Tasker Sleeping for " + SLEEP_TIME + " Minutes...");
                }
                catch (Exception e) {
                	try {
						stmt1.executeUpdate("update tasker_Request set FREQUENCY=replace(FREQUENCY,'_RUNNING','') where Enabled='Y' and FREQUENCY not in ('BYREQ','byreq') ");
					} catch (SQLException e1) {
						
						e1.printStackTrace();
					} 
                    e.printStackTrace();
                }finally {
                	  try {
                		  if(rs!=null)
                    		  rs.close();
                          	if(stmt!=null)
                    		  stmt.close();
                          	if(stmt1!=null)
                    		  stmt1.close();
                          	if(con!=null)
            				con.close();
                		
					} catch (SQLException e) {
						
						e.printStackTrace();
					}
				}
            }
        });
        final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        if(args!=null && args.length>0 && args[0]!=null) {
        	byreq(args);
        }
        else {
        if(SLEEP_TIME!=-1){
        scheduler.scheduleAtFixedRate(repoter, 0L, SLEEP_TIME, TimeUnit.MINUTES);
        }else{
        	repoter.start();
        }
        }
    }
    
    private static void generateReport(String file,String path,String EMAIL_SUBJECT,String EMAIL_LIST,String NAME,String DESCRIPTION,String REQUESTOR,String HEAD,String ifPrecon) {
    	 String htmlMessage="";
    	  try {
        String textcolor = "#333333";
         String thcolor = Util.getDBProperty("table.head.color");
         String trcolor = Util.getDBProperty("table.row.color");
         String fontsize = Util.getDBProperty("table.row.fontsize");
         String headfontsize = Util.getDBProperty("head.row.fontsize");
         String bordersize = Util.getDBProperty("table.border.size");
         String bordercolor = Util.getDBProperty("table.border.color");
         String headtextcolor = Util.getDBProperty("table.head.textcolor");
        textcolor = Util.getDBProperty("table.text.color");
        String font = Util.getDBProperty("report.font");
        htmlMessage = "<html><head><style>body {font-family: "+font+"} pre {font-size: 14px;font-style: normal;color:#b5b7b7;} h3 {font-size: "+headfontsize+"px;font-style: normal;font-variant: normal;} table.tftable {font-style: normal;font-size:"+fontsize+"px;color:" + textcolor + ";border-width: "+bordersize+"px;border-color: "+bordercolor+";border-collapse: collapse;}table.tftable th {font-size:"+fontsize+"px;background-color:" + thcolor + ";border-width: "+bordersize+"px;padding: 5px;border-style: solid;color:"+headtextcolor+";border-color:"+bordercolor+";text-align:left;}table.tftable tr {background-color:" + trcolor + ";}table.tftable td {font-size:"+fontsize+"px;border-width: "+bordersize+"px;padding: 5px;border-style: solid;border-color:"+bordercolor+";}" + "</style>" + "</head>" + "<body><div align=left><h3>" + HEAD  + "</h3></div>";
        
        FileParser fp=new FileParser(file, path);
       
        
        htmlMessage = String.valueOf(htmlMessage) +  fp.processfile();
       
        Util.print_log(String.valueOf(htmlMessage) + "<pre>Deccription : "+DESCRIPTION+"<br>Owner : "+REQUESTOR+"</pre></body></html>");
        boolean sendemail=false;
        if(ifPrecon!=null ){
        	if(ifPrecon.equalsIgnoreCase("Y")&& fp.ifcon==false){
        		sendemail=false;
        	}else{
        		sendemail=true;
        	}
        	
        }else{
        	sendemail=true;
        }
        String [] attachfiles=null;
        String [] delfiles=null;
        if (fp.getAttach()!=null){
       attachfiles = fp.getAttach().toArray(new String[fp.getAttach().size()]);
        }
        if (fp.getAttachdel()!=null){
       delfiles = fp.getAttachdel().toArray(new String[fp.getAttachdel().size()]);
        }
        if(sendemail){
        	SendEmail.sendEmail(EMAIL_LIST,Util.getDBProperty("mail.subject")+EMAIL_SUBJECT , String.valueOf(htmlMessage) + "<pre>Description : "+DESCRIPTION+"<br>Owner : "+REQUESTOR+"</pre>"
            		+ "<br><BR><div style=\"text-align:right;color:#e1eaff;font-style: italic;\"> - Tasker </div></body></html>",attachfiles,delfiles);
        }
        }
        catch (Exception e) {
        	 try {
        		 htmlMessage="<html><body> Exception in excuting task. " +e.getMessage();
				SendEmail.sendEmail(EMAIL_LIST,Util.getDBProperty("mail.subject")+EMAIL_SUBJECT, String.valueOf(htmlMessage) + "</body></html>",null,null);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
            e.printStackTrace();
        }
    }
    
    
    
    private static void generateAlertReport(String file,String path,String EMAIL_SUBJECT,String EMAIL_LIST,String NAME,String DESCRIPTION,String REQUESTOR,String HEAD,String ifPrecon,String ALERT_GROUP) {
   	 String htmlMessage="";
		try {
			
			List<AlertBean> alrets = Util.getAlrets(ALERT_GROUP,file,path);
			List<AlertBean> warningalrets = new ArrayList<AlertBean>(alrets);
			
			 String textcolor =   Util.getDBProperty("table.alert.text.color");;
	         String thcolor = Util.getDBProperty("table.alert.head.color");
	         String trcolor = Util.getDBProperty("table.alert.row.color");
	         int fontsize = Integer.parseInt(Util.getDBProperty("table.alert.row.fontsize"));
	         String headfontsize = Util.getDBProperty("head.alert.row.fontsize");
	         String bordersize = Util.getDBProperty("table.alert.border.size");
	         String bordercolor = Util.getDBProperty("table.alert.border.color");
	         String headtextcolor = Util.getDBProperty("table.alert.head.textcolor");
	         String criticalcolor = Util.getDBProperty("table.alert.head.criticalcolor");
	         String warningcolor = Util.getDBProperty("table.alert.head.warningcolor");
	         String notifcolor = Util.getDBProperty("table.alert.head.notifcolor");
	         String nacolor = Util.getDBProperty("table.alert.head.nacolor");
	         String font = Util.getDBProperty("alert.font");
	         String rowfill = Util.getDBProperty("alert.row.paint");
	         String trCritical="<tr>";
	         String trwarn="<tr>";
	         String trnotif="<tr>";
	         String executionId="";
	         String sendAlertemail=Util.getDBProperty("alert.sendAlertEmail.inBAU");
	      
	         if(rowfill.equalsIgnoreCase("full")) {
	        	 trCritical="<tr style='background-color:"+criticalcolor+"'>";
	        	 trwarn="<tr style='background-color:"+warningcolor+"'>";
	        	 trnotif="<tr style='background-color:"+notifcolor+"'>";
	         }
	         
			htmlMessage = "<html><head><style>body {font-family: "+font+"} pre {font-size: 14px;font-style: normal;color:#b5b7b7;} "
					+ "h3 {font-size: "+headfontsize+"px;"
							+ "font-style: normal;font-variant: normal;}table.tftable {	font-style: normal;font-size:"+fontsize+"px;color:"
					+ textcolor
					+ ";border-width:"+bordersize+"px;border-color: "+bordercolor+";border-collapse: collapse;}table.tftable th {font-size:"+fontsize+"px;background-color:"
					+ thcolor
					+ ";border-width:"+bordersize+"px;padding: 5px;border-style: solid;color:"+headtextcolor+";border-color: "+bordercolor+";text-align:left;}table.tftable tr {background-color:"
					+ trcolor
					+ ";}table.tftable td {font-size:"+fontsize+"px;border-width: "+bordersize+"px;padding: 5px;border-style: solid;border-color: "+bordercolor+";}"
					+ "</style>" + "</head>" + "<body><div align=left><h3>" + ALERT_GROUP +" "+ HEAD + "</h3></div>";
			
			String retTable = "<div ><table id='tfhover' class='tftable' border='1'><tr>";
		    retTable = String.valueOf(retTable) + "<th> Alert </th>";
            retTable = String.valueOf(retTable) + "<th> DB/Host </th>";
            retTable = String.valueOf(retTable) + "<th> Count </th>";
            retTable = String.valueOf(retTable) + "<th> BAU Count </th>";
            retTable = String.valueOf(retTable) + "<th> Owner </th>";
            retTable = String.valueOf(retTable) + "</tr>";
            
			for (AlertBean alertBean : alrets) {
				
				executionId=alertBean.getExecutionid();
				
				
				long alertresult = alertBean.getAlertResult();	
				String result="";
				if (alertBean.getErrorMsg()==null) {
					result=String.valueOf(alertBean.getAlertResult());
				}else if(!alertBean.getErrorMsg().equals("NA")){
					result=" <b>Error! </b> ";
				}
				int critical = alertBean.getCRITICAL_THRESHOLD();
				String condition=alertBean.getTHRESHOLD_condition();
				
				
				
				if((alertBean.getErrorMsg()!=null&&!alertBean.getErrorMsg().equals("NA"))){
					
					retTable = String.valueOf(retTable) + trCritical;
					retTable = String.valueOf(retTable) +"<td>" + alertBean.getALERT_NAME() + "</td>"+"<td>" + alertBean.getDb_host() + "</td>"+ "<td style='background-color:"+criticalcolor+"'>" + result + "</td>";
					retTable = String.valueOf(retTable) + "<td>" + "" + "</td>";
					retTable = String.valueOf(retTable) + "<td>" + alertBean.getOwner() + "</td>";
					retTable = String.valueOf(retTable) + "<td style='background-color:#ffffff;border-color:#ffffff'font-size:"+String.valueOf(fontsize-1)+"px'></td>";
		            retTable = String.valueOf(retTable) + "</tr>";
		            alertBean.insertalertLog(alertBean.getExecutionid(), ALERT_GROUP, alertBean.getALERT_NAME(), alertBean.getALERT_ID(), alertBean.getSTART_TIME(), alertBean.getEND_TIME(), alertBean.getAlertResult(), "Critical", alertBean.getErrorMsg());
		            warningalrets.remove(alertBean);
				}
				
				else if(condition.equalsIgnoreCase("GREATER_THAN"))
				{
					if (alertresult>critical&&alertBean.getErrorMsg()==null) {
						retTable = String.valueOf(retTable) + trCritical;
						retTable = String.valueOf(retTable) +"<td>" + alertBean.getALERT_NAME() + "</td>"+"<td>" + alertBean.getDb_host() + "</td>"+ "<td style='background-color:"+criticalcolor+"'>" + result + "</td>";
						retTable = String.valueOf(retTable) + "<td> <span style='font-size: "+String.valueOf(fontsize-6)+"px;'>Less than </span>" + String.valueOf(Math.max(Integer.valueOf(alertBean.getWARNING_THRESHOLD() - 1),0)) + "</td>";
						retTable = String.valueOf(retTable) + "<td>" +  alertBean.getOwner() + "</td>";
						
						
						retTable = String.valueOf(retTable) + "<td style='background-color:#ffffff;border-color:#ffffff'font-size:"+String.valueOf(fontsize-1)+"px'>" +  alertBean.getNEXT_STEP() + "</td>";
						
						retTable = String.valueOf(retTable) + "</tr>";
			            warningalrets.remove(alertBean);
			            sendAlertemail="Y";
			            
			            alertBean.insertalertLog(alertBean.getExecutionid(), ALERT_GROUP, alertBean.getALERT_NAME(), alertBean.getALERT_ID(), alertBean.getSTART_TIME(), alertBean.getEND_TIME(), alertBean.getAlertResult(), "Critical", alertBean.getErrorMsg());
					}
					
				}else if (condition.equalsIgnoreCase("LESS_THAN")) {
					
					if (alertresult<critical&&alertBean.getErrorMsg()==null) {
						retTable = String.valueOf(retTable) + trCritical;
						retTable = String.valueOf(retTable) +"<td>" + alertBean.getALERT_NAME() + "</td>"+"<td>" + alertBean.getDb_host() + "</td>"+ "<td style='background-color:"+criticalcolor+"'>" + result + "</td>";
						retTable = String.valueOf(retTable) + "<td> <span style='font-size: "+String.valueOf(fontsize-6)+"px;'>Greater than </span>" + String.valueOf(alertBean.getWARNING_THRESHOLD() + 1) + "</td>";
						retTable = String.valueOf(retTable) + "<td>" +  alertBean.getOwner() + "</td>";
						
						retTable = String.valueOf(retTable) + "<td style='background-color:#ffffff;border-color:#ffffff'>" +  alertBean.getNEXT_STEP() + "</td>";
						
						retTable = String.valueOf(retTable) + "</tr>";
			            warningalrets.remove(alertBean);
			            sendAlertemail="Y";
			            alertBean.insertalertLog(alertBean.getExecutionid(), ALERT_GROUP, alertBean.getALERT_NAME(), alertBean.getALERT_ID(), alertBean.getSTART_TIME(), alertBean.getEND_TIME(), alertBean.getAlertResult(), "Critical", alertBean.getErrorMsg());
					}
					
				}
				    
			}
			List<AlertBean> notifalrets = new ArrayList<AlertBean>(warningalrets);
			for (AlertBean alertBean : warningalrets) {
				long result = alertBean.getAlertResult();
				int warning = alertBean.getWARNING_THRESHOLD();
				String condition=alertBean.getTHRESHOLD_condition();
				if(alertBean.getErrorMsg()==null) {
				if(condition.equalsIgnoreCase("GREATER_THAN"))
				{
					if (result>warning) {
						retTable = String.valueOf(retTable) + trwarn;
						retTable = String.valueOf(retTable) +"<td>" + alertBean.getALERT_NAME() + "</td>"+"<td>" + alertBean.getDb_host() + "</td>"+ "<td style='background-color:"+warningcolor+"' >" + result + "</td>";
						retTable = String.valueOf(retTable) + "<td> <span style='font-size: "+String.valueOf(fontsize-6)+"px;'>Less than </span>" + String.valueOf(Math.max(Integer.valueOf(alertBean.getWARNING_THRESHOLD() - 1),0))  + "</td>";
						retTable = String.valueOf(retTable) + "<td>" +  alertBean.getOwner() + "</td>";
						retTable = String.valueOf(retTable) + "<td style='background-color:#ffffff;border-color:#ffffff'></td>";
						retTable = String.valueOf(retTable) + "</tr>";
			            notifalrets.remove(alertBean);
			            sendAlertemail="Y";
			            alertBean.insertalertLog(alertBean.getExecutionid(), ALERT_GROUP, alertBean.getALERT_NAME(), alertBean.getALERT_ID(), alertBean.getSTART_TIME(), alertBean.getEND_TIME(), alertBean.getAlertResult(), "Warning", alertBean.getErrorMsg());
					}
					
				}else if (condition.equalsIgnoreCase("LESS_THAN")) {
					
					if (result<warning) {
						retTable = String.valueOf(retTable) + trwarn;
						retTable = String.valueOf(retTable) +"<td>" + alertBean.getALERT_NAME() + "</td>"+"<td>" + alertBean.getDb_host() + "</td>"+ "<td style='background-color:"+warningcolor+"'>" + result + "</td>";
						retTable = String.valueOf(retTable) + "<td> <span style='font-size: "+String.valueOf(fontsize-6)+"px;'>Greater than </span>" + String.valueOf(alertBean.getWARNING_THRESHOLD() + 1) + "</td>";
						retTable = String.valueOf(retTable) + "<td>" +  alertBean.getOwner() + "</td>";
						retTable = String.valueOf(retTable) + "<td style='background-color:#ffffff;border-color:#ffffff'></td>";
						retTable = String.valueOf(retTable) + "</tr>";
			            notifalrets.remove(alertBean);
			            sendAlertemail="Y";
			            alertBean.insertalertLog(alertBean.getExecutionid(), ALERT_GROUP, alertBean.getALERT_NAME(), alertBean.getALERT_ID(), alertBean.getSTART_TIME(), alertBean.getEND_TIME(), alertBean.getAlertResult(), "Warning", alertBean.getErrorMsg());
					}
					
				}
				}
				    
			}
			List<AlertBean> NAalrets = new ArrayList<AlertBean>(notifalrets);
			for (AlertBean alertBean : notifalrets) {
				long result = alertBean.getAlertResult();
				int notif = alertBean.getNOTIFY_THRESHOLD();
				String condition=alertBean.getTHRESHOLD_condition();
				if(notif==-1&&alertBean.getErrorMsg()==null) {
				retTable = String.valueOf(retTable) + trnotif;
				
				retTable = String.valueOf(retTable) +"<td>" + alertBean.getALERT_NAME() + "</td>"+"<td>" + alertBean.getDb_host() + "</td>"+ "<td style='background-color:"+notifcolor+"'>" + result + "</td>";
				if(condition.equalsIgnoreCase("GREATER_THAN")) {
				retTable = String.valueOf(retTable) + "<td> <span style='font-size: "+String.valueOf(fontsize-6)+"px;'>Less than </span>" + String.valueOf(Math.max(Integer.valueOf(alertBean.getWARNING_THRESHOLD() - 1),0))  + "</td>";
				}
				if(condition.equalsIgnoreCase("LESS_THAN")) {
					retTable = String.valueOf(retTable) + "<td> <span style='font-size: "+String.valueOf(fontsize-6)+"px;'>Greater than </span>" + String.valueOf(alertBean.getWARNING_THRESHOLD() + 1) + "</td>";
					}
				retTable = String.valueOf(retTable) + "<td>" +  alertBean.getOwner() + "</td>";
				retTable = String.valueOf(retTable) + "<td style='background-color:#ffffff;border-color:#ffffff'></td>";
	            retTable = String.valueOf(retTable) + "</tr>";
	            sendAlertemail="Y";
	            NAalrets.remove(alertBean);
	            alertBean.insertalertLog(alertBean.getExecutionid(), ALERT_GROUP, alertBean.getALERT_NAME(), alertBean.getALERT_ID(), alertBean.getSTART_TIME(), alertBean.getEND_TIME(), alertBean.getAlertResult(), "Normal", alertBean.getErrorMsg());
				}
				else if(notif!=-1&&alertBean.getErrorMsg()==null){
				if(condition.equalsIgnoreCase("GREATER_THAN"))
				{
					if (result>notif) {
						retTable = String.valueOf(retTable) + trnotif;
						retTable = String.valueOf(retTable) +"<td>" + alertBean.getALERT_NAME() + "</td>"+"<td>" + alertBean.getDb_host() + "</td>"+ "<td style='background-color:"+notifcolor+"'>" + result + "</td>";
						retTable = String.valueOf(retTable) + "<td> <span style='font-size: "+String.valueOf(fontsize-6)+"px;'>Less than </span>" + String.valueOf(Math.max(Integer.valueOf(alertBean.getWARNING_THRESHOLD() - 1),0))  + "</td>";
						retTable = String.valueOf(retTable) + "<td>" +  alertBean.getOwner() + "</td>";
						retTable = String.valueOf(retTable) + "<td style='background-color:#ffffff;border-color:#ffffff'></td>";
						retTable = String.valueOf(retTable) + "</tr>";
			            NAalrets.remove(alertBean);
			            sendAlertemail="Y";
			            alertBean.insertalertLog(alertBean.getExecutionid(), ALERT_GROUP, alertBean.getALERT_NAME(), alertBean.getALERT_ID(), alertBean.getSTART_TIME(), alertBean.getEND_TIME(), alertBean.getAlertResult(), "Normal", alertBean.getErrorMsg());
			           
					}
					
				}else if (condition.equalsIgnoreCase("LESS_THAN")) {
					
					if (result<notif) {
						retTable = String.valueOf(retTable) + trnotif;
						retTable = String.valueOf(retTable) +"<td>" + alertBean.getALERT_NAME() + "</td>"+"<td>" + alertBean.getDb_host() + "</td>"+ "<td style='background-color:"+notifcolor+"'>" + result + "</td>";
						retTable = String.valueOf(retTable) + "<td> <span style='font-size: "+String.valueOf(fontsize-6)+"px;'>Greater than </span>" + String.valueOf(alertBean.getWARNING_THRESHOLD() + 1) + "</td>";
						retTable = String.valueOf(retTable) + "<td>" +  alertBean.getOwner() + "</td>";
						retTable = String.valueOf(retTable) + "<td style='background-color:#ffffff;border-color:#ffffff'></td>";
						retTable = String.valueOf(retTable) + "</tr>";
			            NAalrets.remove(alertBean);
			            sendAlertemail="Y";
			            alertBean.insertalertLog(alertBean.getExecutionid(), ALERT_GROUP, alertBean.getALERT_NAME(), alertBean.getALERT_ID(), alertBean.getSTART_TIME(), alertBean.getEND_TIME(), alertBean.getAlertResult(), "Normal", alertBean.getErrorMsg());
					}
					
				}
				}
				    
			}
			
			
			 String outofWin = Util.getDBProperty("display.out.of.window.alerts");
				if(!outofWin.equalsIgnoreCase("N")) {
			for (AlertBean alertBean : NAalrets) {
			  
				retTable = String.valueOf(retTable) + trnotif;
				retTable = String.valueOf(retTable) +"<td>" + alertBean.getALERT_NAME() + "</td>"+"<td>" + alertBean.getDb_host() + "</td>"+ "<td style='background-color:"+nacolor+"'>" + alertBean.getErrorMsg() + "</td>";
				retTable = String.valueOf(retTable) + "<td>" + "" + "</td>";
				retTable = String.valueOf(retTable) + "<td>" +  alertBean.getOwner() + "</td>";
				retTable = String.valueOf(retTable) + "<td style='background-color:#ffffff;border-color:#ffffff'></td>";
				retTable = String.valueOf(retTable) + "</tr>";
				 alertBean.insertalertLog(alertBean.getExecutionid(), ALERT_GROUP, alertBean.getALERT_NAME(), alertBean.getALERT_ID(), alertBean.getSTART_TIME(), alertBean.getEND_TIME(), alertBean.getAlertResult(), "NA", alertBean.getErrorMsg());
			}
			}
			
			
			

			 htmlMessage=htmlMessage+ String.valueOf(retTable) + "</table></div><div><br>&nbsp;&nbsp;<span style='border: 1px solid #1C6EA4; border-radius:50px;text-align: center; background-color:"+criticalcolor+"'>&nbsp;&nbsp; Critical &nbsp;</span> &nbsp;&nbsp;"
			 		+ "<span style='border: 1px solid #1C6EA4; text-align: center;border-radius:50px; background-color:"+warningcolor+";'>&nbsp;&nbsp;  Warning &nbsp;</span><br>"
			 		+ "</div><hr>";
			Util.print_log(String.valueOf(htmlMessage) + "<pre>Description : " + DESCRIPTION + "<br>Owner : "
					+ REQUESTOR + "<br>Execution Id : "+executionId+ "</pre></body></html>");
			
			if (alrets==null||alrets.size()==0) {
				 htmlMessage=htmlMessage+ "<span style='border: 1px solid #1C6EA4; text-align: center;border-radius:50px; background-color:"+criticalcolor+";'>&nbsp;&nbsp;  No alerts found ! &nbsp;</span><br>";
			}
			
			if ( !sendAlertemail.equalsIgnoreCase("N")) {
				SendEmail.sendEmail(EMAIL_LIST, Util.getDBProperty("mail.subject") + ALERT_GROUP +" "+ HEAD , String.valueOf(
						htmlMessage) + "<pre>Description : " + DESCRIPTION + "<br>Owner : " + REQUESTOR + "<br>Execution Id : "+executionId+ "</pre>"
						+ "<br><BR><div style=\"text-align:right;color:#e1eaff;font-style: italic;\"> - Tasker </div></body></html>",
						null, null);
			}else {
				Util.print_log("No ALert email Sent , All is well ! \n change property 'alert.sendAlertEmail.inBAU' in TASKER_CONFIG to Y if email needed always. ");
			}
			
			 }
       catch (Exception e) {
       	 try {
       		 htmlMessage="<html><body> Exception in excuting task. " +e.getMessage();
       		 
				SendEmail.sendEmail(EMAIL_LIST,Util.getDBProperty("mail.subject")+EMAIL_SUBJECT, String.valueOf(htmlMessage) + "</body></html>",null,null);
				
			} catch (Exception e1) {
				e1.printStackTrace();
			}
           e.printStackTrace();
       }
   }
    
    public static void byreq(String[] args){
    	String repname=args[0];
    	Connection con=null;
    	 Statement stmt=null,stmt1 =null;
    	  ResultSet rs=null;
        try {
        	con=Util.getConnection(CONSTANTS.MAINDB);
        	
        	stmt = con.createStatement();
        	stmt1 = con.createStatement();
        	  rs = stmt.executeQuery("select ROWID,tasker_Request.* from tasker_Request where Enabled='Y' and name='"+repname+"'");
        	 while (rs.next()) {
                 String ROWID=rs.getString("ROWID");
                 String FREQUENCY=rs.getString("FREQUENCY");
                 String TIME=rs.getString("TIME");
                 String PATH=rs.getString("PATH");
                 String FILENAME=rs.getString("FILENAME");
                 String REQUESTOR=rs.getString("REQUESTOR");
                 String DESCRIPTION=rs.getString("DESCRIPTION");
                 String NAME=rs.getString("NAME");
                 String EMAIL_SUBJECT=rs.getString("EMAIL_SUBJECT");
                 String HEAD=rs.getString("EMAIL_HEAD");
                 String ifPrecon=rs.getString("Email_IF_PRE_COND_IS_TRUE");
                 
                 if (HEAD ==null){
                	 HEAD="";
                 }
                 String EMAIL_LIST=rs.getString("EMAIL_LIST");
                 Timestamp LASTEXECUTED=rs.getTimestamp("LASTEXECUTED");
                 if (LASTEXECUTED ==null)
                 {
                	 LASTEXECUTED=new Timestamp(0);
                 }
                 System.out.println("FILENAME : "+FILENAME);
                 System.out.println("PATH "+PATH);
                 System.out.println("EMAIL_SUBJECT "+EMAIL_SUBJECT);
                 System.out.println("EMAIL_LIST "+EMAIL_LIST);
                 Calendar lastExecutetime=Calendar.getInstance();
                 lastExecutetime.setTime(LASTEXECUTED);
                 Calendar now=Calendar.getInstance();
                
                                          
                
                
                 System.out.println(FILENAME + " Now " +now.getTime().toLocaleString());
                 System.out.println(FILENAME + " lastExecutetime  " +lastExecutetime.getTime().toLocaleString());
                
                 System.out.println(FILENAME + " last hrs " +lastExecutetime.get(Calendar.HOUR_OF_DAY));
                 System.out.println(FILENAME + "  last min " +lastExecutetime.get(Calendar.MINUTE));
                 System.out.println(FILENAME + " day " +lastExecutetime.get(Calendar.DAY_OF_MONTH));
                 System.out.println(FILENAME + " day now " +now.get(Calendar.DAY_OF_MONTH));
                 
                	 
                			 stmt1.executeUpdate("update tasker_Request set LastExecuted=sysdate,FREQUENCY='RUNNING' where rowid='"+ROWID+"'");
                			 if (repname.equalsIgnoreCase("ALERT")) {
                				 if(args[1]!=null&&args[1].trim().equals("*")) {
                					 Util.print_log(args[1]);
                					 Connection conx=null;
                			    	 Statement stmtx=null;
                			    	  ResultSet rsx=null;
                			        try {
                			        	conx=Util.getConnection(PATH);
                			        	
                			        	stmtx = conx.createStatement();
                			        	
                			            rsx = stmtx.executeQuery("select distinct ALERT_GROUP from "+FILENAME+" where IS_VALID='Y'");
                			        	 while (rsx.next()) { 
                			        		 
                			        		String ALERT_GROUP= rsx.getString(1);
                			        		 
                			               Util.print_log("Running for " + ALERT_GROUP + " ..");
                			               generateAlertReport(FILENAME,PATH,EMAIL_SUBJECT,EMAIL_LIST,NAME,DESCRIPTION,REQUESTOR,HEAD,ifPrecon,ALERT_GROUP);
                			             }
                			        }
                					 
                				 
                				 catch (Exception e) {
									e.printStackTrace();
								}finally {
									try {
			        	        		  if(rsx!=null)
			        	        			  rsx.close();
			        	        		  if(stmtx!=null)
			        	        			  stmtx.close();
			        	        		  if(conx!=null)
			        	        			  conx.close();
			        	        		  
			        					
			        				} catch (SQLException e) {
			        					
			        					e.printStackTrace();
			        				}
								}
                			        }
                				 else {
                				 	generateAlertReport(FILENAME,PATH,EMAIL_SUBJECT,EMAIL_LIST,NAME,DESCRIPTION,REQUESTOR,HEAD,ifPrecon,args[1]);
                				 }
                			 }else {
                			            generateReport(FILENAME,PATH,EMAIL_SUBJECT,EMAIL_LIST,NAME,DESCRIPTION,REQUESTOR,HEAD,ifPrecon);
                			 }
                			 stmt1.executeUpdate("update tasker_Request set LastExecuted=sysdate,FREQUENCY='HOURLY' where rowid='"+ROWID+"'");
                	
        		  
        		 
        		 
             }
            
        	
        }
        catch (Exception e) {
            e.printStackTrace();
        }finally {
        	  try {
        		  
              	if(rs!=null)
        		  rs.close();
              	if(stmt!=null)
        		  stmt.close();
              	if(stmt1!=null)
        		  stmt1.close();
              	if(con!=null)
				con.close();
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}
    
    
    }
}
