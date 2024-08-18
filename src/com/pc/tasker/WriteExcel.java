package com.pc.tasker;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
public class WriteExcel {
	
	
	public static void main(String[] args) throws IOException {
		String excelFilePath = "NiceJavaBooks.xls";
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet();
		int rowCount = 0;
			Row row = sheet.createRow(rowCount++);
			Cell cell = row.createCell(0);
			cell.setCellValue("abc");
			cell = row.createCell(1);
			cell.setCellValue("xyz");
			cell = row.createCell(2);
			cell.setCellValue(123);
			try (FileOutputStream outputStream = new FileOutputStream(excelFilePath)) {
				workbook.write(outputStream);
			}	
		
	}
	
	public String generateSheet(String filename) throws Exception {
		String path=Util.getDBProperty("tempSheetpath");
		String excelFilePath = filename+".xls";
		if(path!=null && !path.trim().equals("")){
			excelFilePath=path+"/"+excelFilePath;
		}
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet();
		int rowCount = 0;
			Row row = sheet.createRow(rowCount++);
			Cell cell = row.createCell(0);
			cell.setCellValue("abc");
			cell = row.createCell(1);
			cell.setCellValue("xyz");
			cell = row.createCell(2);
			cell.setCellValue(123);
			try (FileOutputStream outputStream = new FileOutputStream(excelFilePath)) {
				workbook.write(outputStream);
			}
			
			
			
			return excelFilePath;	
		
	
	}
	
}
