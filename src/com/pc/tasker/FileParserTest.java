package com.pc.tasker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FileParserTest {
	private String content=null;
	private String contentorig=null;
	private ArrayList<String> operArr=null;
	//private ArrayList<String> condArr=null;
	//private String ifcond=null;
	public String htmlmessage="";
	
	FileParserTest(String file,String path) throws Exception{
		
		this.content=Util.readfile(file,path).trim();
		contentorig=content;
		content=content.toUpperCase();
		operArr=new ArrayList<String>();
		//condArr=new ArrayList<String>();
		operArr.add("=");operArr.add("!=");operArr.add("<=");operArr.add(">=");
	}
	
	
	
	public static void main(String[] args) throws Exception {
		
		FileParserTest fp=new FileParserTest("taskertest.txt", "");		
		System.out.println(fp.content);
		
		fp.processfile();
		System.out.println(fp.htmlmessage);
		
	}
	
	
	public String fetchQuery (String queryid ){
		
		return null;		
	}
	
	public boolean evalif () throws Exception{
		String eq=content.substring(3,content.indexOf("RUN ")).trim();
		System.out.println(eq);				
			if(eq.contains("!=")){
				String eqarr[]=eq.split("!=");
				 long ret=executeQuery(eqarr[0]);
				 System.out.println("Ret = "+ret);
				 if(ret!=Long.parseLong(eqarr[1])){
					 System.out.println("TRUE");
					 return true;							 
				 }else {
					System.out.println("FALSE");
					return false;
				}
					
			}else if (eq.contains("<=")) {
				String eqarr[]=eq.split("<=");
				 long ret=executeQuery(eqarr[0]);
				 System.out.println("Ret = "+ret);
				 if(ret<=Long.parseLong(eqarr[1])){
					 System.out.println("TRUE");
					 return true;							 
				 }else {
					System.out.println("FALSE");
					return false;
				}
			}else if (eq.contains(">=")) {
				String eqarr[]=eq.split(">=");
				 long ret=executeQuery(eqarr[0]);
				 System.out.println("Ret = "+ret);
				 if(ret>=Long.parseLong(eqarr[1])){
					 System.out.println("TRUE");
					 return true;							 
				 }else {
					System.out.println("FALSE");
					return false;
				}
				
			}else if (eq.contains("=")){
				String eqarr[]=eq.split("=");
				 long ret=executeQuery(eqarr[0]);
				 System.out.println("Ret = "+ret);
				 if(ret==Long.parseLong(eqarr[1])){
					 System.out.println("TRUE");
					 return true;							 
				 }else {
					System.out.println("FALSE");
					return false;
				}
			}else if (eq.contains(">")){
				String eqarr[]=eq.split(">");
				 long ret=executeQuery(eqarr[0]);
				 System.out.println("Ret = "+ret);
				 if(ret>Long.parseLong(eqarr[1])){
					 System.out.println("TRUE");
					 return true;							 
				 }else {
					System.out.println("FALSE");
					return false;
				}
			}else if (eq.contains("<")){
				String eqarr[]=eq.split("<");
				 long ret=executeQuery(eqarr[0]);
				 System.out.println("Ret = "+ret);
				 if(ret<Long.parseLong(eqarr[1])){
					 System.out.println("TRUE");
					 return true;							 
				 }else {
					System.out.println("FALSE");
					return false;
				}
			}
		
		
		
	//	String id=this.content.substring();
		return false;		
	}
	
	public String processfile () throws Exception{
			
		String ifstr=content.substring(0, 3);
		System.out.println(ifstr+".");
		if (ifstr.equals("IF ")){			
			if(evalif()){
				executeIfRun(content.indexOf("IF "));
			}
			else{
				
				if(content.contains("ELSE ")){
					System.err.println("in else");
					executeElseRun(content.indexOf("ELSE "));
				}
		}
		}else {
			executeIfRun(content.indexOf("RUN "));
		}
		
		return htmlmessage;		
	}
	
	
	private void executeElseRun(int indexOf) throws Exception {
		
			String runpart=content.substring(indexOf+9,content.indexOf(";")).trim();
			System.out.println("RUn part"+runpart);
			processrun(runpart);
		
	}



	private void executeIfRun(int stind) throws Exception {
		
		if(content.contains("ELSE ")){
			String runpart=content.substring(stind,content.indexOf("ELSE ")).trim();
			runpart=runpart.substring(runpart.indexOf("RUN ")+4);
			processrun(runpart);
		}else{
			String runpart=content.substring(stind,content.indexOf(";")).trim();
			runpart=runpart.substring(runpart.indexOf("RUN ")+4);
			processrun(runpart);
		}
		
	}
	
	private void processrun(String runpart) throws Exception {		
		System.out.println("RUn part "+runpart);
		String runarr[]=runpart.split(" AND ");
		for (int i = 0; i < runarr.length; i++) {
			String task[]=runarr[i].split(" ");
			String type=task[0].trim();
			String name=task[1].trim();
			System.out.println(type);
			System.out.println(name);
			if(type.equals("SELECT")){
				executeselect(name);

			}else if(type.equals("UPDATE")){
				executeupdate(name);
				
			}else if(type.equals("DELETE")){
				executedelete(name);
				
			}else if(type.equals("BACKUP")){
				executebackup(name);
				
			}else if(type.equals("INSERT")){
				executeinsert(name);
				
			}
			
		}
	}


	private void executeinsert(String head) throws Exception {

		// 
		String query=getquery(head);
		String querycom=query.toLowerCase();
		String tablename=querycom.substring(querycom.indexOf("insert into ")+new String("insert into ").length(), querycom.indexOf("select")-1);
        
       // final Connection connection = Util.getConnection();
       final Connection connection = getcon();
        String retTable="";
        try {
           
            final Statement stmt = connection.createStatement();
            final int rs = stmt.executeUpdate(query);
            retTable = "<h3>" + head + "</h3> - "+rs+" rows Inserted in table <b>"+tablename+"</b>";
           
        }
        catch (Exception e) {
            throw e;
        }
        finally {
            connection.close();
        }
       
        htmlmessage=htmlmessage+ String.valueOf(retTable) + "<hr>";
    
		
	
		
	
		
	}



	private void executebackup(String head) throws Exception {


		String query=getquery(head);
		String querycom=query.toLowerCase();
		String tablename=querycom.substring(querycom.indexOf("create table ")+new String("create table ").length(), querycom.indexOf("as"));
        //final Connection connection = Util.getConnection();
		System.out.println(tablename);
		Calendar calendar=Calendar.getInstance();
		
		String tablenamenew=tablename.trim()+calendar.get(Calendar.YEAR)+calendar.get(Calendar.MONTH)+calendar.get(Calendar.DATE)+calendar.get(Calendar.HOUR)+calendar.get(Calendar.MINUTE)+calendar.get(Calendar.SECOND)+" ";
		query=query.replace(tablename, tablenamenew);
        final Connection connection = getcon();
        System.out.println(query);
        String retTable="";
        try {
           
            final Statement stmt = connection.createStatement();
            final int rs = stmt.executeUpdate(query);
            retTable = "<h3>" + head + "</h3> - "+rs+" rows backed up in table <b>"+tablenamenew+"</b>";
           
        }
        catch (Exception e) {
            throw e;
        }
        finally {
            connection.close();
        }
        
        htmlmessage=htmlmessage+ String.valueOf(retTable) + "<hr>";
    
		
	
		
	
		
	
		
	}



	private void executedelete(String head) throws Exception {

		// 
		String query=getquery(head);
       // final Connection connection = Util.getConnection();
        final Connection connection = getcon();
        String retTable="";
        try {
           
            final Statement stmt = connection.createStatement();
            final int rs = stmt.executeUpdate(query);
            retTable = "<h3>" + head + "</h3> - "+rs+" rows Deleted.";
           
        }
        catch (Exception e) {
            throw e;
        }
        finally {
            connection.close();
        }
       
        htmlmessage=htmlmessage+ String.valueOf(retTable) + "<hr>";
    
		
	
		
	
		
	}



	private void executeupdate(String head) throws Exception {
		// 
		String query=getquery(head);
        //final Connection connection = Util.getConnection();
        final Connection connection = getcon();
        String retTable="";
        try {
           
            final Statement stmt = connection.createStatement();
            final int rs = stmt.executeUpdate(query);
            retTable = "<h3>" + head + "</h3> - "+rs+" rows updated.";
           
        }
        catch (Exception e) {
            throw e;
        }
        finally {
            connection.close();
        }
      
        htmlmessage=htmlmessage+ String.valueOf(retTable) + "<hr>";
    
		
	
		
	}



	private void executeselect(String head) throws Exception {
		String query=getquery(head);
        //final Connection connection = Util.getConnection();
        final Connection connection = getcon();
        String retTable = "" + head + "<div ><table id='tfhover' class='tftable' border='1'><tr>";
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
            connection.close();
        }
       
        htmlmessage=htmlmessage+ String.valueOf(retTable) + "</table></div><hr>";
    
		
	}


	

	public long executeQuery(String queryname) throws Exception{
		
		String query=getquery(queryname);
		Connection con=null;
		
		
		Statement stmt =null;
		ResultSet rs=null;
		try{
			//con=Util.getConnection();
			 con = getcon();
        	stmt = con.createStatement();
        	  rs = stmt.executeQuery(query);
        	  
        	  rs.next();
        	  return Long.parseLong(rs.getString(1));
        	  
        	  
		}catch (Exception e) {
           throw e;
        }finally {
        	  try {
        		  rs.close();
        		  stmt.close();
				con.close();
			} catch (SQLException e) {
				
				e.printStackTrace();
				throw e;
			}
		}
		
		
		
	}
	

	
	

private String getquery(String queryname) {
	String declaresec=content.substring(content.indexOf("DECLARE"));
	int stindex=declaresec.indexOf(queryname);
	String query=declaresec.substring(stindex, declaresec.indexOf(";", stindex)).split("->")[1].trim();
	String queryorig=contentorig.substring(content.indexOf(query), content.indexOf(query)+query.length());
	System.out.println(declaresec);
	System.out.println(queryorig);
	return queryorig;
	}



private Connection getcon() throws SQLException{
	
	String connstr = "jdbc:oracle:thin:@" + "10.20.164.106" + ":" + "1521" + ":" + "SINDB106";
	return DriverManager.getConnection(connstr, "ABPAPP1", "ABPAPP1");
}
	
}
