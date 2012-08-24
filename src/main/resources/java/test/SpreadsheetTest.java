/* SpreadsheetTest.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Aug 6, 2012 5:01:52 PM , Created by Sam
}}IS_NOTE

Copyright (C) 2012 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
*/
package test;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zkoss.poi.ss.usermodel.Cell;
import org.zkoss.poi.ss.usermodel.Row;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zats.mimic.Zats;
import org.zkoss.zats.mimic.impl.OperationAgentManager;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zul.Window;

import test.SpreadsheetAgentBuilder.SpreadsheetAgent;

/**
 * @author Sam
 *
 */
public class SpreadsheetTest {

	@BeforeClass
	public static void init() {
		 Zats.init("./src/main/webapp");
		 OperationAgentManager manager = OperationAgentManager.getInstance();
		 manager.registerBuilder("5.0.0", "*", Spreadsheet.class, new SpreadsheetAgentBuilder());
	}
	
	@AfterClass
    public static void end() {
        Zats.end();
    }

	@Test
	public void editFormulaTest() {
		DesktopAgent desktop = Zats.newClient().connect("/index.zul");
		SpreadsheetAgent spreadsheetAgent = desktop.query("spreadsheet").as(SpreadsheetAgent.class);
		
		spreadsheetAgent.setEditText(0, 0, "1");//input "1" in A1
		spreadsheetAgent.setEditText(0, 1, "2");//input "2" in B1
		spreadsheetAgent.setEditText(0, 2, "=A1+B1");//input formula in C1
		
		Spreadsheet spreadsheet = spreadsheetAgent.getSpreadsheet();
		Row row = spreadsheet.getSelectedSheet().getRow(0);
		Cell cell = row.getCell(2);
		Assert.assertEquals(Cell.CELL_TYPE_FORMULA, cell.getCellType());
		Assert.assertEquals(Cell.CELL_TYPE_NUMERIC, cell.getCachedFormulaResultType());
		Assert.assertEquals(Double.valueOf(3), cell.getNumericCellValue());
	}
	
	@Test
	public void openFormatNumberDialogTest() {
		DesktopAgent desktop = Zats.newClient().connect("/index.zul");
		SpreadsheetAgent spreadsheetAgent = desktop.query("spreadsheet").as(SpreadsheetAgent.class);
		
		spreadsheetAgent.postActionCommand(6, 8, SpreadsheetAgent.ACTION_FORMAT_CELL);
		
		ComponentAgent windowAgent = desktop.query("window[title='Number Format']");
		Assert.assertNotNull(windowAgent);
		
		Window win = (Window) windowAgent.getDelegatee();
		Assert.assertEquals(true, win.isVisible());
		Assert.assertEquals("modal", win.getMode());
		Assert.assertEquals("Number Format", win.getTitle());
	}
	
	@Test
	public void customFormulaTest() {
		DesktopAgent desktop = Zats.newClient().connect("/test/customizeFormula.zul");
		
		SpreadsheetAgent spreadsheetAgent = desktop.query("spreadsheet").as(SpreadsheetAgent.class);
		spreadsheetAgent.setEditText(0, 0, "=bar(1, 2, 3)");
		
		Spreadsheet spreadsheet = spreadsheetAgent.getSpreadsheet();
		Row row = spreadsheet.getSelectedSheet().getRow(0);
		Cell cell = row.getCell(0);
		
		Assert.assertEquals(Cell.CELL_TYPE_FORMULA, cell.getCellType());
		Assert.assertEquals(Cell.CELL_TYPE_NUMERIC, cell.getCachedFormulaResultType());
		Assert.assertEquals(Double.valueOf(3.1415), cell.getNumericCellValue());
	}
}
