package com.pc.tasker;

import java.io.*;
import java.util.*;
import java.util.Date;
import java.sql.*;
import java.text.SimpleDateFormat;

public class Util
{
	
	private static HashMap<String, String> props = new HashMap<String, String>();
	public static HashMap<String, ConnectBean> connections = new HashMap<String, ConnectBean>();
	private static String connstr, DBuser, DBPAss;
	
		
	public static Connection getConnection(String db) throws Exception {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        }
        catch (ClassNotFoundException e) {
            System.out.println("Where is your Oracle JDBC Driver?");
            e.printStackTrace();
        }
        Connection connection = null;
        if (db==null||db.trim().length()==0)
        {
        	db=CONSTANTS.MAINDB;
        }
        try {
        	db=db.toLowerCase();
        	if(!connections.containsKey(db)) {
        	//String cmd = "tasker_getconn getconn";
        		String cmd="";
        		if (db.equalsIgnoreCase(CONSTANTS.MAINDB)) {
        			cmd = "tasker_getconn getconn";
        		}else {
        			cmd = "tasker_getconn getconn_"+db.trim();
        		}
    		Runtime run = Runtime.getRuntime();
    		Process pr = run.exec(cmd);
    		//pr.waitFor();
    		
    		long now = System.currentTimeMillis();
    	    long timeoutInMillis = 1000L * 900;
    	    long finish = now + timeoutInMillis;
    	    while ( isAlive( pr ) && ( System.currentTimeMillis() < finish ) )
    	    {
    	        Thread.sleep( 100 );
    	    }
    	    if ( isAlive( pr ) )
    	    {
    	        throw new InterruptedException( "Get Connection timeout out after " + 900 + " seconds" );
    	    }
    	    
    		
    		BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
    		String line = "";
    		StringBuffer output=new StringBuffer();
    		while ((line=buf.readLine())!=null) {
    			output.append(line);
    		//System.out.println(line);
    		}
    		
    		//System.out.println("Output"+output);
    		String outputStr=output.toString();
    		outputStr=outputStr.replaceAll(" ", "");
    		//System.out.println("Output"+outputStr);
    		String [] outarr=outputStr.toString().split("<end>;");
    		
    		
    		
    	
    		
    		 DBuser=outarr[0].substring(outarr[0].indexOf("DBuser=")+7);
    		 DBPAss=outarr[1].substring(outarr[1].indexOf("DBPAss=")+7);
    		String DBhost = outarr[2].substring(outarr[2].indexOf("DBhost=")+7);
    		String DBport=	outarr[3].substring(outarr[3].indexOf("DBport=")+7);
    		if (DBport==null||DBport.trim().equals(""))
    		{
    			DBport="1831";
    		}
    		String DBsid= outarr[4].substring(outarr[4].indexOf("DBsid=")+6);
    		
    		
    		        	
             connstr = "jdbc:oracle:thin:@" + DBhost + ":" + DBport + ":" + DBsid;
            //System.out.println("Connection str "+connstr+" "+DBuser+ " "+DBPAss);
             System.out.println("Connection str "+DBuser+connstr);
             connection = DriverManager.getConnection(connstr, DBuser, DBPAss);
             connections.put(db, new ConnectBean(connstr, DBuser, DBPAss));
        	}
        	else {
        		ConnectBean connect = connections.get(db);
        		 connection = DriverManager.getConnection(connect.getConnstr(), connect.getDBuser(), connect.getDBPAss());
        	}
        }
        catch (Exception e2) {
        	System.out.println("Connection Failed! Check output console");
        	connstr=null; DBuser=null; DBPAss=null;
            e2.printStackTrace();
            throw new Exception("Connection Failed!");
           // return null;
        } 
        
