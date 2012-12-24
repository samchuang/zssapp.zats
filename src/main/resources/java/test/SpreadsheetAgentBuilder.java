/* SpreadsheetComponentAgent.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Aug 6, 2012 5:32:52 PM , Created by Sam
}}IS_NOTE

Copyright (C) 2012 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
*/
package test;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.impl.ClientCtrl;
import org.zkoss.zats.mimic.impl.OperationAgentBuilder;
import org.zkoss.zats.mimic.impl.operation.AgentDelegator;
import org.zkoss.zats.mimic.operation.OperationAgent;
import org.zkoss.zss.ui.Spreadsheet;

import test.SpreadsheetAgentBuilder.SpreadsheetAgent;

/**
 * @author Sam
 *
 */
public class SpreadsheetAgentBuilder implements OperationAgentBuilder<ComponentAgent, SpreadsheetAgent> {

	public class SpreadsheetAgent extends AgentDelegator<ComponentAgent> implements OperationAgent {
		private Spreadsheet spreadsheet;
		
		public static final String ACTION_FORMAT_CELL = "formatCell";
		
		public SpreadsheetAgent(ComponentAgent target) {
			super(target);
			spreadsheet = (Spreadsheet)target.getDelegatee();
			syncBlock();//sent sync block command to spreadsheet (fake data)
		}
		
		private void syncBlock() {
			//Note. "fake" client data. For example, client panel height
			String desktopId = spreadsheet.getDesktop().getId();
			
			Map<String, Object> syncBlockData = new HashMap<String, Object>();
			syncBlockData.put("sheetId", spreadsheet.getUuid());
			syncBlockData.put("dpWidth", 640); //pixel value of data panel width
			syncBlockData.put("dpHeight", 380); //pixel value of data panel height
			syncBlockData.put("viewWidth", 800);
			syncBlockData.put("viewHeight", 500);
			syncBlockData.put("blockLeft", 0);
			syncBlockData.put("blockTop", 0);
			syncBlockData.put("blockRight", 9);
			syncBlockData.put("blockBottom", 19);
			syncBlockData.put("fetchLeft", -1);
			syncBlockData.put("fetchTop", -1);
			syncBlockData.put("fetchWidth", -1);
			syncBlockData.put("fetchHeight", -1);
			syncBlockData.put("rangeLeft", 0);
			syncBlockData.put("rangeTop", 0);
			syncBlockData.put("rangeRight", 9);
			syncBlockData.put("rangeBottom", 19);
			((ClientCtrl)getClient()).postUpdate(desktopId, "onZSSSyncBlock", spreadsheet.getUuid(), syncBlockData, null);
		}
		
		public Spreadsheet getSpreadsheet() {
			return spreadsheet;
		}

		public void setEditText(int row, int col, String editValue) {
			String desktopId = spreadsheet.getDesktop().getId();
			
			Map<String, Object> stopEditingData = new HashMap<String, Object>();
			stopEditingData.put("token", "0");
			stopEditingData.put("sheetId", "0");//always assume editing on the first sheet
			stopEditingData.put("row", row);
			stopEditingData.put("col", col);
			stopEditingData.put("value", editValue);
			stopEditingData.put("type", "inlineEditing");
			
			((ClientCtrl)getClient()).postUpdate(desktopId, "onStopEditing", spreadsheet.getUuid(), stopEditingData, null);
		}
		
		public void postActionCommand(int tRow, int lCol, int bRow, int rCol, String action) {
			final String desktopId = spreadsheet.getDesktop().getId();
			
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("sheetId", "0");
			args.put("tag", "toolbar");
			args.put("act", action);
			args.put("tRow", tRow);
			args.put("lCol", lCol);
			args.put("bRow", bRow);
			args.put("rCol", rCol);
			
			((ClientCtrl)getClient()).postUpdate(desktopId, "onZSSAction", spreadsheet.getUuid(), args, null);
		}
		
		public void postActionCommand(int row, int col, String action) {
			postActionCommand(row, col, row, col, action);
		}

		public void postSelectSheetCommand(String sheetId) {
			final String desktopId = spreadsheet.getDesktop().getId();
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("sheetId", sheetId);
			args.put("cache", false);
			//focus
			args.put("row", -1);
			args.put("col", -1);
			
			//selection
			args.put("left", -1);
			args.put("top", -1);
			args.put("right", -1);
			args.put("bottom", -1);
			
			//highlight
			args.put("hleft", -1);
			args.put("htop", -1);
			args.put("hright", -1);
			args.put("hbottom", -1);
			
			//freeze
			args.put("frow", -1);
			args.put("fcol", -1);
			
			((ClientCtrl)getClient()).postUpdate(desktopId, "onZSSSelectSheet", spreadsheet.getUuid(), args, null);
		}
	}

	public SpreadsheetAgent getOperation(ComponentAgent agent) {
		return new SpreadsheetAgent(agent);
	}

	public Class<SpreadsheetAgent> getOperationClass() {
		return SpreadsheetAgent.class;
	}
}
