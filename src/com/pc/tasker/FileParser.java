package com.pc.tasker;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class FileParser {
	private String content=null;
	private String contentorig=null;
	private ArrayList<String> operArr=null;
	//private ArrayList<String> condArr=null;
	private String ifcond=null;
	public String htmlmessage="";
	public boolean ifcon=true;
	private  String pre="";
	private ArrayList<String> attach = null;
	private ArrayList<String> attachdel = null;
	
	FileParser(String file,String path) throws Exception{
		this.attach=new ArrayList<String>();
		this.attachdel=new ArrayList<String>();
		this.content=Util.readfile(file,path).trim();
		contentorig=content;
		content=content.toUpperCase();
		pre=content.substring(0, content.indexOf("DECLARE"));
		operArr=new ArrayList<String>();
		//condArr=new ArrayList<String>();
		operArr.add("=");operArr.add("!=");operArr.add("<=");operArr.add(">=");
	}
	
	
	
	public static void main(String[] args) throws Exception {
		
		FileParser fp=new FileParser("taskertest.txt", "");		
		System.out.println(fp.content);
		
		fp.processfile();
		System.out.println(fp.htmlmessage);
		
	}
	
	
	public String fetchQuery (String queryid ){
		
		return null;		
	}
	
	public boolean evalif () throws Exception{
		String eq=content.substring(3,content.indexOf("RUN ")).trim();
		Util.print_log(eq);	
		ifcond=eq;
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
		System.out.println("Pre : "+pre);
		if (ifstr.equals("IF ")){			
			if(evalif()){
				//htmlmessage=htmlmessage+"<h3>Precondition \""+ifcond+"\" satisfied.</h3>";
				this.ifcon=true;				
				executeIfRun(content.indexOf("IF "));				
			}
			else{
				//htmlmessage=htmlmessage+"<h3>Precondition  \""+ifcond+"\" not satisfied.</h3>";
				this.ifcon=false;
				
				if(pre.contains("ELSE ")){
					System.err.println("in else");
					executeElseRun(content.indexOf("ELSE "));
				}
		}
		}else {
			executeIfRun(content.indexOf("RUN "));
		}
		if(ifcon){
			if (ifcond!=null&&!ifcond.equalsIgnoreCase("null")) {
			htmlmessage=htmlmessage+"<pre>Report Precondition \""+ifcond+"\" satisfied.</pre>";
			}
		}else{
			htmlmessage=htmlmessage+"<pre>Report Precondition  \""+ifcond+"\" not satisfied.</pre>";
		}
		return htmlmessage;		
	}
	
	
	private void executeElseRun(int indexOf) throws SQLException {
		
			String runpart=content.substring(indexOf+9,content.indexOf(";",pre.indexOf("ELSE "))).trim();
			System.out.println("RUn part"+runpart);
			processrun(runpart);
		
	}



	private void executeIfRun(int stind) throws SQLException {
		
		/*if(content.contains("ELSE ")){
			String runpart=content.substring(stind,content.indexOf("ELSE ")).trim();
			runpart=runpart.substring(runpart.indexOf("RUN ")+4);
			processrun(runpart);
		}else*/{
			String runpart=content.substring(stind,content.indexOf(";")).trim();
			runpart=runpart.substring(runpart.indexOf("RUN ")+4);
			processrun(runpart);
		}
		
	}
	
	private void processrun(String runpart)  {		
		System.out.println("RUn part "+runpart);
		String runarr[]=runpart.split(" AND ");
		for (int i = 0; i < runarr.length; i++) {
			String task[]=runarr[i].split(" ");
			String type=task[0].trim();
			String name=task[1].trim();
	//		System.out.println(type);
		//	System.out.println(name);
			try{
								
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
				
			}else if(type.equals("DROP")){
				executedrop(name);
				
			}
			else if(type.equals("CREATE")){
				executecreate(name);
				
			}else if(type.equals("GRANT")){
				executegrant(name);
				
			}else if(type.equals("EXPORT")){
				executeexport(name);
				
			}else if(type.equals("ATTACH")){
				executeattach(name);
				
			}else if(type.equals("EXECUTE")){
				executeexecute(name);
				
			}
			else if(type.equals("SQLFILE")){
				executesqlfile(name);
				
			}
			}catch (Exception e){
				htmlmessage=htmlmessage+"Error executing "+name+". Error : "+e.getMessage() +"<hr>";
			}
			
		}
	}


	private void executeexecute(String head) throws Exception {


		Query q=getquery(head);
		String query=q.query;
		
        
       
        String retTable="";
		try {
			String[] cmd={ "/bin/sh", "-c", query};
        	//String cmd = query;
    		Runtime run = Runtime.getRuntime();
    		Process pr = run.exec(cmd);
    		pr.waitFor();
    		BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
    		String line = "";
    		StringBuffer output=new StringBuffer();
    		while ((line=buf.readLine())!=null) {
    			output.append(line);
    		//System.out.println(line);
    		}
    		
    		System.out.println(head + "Output = "+output);
    		String outputStr=output.toString();
            
            retTable = "" + head + "- <b>"+outputStr +"</b>";
           
        }
        catch (Exception e) {
            throw e;
        }
        finally {
        	
        }
       if(q.sendmail==true){
        htmlmessage=htmlmessage+ String.valueOf(retTable) + "<hr>";
       }
    
		
	
		
	
		
	
		
	}



	private void executegrant(String head) throws Exception {


		Query q=getquery(head);
		String query=q.query;
		String querycom=query.toLowerCase();
		String tablename=querycom.substring(querycom.indexOf("on ")+new String("on ").length(), querycom.indexOf("to"));
        
        final Connection connection = Util.getConnection(q.db);
         Statement stmt=null;
       // final Connection connection = getcon();
        String retTable="";
        
        try {
           
        	stmt = connection.createStatement();
            final int rs = stmt.executeUpdate(query);
            
            retTable = "" + head + "- Grant succeed on table <b>"+tablename+" </b>";
           
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
       if(q.sendmail==true){
        htmlmessage=htmlmessage+ String.valueOf(retTable) + "<hr>";
       }
    
		
	
		
	
		
	
		
	}



	private void executecreate(String head) throws Exception {


		Query q=getquery(head);
		String query=q.query;
		String querycom=query.toLowerCase();
		String tablename=querycom.substring(querycom.indexOf("create table ")+new String("create table ").length(), querycom.indexOf("as"));
        
        final Connection connection = Util.getConnection(q.db);
         Statement stmt =null;
       // final Connection connection = getcon();
        String retTable="";
        try {
           
        	stmt = connection.createStatement();
            final int rs = stmt.executeUpdate(query);
            retTable = "" + head + "- <b>"+tablename+" created </b> with "+rs+" rows.";
           
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
       if(q.sendmail==true){
        htmlmessage=htmlmessage+ String.valueOf(retTable) + "<hr>";
       }
    
		
	
		
	
		
	
		
	}



	private void executedrop(String head) throws Exception {


		Query q=getquery(head);
		String query=q.query;
		String querycom=query.toLowerCase();
		String tablename=querycom.substring(querycom.indexOf("drop table ")+new String("drop table ").length());
        
        final Connection connection = Util.getConnection(q.db);
         Statement stmt=null;
       // final Connection connection = getcon();
        String retTable="";
        try {
           
        	stmt = connection.createStatement();
            final int rs = stmt.executeUpdate(query);
            retTable = "" + head + "- <b>"+tablename+" Dropped</b>";
           
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
       if(q.sendmail==true){
        htmlmessage=htmlmessage+ String.valueOf(retTable) + "<hr>";
       }
    
		
	
		
	
		
	
		
	}



	private void executeinsert(String head) throws Exception {

		Query q=getquery(head);
		String query=q.query;
		String querycom=query.toLowerCase();
		String tablename=querycom.substring(querycom.indexOf("insert into ")+new String("insert into ").length(), querycom.indexOf("select")-1);
        
        final Connection connection = Util.getConnection(q.db);
         Statement stmt=null;
       // final Connection connection = getcon();
        String retTable="";
        try {
           
        	stmt = connection.createStatement();
            final int rs = stmt.executeUpdate(query);
            retTable = "" + head + "- "+rs+" rows Inserted in table <b>"+tablename+"</b>";
           
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
       if(q.sendmail){
        htmlmessage=htmlmessage+ String.valueOf(retTable) + "<hr>";
       }
    
		
	
		
	
		
	}



	private void executebackup(String head) throws Exception {


		Query q=getquery(head);
		String query=q.query;
		String querycom=query.toLowerCase();
		String tablename=querycom.substring(querycom.indexOf("create table ")+new String("create table ").length(), querycom.indexOf("as"));
        final Connection connection = Util.getConnection(q.db);
         Statement stmt =null;
		System.out.println(tablename);
		Calendar calendar=Calendar.getInstance();
		
		String tablenamenew=tablename.trim()+calendar.get(Calendar.YEAR)+calendar.get(Calendar.MONTH)+calendar.get(Calendar.DATE)+calendar.get(Calendar.HOUR)+calendar.get(Calendar.MINUTE)+calendar.get(Calendar.SECOND)+" ";
		querycom=querycom.replace(tablename, tablenamenew);
				
       // final Connection connection = getcon();
		String grantq="grant all on "+tablenamenew+" to PUBLIC";
        System.out.println(querycom);
        System.out.println(grantq);
        String retTable="";
        try {
           
        	stmt  = connection.createStatement();
            final int rs = stmt.executeUpdate(querycom);
            retTable = "" + head + " - "+rs+" rows backed up in table <b>"+tablenamenew+"</b>";
            stmt.executeUpdate(grantq);
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
        if(q.sendmail){
        htmlmessage=htmlmessage+ String.valueOf(retTable) + "<hr>";
        }
		
	
		
	
		
	
		
	}



	private void executedelete(String head) throws Exception {

		Query q=getquery(head);
		String query=q.query;
        final Connection connection = Util.getConnection(q.db);
         Statement stmt=null;
        //final Connection connection = getcon();
        String retTable="";
        try {
           
        	stmt = connection.createStatement();
            final int rs = stmt.executeUpdate(query);
            retTable = "" + head + " - "+rs+" rows Deleted.";
           
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
       if(q.sendmail){
        htmlmessage=htmlmessage+ String.valueOf(retTable) + "<hr>";
       }
		
	
		
	
		
	}



	private void executeupdate(String head) throws Exception {
		Query q=getquery(head);
		String query=q.query;
        final Connection connection = Util.getConnection(q.db);
        Statement stmt=null;
         
        //final Connection connection = getcon();
        String retTable="";
        try {
           
        	stmt = connection.createStatement();
            final int rs = stmt.executeUpdate(query);
            retTable = "" + head + " - "+rs+" rows updated.";
           
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
      if(q.sendmail){
        htmlmessage=htmlmessage+ String.valueOf(retTable) + "<hr>";
      }
		
	
		
	}



	private void executeselect(String head) throws Exception {
		Query q=getquery(head);
		String query=q.query;
        final Connection connection = Util.getConnection(q.db);
         Statement stmt =null;
          ResultSet rs=null;
        //final Connection connection = getcon();
        String retTable = "" + head + "<div ><table id='tfhover' class='tftable' border='1'><tr>";
        try {
           
           stmt = connection.createStatement();
             rs= stmt.executeQuery(query);
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
        	htmlmessage=htmlmessage+" "+e.getMessage();
            throw e;
        }
        finally {
        	if(rs!=null)
        	rs.close();
        	if(stmt!=null)
        	stmt.close();
        	if(connection!=null)
            connection.close();
        }
       if(q.sendmail){
        htmlmessage=htmlmessage+ String.valueOf(retTable) + "</table></div><hr>";
       }
		
	}

	private void executeexport(String head) throws Exception {
		Query q=getquery(head);
		String query=q.query;
        final Connection connection = Util.getConnection(q.db);
         Statement stmt =null;
          ResultSet rs=null;
        //final Connection connection = getcon();
          String path=Util.getDBProperty("tempSheetpath");
                	  
  		String excelFilePath = head+".xls";
  		if(path!=null && !path.trim().equals("")){
  			excelFilePath=path+"/"+excelFilePath;
  		}
  		Workbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet();
		int rowCount = 0;
        String retTable = "" + head + " - Please find attachment";
        try {
           
           stmt = connection.createStatement();
             rs= stmt.executeQuery(query);
            final List<String> colNames = new ArrayList<String>();
            final ResultSetMetaData rsMetaData = rs.getMetaData();
            final int numberOfColumns = rsMetaData.getColumnCount();
            Row row = sheet.createRow(rowCount++);
            Cell cell = null;
            for (int i = 1; i <= numberOfColumns; ++i) {
            	
                colNames.add(rsMetaData.getColumnName(i));
                cell= row.createCell(i-1);cell.setCellValue(rsMetaData.getColumnName(i));

            }

            while (rs.next()) {
            	row = sheet.createRow(rowCount++);
                for (int i = 1; i <= numberOfColumns; i++) 
                {
                	cell= row.createCell(i-1);
                	cell.setCellValue(rs.getString(i));
                }

            }
            
            FileOutputStream outputStream = new FileOutputStream(excelFilePath);
				workbook.write(outputStream);
				this.attach.add(excelFilePath);
				this.attachdel.add(excelFilePath);
				 
			
        }
        catch (Exception e) {
        	htmlmessage=htmlmessage+" "+e.getMessage();
            throw e;
        }
        finally {
        	if(rs!=null)
            	rs.close();
            	if(stmt!=null)
            	stmt.close();
            	if(connection!=null)
                connection.close();
        }
       if(q.sendmail){
        htmlmessage=htmlmessage+ String.valueOf(retTable) + "</table></div><hr>";
       }
		
	}

	public ArrayList<String> getAttachdel() {
		return attachdel;
	}



	public void setAttachdel(ArrayList<String> attachdel) {
		this.attachdel = attachdel;
	}



	private void executeattach(String head) throws Exception {
		Query q=getquery(head);
		String query=q.query;
        String retTable = "" + head + " - Please find attachment";
     
				this.attach.add(query);
			
       if(q.sendmail){
        htmlmessage=htmlmessage+ String.valueOf(retTable) + "</table></div><hr>";
       }
		
	}
	private void executesqlfile(String head) throws Exception {


		Query q=getquery(head);
		String query=q.query;
		
        final Connection connection = Util.getConnection(q.db);
        ConnectBean connect = Util.connections.get(q.db);
        connection.close();
        String sqlcommand= "echo @"+query+"| sqlplus "+connect.getDBuser()+"/"+connect.getDBPAss()+"@"+connect.getConnstr().split(":")[5];
        System.out.println("echo @"+query+"| sqlplus "+connect.getDBuser()+"/"+"pass*****"+"@"+connect.getConnstr().split(":")[5]);
       
        String retTable="";
		try {
			String[] cmd={ "/bin/sh", "-c", sqlcommand};
        	//String cmd = query;
    		Runtime run = Runtime.getRuntime();
    		Process pr = run.exec(cmd);
    		pr.waitFor();
    		BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
    		String line = "";
    		StringBuffer output=new StringBuffer();
    		while ((line=buf.readLine())!=null) {
    			output.append(line);
    		//System.out.println(line);
    		}
    		
    		System.out.println(head + "Output = "+output);
    		String outputStr=output.toString();
            
            retTable = "" + head + "- <b>"+outputStr +"</b>";
           
        }
        catch (Exception e) {
            throw e;
        }
        finally {
        	
        }
       if(q.sendmail==true){
        htmlmessage=htmlmessage+ String.valueOf(retTable) + "<hr>";
       }
    
		
	
		
	
		
	
		
	}
	

	public ArrayList<String> getAttach() {
		return attach;
	}



	public void setAttach(ArrayList<String> attach) {
		this.attach = attach;
	}



	public long executeQuery(String queryname) throws Exception{
		
		Query q=getquery(queryname);
		String query=q.query;
		if (query.toLowerCase().contains("select")) {
			 Util.print_log("Running precondition Query - " + q.query);
		Connection con=null;
		
		
		Statement stmt =null;
		ResultSet rs=null;
		try{
			con=Util.getConnection(q.db);
			 //con = getcon();
        	stmt = con.createStatement();
        	  rs = stmt.executeQuery(query);
        	  
        	  rs.next();
        	  return Long.parseLong(rs.getString(1));
        	  
        	  
		}catch (Exception e) {
			htmlmessage=htmlmessage+" "+e.getMessage();
           throw e;
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
				throw e;
			}
		}
		}else {
			 Util.print_log("Running precondition command - " + q.query);
        	 String[] cmda={ "/bin/sh", "-c", q.query};
    		Runtime run = Runtime.getRuntime();
    		Process pr = run.exec(cmda);
    		pr.waitFor();
    		Util.print_log("output exit- "+ pr.exitValue());
    		BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
    		String line = "";
    		StringBuffer output=new StringBuffer();
    		int flag=0;
    		while ((line=buf.readLine())!=null) {
    			flag++;
    			output.append(line);
    			if(flag>1) {throw new Exception(" Failed pre condition ");}
    		//System.out.println("output - "+line);
    		}
    		
    		
    		return Long.valueOf(output.toString());
		}
		
		
	}
	

	
	

private Query getquery(String queryname) throws Exception {
	System.out.println("Searching query : "+queryname);
	Query query=new Query();
	try{
	String declaresec=contentorig.substring(content.indexOf("DECLARE")+7);
	String declare_version=declaresec.substring(0, 3);
	/*int stindex=declaresec.indexOf(queryname);
	System.out.println("test "+declaresec.substring(stindex, declaresec.indexOf(";", stindex)));
	String query=declaresec.substring(stindex, declaresec.indexOf(";", stindex)).split("->")[1].trim();*/
	String queryorig=null;
	String declaresecArr[]=null;
	
	if (declare_version.equalsIgnoreCase("_V1")) {
		Util.print_log("Declare version - "+declare_version);
		declaresec=declaresec.substring(3);
		declaresec=declaresec.replaceAll("->end;", CONSTANTS.DeclareEnd);
		declaresec=declaresec.replaceAll("->End;", CONSTANTS.DeclareEnd);
		declaresecArr=declaresec.split(CONSTANTS.DeclareEnd);
	}else {
		 declaresecArr=declaresec.split(";");
	}
	
	for (int i = 0; i < declaresecArr.length; i++) {
		//Util.print_log("Declare Section :"+declaresecArr[i]);
		String []item=declaresecArr[i].split("->");
		if(item[0].trim().equalsIgnoreCase(queryname.trim())){
			query.query=item[1];
			try{
			if(item[2]!=null && item[2].trim().equalsIgnoreCase("Y")){
			query.sendmail=true;
			}
			
			}catch(ArrayIndexOutOfBoundsException e){
				query.sendmail=false;
			}
			try{
				if(item[3]!=null){
				query.db=item[3];
				}
				
				}catch(ArrayIndexOutOfBoundsException e){
					query.db=CONSTANTS.MAINDB;
				}
			
		}
	}
	Util.print_log("Declare Section :"+declaresec);
	Util.print_log("Query name : "+queryname +" Query fetched :"+query.query);
	if (query.query==null || query.query.equals("")){
		throw new Exception("Error in finding query with name "+queryname);
	}
	return query;
	}
	catch(Exception e){
		e.printStackTrace();
		
		throw new Exception("Error in finding query with name "+queryname);
	}
	
	}


private Query getquery_new(String queryname) throws Exception {
	System.out.println("Searching query : "+queryname);
	Query query=new Query();
	try{
	String declaresec=contentorig.substring(content.indexOf("DECLARE")+7);
	String declare_version=declaresec.substring(0, 3);
	/*int stindex=declaresec.indexOf(queryname);
	System.out.println("test "+declaresec.substring(stindex, declaresec.indexOf(";", stindex)));
	String query=declaresec.substring(stindex, declaresec.indexOf(";", stindex)).split("->")[1].trim();*/
	String queryorig=null;
	String declaresecArr[]=null;
	
	if (declare_version.equalsIgnoreCase("_V1")) {
		Util.print_log("Declare version - "+declare_version);
		declaresec=declaresec.substring(3);
		declaresec=declaresec.replaceAll("->end;", CONSTANTS.DeclareEnd);
		declaresec=declaresec.replaceAll("->End;", CONSTANTS.DeclareEnd);
		declaresecArr=declaresec.split(CONSTANTS.DeclareEnd);
	}else {
		 declaresecArr=declaresec.split(";");
	}
	
	for (int i = 0; i < declaresecArr.length; i++) {
		//Util.print_log("Declare Section :"+declaresecArr[i]);
		String []item=declaresecArr[i].split("->");
		if(item[0].trim().equalsIgnoreCase(queryname.trim())){
			query.query=item[1];
			try{
			if(item[2]!=null && item[2].trim().equalsIgnoreCase("Y")){
			query.sendmail=true;
			}
			
			}catch(ArrayIndexOutOfBoundsException e){
				query.sendmail=false;
			}
			try{
				if(item[3]!=null){
				query.db=item[3];
				}
				
				}catch(ArrayIndexOutOfBoundsException e){
					query.db=CONSTANTS.MAINDB;
				}
			
		}
	}
	Util.print_log("Declare Section :"+declaresec);
	Util.print_log("Query name : "+queryname +" Query fetched :"+query.query);
	if (query.query==null || query.query.equals("")){
		throw new Exception("Error in finding query with name "+queryname);
	}
	return query;
	}
	catch(Exception e){
		e.printStackTrace();
		
		throw new Exception("Error in finding query with name "+queryname);
	}
	
	}



/*private Connection getcon() throws SQLException{
	
	String connstr = "jdbc:oracle:thin:@" + "10.20.164.106" + ":" + "1521" + ":" + "SINDB106";
	return DriverManager.getConnection(connstr, "ABPAPP1", "ABPAPP1");
}*/
	
}
