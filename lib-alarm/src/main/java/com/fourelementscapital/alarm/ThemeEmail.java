/******************************************************************************
*
* Copyright: Intellectual Property of Four Elements Capital Pte Ltd, Singapore.
* All rights reserved.
*
******************************************************************************/

package com.fourelementscapital.alarm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * A com.fourelementscapital Class to write email message from ThemeEmailVO class into excel file
 */

public class ThemeEmail {
	
    private static Object getCellValue(Cell cell) {
        switch (cell.getCellType()) {
        case Cell.CELL_TYPE_STRING:
            return cell.getStringCellValue();
        case Cell.CELL_TYPE_BOOLEAN:
            return cell.getBooleanCellValue();
        case Cell.CELL_TYPE_NUMERIC:
            return cell.getNumericCellValue();
	    case Cell.CELL_TYPE_FORMULA:
	        switch(cell.getCachedFormulaResultType()) {
            case Cell.CELL_TYPE_NUMERIC:
            	return cell.getNumericCellValue();
            case Cell.CELL_TYPE_STRING:
            	return (cell.getRichStringCellValue()).toString();
	        }
        }
        return null;
    }
    
    public static List<ThemeEmailVO> readFromExcelFile(String themeSelected) throws IOException {
    	
    	List<ThemeEmailVO> themeEmailSelected = new ArrayList<ThemeEmailVO>();
    	
        List<ThemeEmailVO> themeEmailsList = new ArrayList<ThemeEmailVO>();
        FileInputStream inputStream = new FileInputStream(new File(Config.getConfigValue_email("excel_file")));
     
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet firstSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = firstSheet.iterator();
     
        String themeIterate = "";
        while (iterator.hasNext()) {
            Row nextRow = iterator.next();
            Iterator<Cell> cellIterator = nextRow.cellIterator();
            ThemeEmailVO themeEmailVO = new ThemeEmailVO();
     
            while (cellIterator.hasNext()) {
                Cell nextCell = cellIterator.next();
                int columnIndex = nextCell.getColumnIndex();
                switch (columnIndex) {
                case 0:
                	themeEmailVO.setEmail((String) getCellValue(nextCell));
                    break;
                case 1:
                	themeIterate = (String) getCellValue(nextCell);
                	themeEmailVO.setTheme(themeIterate);
                    break;
                case 2:
                	themeEmailVO.setName((String) getCellValue(nextCell));
                    break;
                case 3:
                	themeEmailVO.setLastName((String) getCellValue(nextCell));
                    break;
                case 4:
                	themeEmailVO.setPassword((String) getCellValue(nextCell));
                    break;
                }
            }
            
            if (themeSelected.equalsIgnoreCase(themeIterate)) {
            	themeEmailSelected.add(themeEmailVO);
            }
            
        }
        
        workbook.close();
        inputStream.close();
     
        return themeEmailSelected;
    }    	

}

 