        //System.out.println(" Connection successful ! ");
        return connection;
    }
    
    public static void print_log(final String string) throws Exception {
        final String status = getDBProperty(CONSTANTS.proplog);
        if (status.equalsIgnoreCase("TRUE")) {
            System.out.println(String.valueOf(new Date().toLocaleString()) + " :  " + string);
        }
    }
    
    public static String readfile(final String filename,final String path) throws Exception {
        BufferedReader br = null;
        String data = "";
        Label_0183: {
            try {
            	String f=path + filename;
            	//System.out.println("File  : "+f);
                final File file = new File(f);
                if (!file.exists()) {
                    throw new Exception("File "+file.getAbsolutePath()+" not found");
                }
                br = new BufferedReader(new FileReader(path+ filename));
                String sCurrentLine;
                while ((sCurrentLine = br.readLine()) != null) {
                    data = String.valueOf(data) + sCurrentLine;
                }
            }
            catch (IOException e) {
                e.printStackTrace();
                try {
                    if (br != null) {
                        br.close();
                    }
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
                break Label_0183;
            }
            finally {
                try {
                    if (br != null) {
                        br.close();
                    }
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            try {
                if (br != null) {
                    br.close();
                }
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (data == null || data.equalsIgnoreCase("")) {
            data = "0";
        }
        return data;
    }
    
    public static String getProperty(final String name) throws Exception {
        final String settings = readfile(CONSTANTS.propFile,CONSTANTS.PATH);
        final String[] settingsArr = settings.split("\\|");
        for (int i = 0; i < settingsArr.length; ++i) {
            if (settingsArr[i].contains(name)) {
                final int start = settingsArr[i].indexOf("=");
                return settingsArr[i].substring(start + 1);
            }
        }
        return null;
    }
    
    
    public static String getDBProperty(final String name) throws Exception {
    	String ret = "";
    	if (props.containsKey(name)) {
    		ret=props.get(name);
    		 if (ret==null) {ret="";}
    	     return ret;
    		 	 }
    	
        final Connection connection = getConnection(CONSTANTS.MAINDB);
       // String ret = "";
        try {
            final Statement stmt = connection.createStatement();
            final ResultSet rs = stmt.executeQuery("select value from TASKER_CONFIG where name='"+name+"'");
         
         
                    
            while (rs.next()) {
             ret=  rs.getString(1);
            }
        }
        catch (Exception e) {
            throw e;
        }
        finally {
        	if(connection!=null)
            connection.close();
        }
        connection.close();
        if (ret==null) {ret="";}
        if (!props.containsKey(name)) {
        	props.put(name, ret);
    		 	 }
        return ret;
    }
    
    public static String getHtmlReport(final String query) throws Exception {
        final Connection connection = getConnection(CONSTANTS.MAINDB);
        String retTable = "<div ><table id='tfhover' class='tftable' border='1'><tr>";
        try {
            final Statement stmt = connection.createStatement();
            final ResultSet rs = stmt.executeQuery(query);
            final List<String> colNames = new ArrayList<String>();
            final ResultSetMetaData rsMetaData = rs.getMetaData();
            final int numberOfColumns = rsMetaData.getColumnCount();
            for (int i = 1; i <= numberOfColumns; ++i) {
                colNames.add(rsMetaData.getColumnName(i));
                retTable = String.valueOf(retTable) + "<th>" + rsMetaData.getColumnName(i) + "</th>";
            }
            retTable = String.valueOf(retTable) + "</tr>";
            while (rs.next()) {
                retTable = String.valueOf(retTable) + "<tr>";
                for (int i = 1; i <= numberOfColumns; retTable = String.valueOf(retTable) + "<td>" + rs.getString(i++) + "</td>") {}
                retTable = String.valueOf(retTable) + "</tr>";
            }
        }
        catch (Exception e) {
            throw e;
        }
        finally {
        	if(connection!=null)
            connection.close();
        }
        connection.close();
        return String.valueOf(retTable) + "</table></div><hr>";
    }
    
    public static void main(final String[] args) throws Exception {
        print_log(getProperty(CONSTANTS.propFdbcon));
        print_log(getProperty(CONSTANTS.proplog));
        print_log(getProperty(CONSTANTS.proprunInterval));
        print_log(getProperty(CONSTANTS.proprunInterval));
    }
    
    public static String runCommand(String cmd,boolean returnoutput) throws IOException, InterruptedException {
		Runtime run = Runtime.getRuntime();
		Process pr = run.exec(cmd);
		pr.waitFor();
		BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
		String line = "";
		StringBuffer output=new StringBuffer();
		while ((line=buf.readLine())!=null) {
			output.append(line+"\n");
		System.out.println(line);
		}		
		//System.out.println("Output"+output);
		if(returnoutput==true)
		return output.toString();
		else {
			return null;
		}
	}
    
    public static boolean isAlive( Process p ) {
        try
        {
            p.exitValue();
            return false;
        } catch (IllegalThreadStateException e) {
            return true;
        }
    }
    
    
    public static List<AlertBean> getAlrets(String ALERT_GROUP,String table,String db){
    	List<AlertBean> alerts=new ArrayList<AlertBean>();
    	List<AlertBean> alertsFiltered=new ArrayList<AlertBean>();
    	

    	Connection con=null;
    	 Statement stmt=null;
    	  ResultSet rs=null;
        try {
        	con=Util.getConnection(db);
        	
        	stmt = con.createStatement();
        	String dbHOME = Util.getDBProperty("home.db");
            String unixHOME = Util.getDBProperty("home.env");
            Calendar sysdate=Calendar.getInstance();
			SimpleDateFormat format=new SimpleDateFormat("HHmm");
			String formatExecutionid=new SimpleDateFormat("yyyyMMddHHmmss").format(sysdate.getTime());
			int now=Integer.parseInt(format.format(sysdate.getTime()));
        	  rs = stmt.executeQuery("select * from "+table+" where IS_VALID='Y' and ALERT_GROUP='"+ALERT_GROUP+"' order by PRIORITY");
        	 while (rs.next()) { 
        		 
        		 AlertBean alertBean=new AlertBean(rs.getString("ALERT_GROUP"),rs.getString("ALERT_NAME"), 
        				 rs.getString("ALERT_SCRIPT"), rs.getString("IS_VALID"), rs.getString("THRESHOLD_CONDITION"), rs.getInt("NOTIFY_THRESHOLD"),
        				 rs.getInt("WARNING_THRESHOLD"), rs.getInt("CRITICAL_THRESHOLD"), rs.getString("ALERT_TYPE"), rs.getString("ALERT_USER_HOST"), 
        				 rs.getString("APPLICATION_DB"), rs.getInt("PRIORITY"),rs.getString("ALERT_WINDOW"),rs.getString("NEXT_STEP"),"0",rs.getString("ALERT_ID"),rs.getString("ATTR_VALUE1"));
               alerts.add(alertBean);
        		 
               Util.print_log("Alert " + alertBean.getALERT_NAME() + " Added...");
             }
            
        	 Util.print_log("Alerts count : " + alerts.size());
        	 
        	 for (AlertBean alertBean : alerts) {
        		// Util.print_log( alertBean.toString()); 
        		 if(alertBean.getALERT_TYPE().equalsIgnoreCase("QUERY")) {
        			 String[] dbArr=null;
        			 if (alertBean.getAPPLICATION_DB()!=null) {
        			  dbArr=alertBean.getAPPLICATION_DB().split(",");
        			 }else {
        				  dbArr=new String[]{alertBean.getAPPLICATION_DB()};
        				 
        			 }
        			 for (int i = 0; i < dbArr.length; i++) {
        				 AlertBean alert=new AlertBean(alertBean.getALERT_GROUP(),alertBean.getALERT_NAME(),alertBean.getALERT_SCRIPT(),alertBean.getIS_VALID(),alertBean.getTHRESHOLD_condition(),alertBean.getNOTIFY_THRESHOLD(),alertBean.getWARNING_THRESHOLD(),alertBean.getCRITICAL_THRESHOLD(),alertBean.getALERT_TYPE(),alertBean.getALERT_USER_HOST(),dbArr[i],alertBean.getPRIORITY(),alertBean.getALERT_WINDOW(),alertBean.getNEXT_STEP(),formatExecutionid,alertBean.getALERT_ID(),alertBean.getOwner());
        				 if(dbArr[i]!=null) {
        				 alert.setDb_host(dbArr[i]);
        				 }else {
        				 alert.setDb_host(dbHOME);
        				 }
        				 alertsFiltered.add(alert);
					}
        		 }
        		 else if(alertBean.getALERT_TYPE().equalsIgnoreCase("COMMAND")) {
        			 String[] hostArr=null;
        			 if (alertBean.getALERT_USER_HOST()!=null) {
        				 hostArr=alertBean.getALERT_USER_HOST().split(",");
           			 }else {
           				hostArr=new String[]{alertBean.getALERT_USER_HOST()};
           			 }
        			 for (int i = 0; i < hostArr.length; i++) {
        				 AlertBean alert=new AlertBean(alertBean.getALERT_GROUP(),alertBean.getALERT_NAME(),alertBean.getALERT_SCRIPT(),alertBean.getIS_VALID(),alertBean.getTHRESHOLD_condition(),alertBean.getNOTIFY_THRESHOLD(),alertBean.getWARNING_THRESHOLD(),alertBean.getCRITICAL_THRESHOLD(),alertBean.getALERT_TYPE(),hostArr[i],alertBean.getAPPLICATION_DB(),alertBean.getPRIORITY(),alertBean.getALERT_WINDOW(),alertBean.getNEXT_STEP(),formatExecutionid,alertBean.getALERT_ID(),alertBean.getOwner());
        				 if(hostArr[i]!=null) {
        					 alert.setDb_host(hostArr[i]);
            				 }else {
            				 alert.setDb_host(unixHOME);
            				 }
        				
        				 alertsFiltered.add(alert);
					}
        		 }
        		 
        	 }
        	 
        	 for (AlertBean alertBean : alertsFiltered) {
        		 Util.print_log("Processing " + alertBean.getALERT_NAME() + " ...");
        		 
        		 boolean inWindow=true;
        		 
 				if (alertBean.getALERT_WINDOW()!=null&&alertBean.getALERT_WINDOW().contains("-")) {
 					try {
 					int alertStart=Integer.parseInt(alertBean.getALERT_WINDOW().split("-")[0]);
 					int alertEnd=Integer.parseInt(alertBean.getALERT_WINDOW().split("-")[1]);
 					/*
 					 * now 0100
 					 * start 2100
 					 * end 0300
 					 * 
 					 * 
 					 */
 					
 					if(alertStart>alertEnd) {
 						
 						if (now>=alertStart && now<2359) {
 	 						inWindow=true;
 	 					}else if (now>=0 && now<alertEnd) {
 	 						inWindow=true;
 	 					}
 						else {
 	 						inWindow=false;
 	 						Util.print_log("Alert not in run window "+alertBean.getALERT_WINDOW()+ " Current time "+now);
 						}
 						
 						
 					} 						
 					else if (now>=alertStart && now<alertEnd) {
 						inWindow=true;
 					}else {
 						inWindow=false;
 						Util.print_log("Alert not in run window "+alertBean.getALERT_WINDOW()+ " Current time "+now);
					}
 				
 					
 					}
 					catch (Exception e) {
 						e.printStackTrace();
 						Util.print_log(e.getMessage()+" Invalid Alert window value "+alertBean.getALERT_WINDOW());
					}
 					
 				}
 				
 				if(inWindow) {
        		 
        		 if(alertBean.getALERT_TYPE().equalsIgnoreCase("QUERY")) {
        		Connection conapp=null;
        		Statement stmta=null;
           	  	ResultSet rsa=null; 
           	  	
           		
				
        		 try {
        			 alertBean.setSTART_TIME(new java.sql.Timestamp(System.currentTimeMillis()));
        			 conapp=Util.getConnection(alertBean.getAPPLICATION_DB());
        			 if (conapp==null) {Util.print_log( "Connection Failed ! for "+alertBean.getAPPLICATION_DB());;throw new Exception("Connection Failed ! for "+alertBean.getAPPLICATION_DB());}
        			 stmta=conapp.createStatement();
        			
        			 String query=alertBean.getALERT_SCRIPT().trim();
        			 if(query.lastIndexOf(";")==query.length()-1) {
        				 query=query.substring(0, query.length()-1);
        			 }
        			 Util.print_log("Running alert query - " + query + " ...");
        			 rsa=stmta.executeQuery(query);
        			 while (rsa.next()) { 
        				 alertBean.setAlertResult(rsa.getLong(1));
        				 if(rsa.getRow()>1) {throw new Exception(" ");}
        			 }
        			 
        		 }catch (Exception e) {
        			 Util.print_log("Query should return single row with a count. "+e.getMessage());
        			 alertBean.setErrorMsg("Query should return single row with a count. "+e.getMessage());
        	            e.printStackTrace();
        	        }finally {
        	        	  try {
        	        		  if(conapp!=null)
        	        			  conapp.close();
        	        		  if(rsa!=null)
        	        		  rsa.close();
        	        		  if(stmta!=null)
        	        		  stmta.close();
        	        		  
        					
        				} catch (SQLException e) {
        					
        					e.printStackTrace();
        				}finally {
        					 alertBean.setEND_TIME(new java.sql.Timestamp(System.currentTimeMillis()));
        				}
        			}
        		 }else if (alertBean.getALERT_TYPE().equalsIgnoreCase("COMMAND")) {
					
        			 
        			 try {
        				 alertBean.setSTART_TIME(new java.sql.Timestamp(System.currentTimeMillis()));
        		        	String cmd = "" ;
        		        	if (alertBean.getALERT_USER_HOST()!=null && !alertBean.getALERT_USER_HOST().trim().equals("")) {
        		        		cmd="ssh "+alertBean.getALERT_USER_HOST()+" "+alertBean.getALERT_SCRIPT();
        		        	}else
        		        	{
        		        		cmd=alertBean.getALERT_SCRIPT();
        		        	}
        		        	 Util.print_log("Running alert command - " + cmd);
        		        	 String[] cmda={ "/bin/sh", "-c", cmd};
        		    		Runtime run = Runtime.getRuntime();
        		    		Process pr = run.exec(cmda);
        		    		pr.waitFor();
        		    		System.out.println("output exit- "+ pr.exitValue());
        		    		BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        		    		String line = "";
        		    		StringBuffer output=new StringBuffer();
        		    		int flag=0;
        		    		while ((line=buf.readLine())!=null) {
        		    			flag++;
        		    			output.append(line);
        		    			if(flag>1) {throw new Exception(" ");}
        		    		//System.out.println("output - "+line);
        		    		}
        		    		
        		    		
        		    		String outputStr=output.toString();
        		    		if(outputStr.length()>50) {
        		    			outputStr=outputStr.substring(0, 49);
        		    		}
        		    		alertBean.setAlertResult(Long.parseLong(outputStr));;
        		            
        		           
        		        }
        		        catch (Exception e) {
        		        	String errM=e.getMessage();
        		        	if(errM.length()>50) {
        		        		errM=	errM.substring(0, 49);
        		    		}
        		        	Util.print_log("Command should return single row with a count. "+errM);
        		        	alertBean.setErrorMsg("Command should return single row with a count. "+errM);
        		        	e.printStackTrace();
        		        }finally {
       					 alertBean.setEND_TIME(new java.sql.Timestamp(System.currentTimeMillis()));
       				}
        			 
        			 
				}
 				}else {
 					 alertBean.setErrorMsg("NA");
				}
        		 Util.print_log( alertBean.toString()); 
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
        		  if(con!=null)
				con.close();
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}
    
    	
    	
		return alertsFiltered;
    	
    	}
    
    
    
    }
