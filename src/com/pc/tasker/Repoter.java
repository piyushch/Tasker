package com.pc.tasker;

import java.util.*;
import java.sql.*;

public class Repoter
{
    public static String getReports() throws Exception {
        final String queriesData = Util.readfile("reportQueries",CONSTANTS.PATH);
        final String[] queriesDataArr = queriesData.split(";");
        String returnHtml = "";
        for (int i = 0; i < queriesDataArr.length; ++i) {
            final String[] queryArr = queriesDataArr[i].split("->");
            final String queryHead = queryArr[0];
            final String query = queryArr[1];
            Util.print_log("Query Head=" + queryHead);
            Util.print_log("Query =" + query);
            try {
                returnHtml = String.valueOf(returnHtml) + getHtmlReport(queryHead, query);
            }
            catch (Exception e) {
                returnHtml = String.valueOf(returnHtml) + "Error in running report " + queryHead + " Query - " + query + " Error - " + e.getMessage();
                e.printStackTrace();
            }
        }
        return returnHtml;
    }
    
    public static String getHtmlReport(final String head, final String query) throws Exception {
        final Connection connection = Util.getConnection(CONSTANTS.MAINDB);
        String retTable = "<h3>" + head + "</h3><div ><table id='tfhover' class='tftable' border='1'><tr>";
        try {
            final String[] qryArr = query.split(" ");
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
        connection.close();
        return String.valueOf(retTable) + "</table></div><hr>";
    }
    
    public static void main(final String[] args) throws Exception {
        System.out.println(getReports());
    }
}
