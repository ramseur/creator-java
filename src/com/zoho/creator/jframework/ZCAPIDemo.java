// $Id$
package com.zoho.creator.jframework;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import com.zoho.creator.jframework.FieldType;
import com.zoho.creator.jframework.ZCApplication;
import com.zoho.creator.jframework.ZCButton;
import com.zoho.creator.jframework.ZCButtonType;
import com.zoho.creator.jframework.ZCComponent;
import com.zoho.creator.jframework.ZCCustomAction;
import com.zoho.creator.jframework.ZCException;
import com.zoho.creator.jframework.ZCField;
import com.zoho.creator.jframework.ZCFilter;
import com.zoho.creator.jframework.ZCForm;
import com.zoho.creator.jframework.ZCGroup;
import com.zoho.creator.jframework.ZCHtmlView;
import com.zoho.creator.jframework.ZCNavList;
import com.zoho.creator.jframework.ZCRecord;
import com.zoho.creator.jframework.ZCResponse;
import com.zoho.creator.jframework.ZCSection;
import com.zoho.creator.jframework.ZCSharedGroup;
import com.zoho.creator.jframework.ZCView;
import com.zoho.creator.jframework.ZOHOCreator;
import com.zoho.creator.jframework.ZOHOUser;

public class ZCAPIDemo {
	private static Calendar cal = Calendar.getInstance();
	
	public static String getInput(String question) {
		return getInput(question, false);
	}

	public static String getInput(String question, boolean mask) {
	  printInfo(question + "  ");
	  if(mask) {
		  char[] passwordChars = System.console().readPassword();
		  return  new String(passwordChars);
	  } else {
		  return System.console().readLine();
	  }
	}
	
	public static void printAppList() throws ZCException {
		List<ZCApplication> zcapps = ZOHOCreator.getCurrentAppList().getApps();
		for(int i=0; i<zcapps.size(); i++) {
			printInfo((i+1) + ". " + zcapps.get(i).getAppName());
		}
		String inp = getInput("Type the serial no of the app..... Type -1 to quit..... Type 0 to go to Home Page Listing");//No I18N
		int selectedApp = 0;
		try {
			selectedApp = Integer.parseInt(inp);
		} catch(Exception ee) {}

		if(selectedApp == -1) {
			return;
		}  else if(selectedApp == 0) {
			printNavList();
		} else if(selectedApp > zcapps.size()) {
			printAppList();
		} else {
			ZOHOCreator.setCurrentApplication(zcapps.get(selectedApp -1));
			printCompsList();
		}
	}
	
	public static void printNavList() throws ZCException {
		ZCNavList navList = ZOHOCreator.getCurrentNavigationListForApps();
		String inp = getInput("\n\nType the serial no..... \n1 for Personal Applications\n2 for Shared Apps with me\n3 for shared apps with groups\n4 for Workspace apps\n5 for Support Form\n-1 to quit");//No I18N
		int inpValue = -1;
		try {
			inpValue = Integer.parseInt(inp);
		} catch(Exception ee) {}

		if(inpValue == -1) {
			return;
		} else if(inpValue == 1) {
			ZOHOCreator.setCurrentAppList(ZOHOCreator.getPersonalApplicationList());
			printAppList();
		} else if(inpValue == 2) {
			ZOHOCreator.setCurrentAppList(ZOHOCreator.getSharedApplicationList());
			printAppList();
		}  else if(inpValue == 3) {
			List<ZCSharedGroup> sharedWithGroupList = navList.getSharedWithGroupList();
			for(int i=0; i<sharedWithGroupList.size(); i++) {
				ZCSharedGroup sharedWithGroup = sharedWithGroupList.get(i);
				printInfo((i+1) + ") " + sharedWithGroup.getGroupName());
			}
			inp = getInput("Type the serial no of group.....  Type -1 to quit...... Type 0 to go back... \n");//No I18N
			try {
				inpValue = Integer.parseInt(inp);
			} catch(Exception ee) {}
			if(inpValue == -1) {
				return;
			} else if(inpValue == 0) {
				printNavList();
			} else {
				ZOHOCreator.setCurrentAppList(ZOHOCreator.getSharedApplicationList(sharedWithGroupList.get(inpValue-1)));
				printAppList();
			}
		}  else if(inpValue == 4) {
			List<String> sharedWithWorkSpaceList = navList.getSharedWithWorkSpaceList();
			for(int i=0; i<sharedWithWorkSpaceList.size(); i++) {
				String workSpaceName = sharedWithWorkSpaceList.get(i);
				printInfo((i+1) + ") " + workSpaceName);
			}
			inp = getInput("Type the serial no of the workspace.....  Type -1 to quit...... Type 0 to go back... \n");//No I18N
			try {
				inpValue = Integer.parseInt(inp);
			} catch(Exception ee) {}
			if(inpValue == -1) {
				return;
			} else if(inpValue == 0) {
				printNavList();
			} else {
				ZOHOCreator.setCurrentAppList(ZOHOCreator.getWorkspaceApplicationList(sharedWithWorkSpaceList.get(inpValue-1)));
				printAppList();
			}
		} else if(inpValue == 5) {
			ZOHOCreator.setCurrentForm(ZOHOCreator.getForm("support", "Submit_a_Support_Request", "zoho1"));//No I18N
			printForm();
		}
	}
	
	public static void printCompsList() throws ZCException  {
		ZOHOCreator.loadSelectedApplication();
		List<ZCSection> zcSections = ZOHOCreator.getCurrentSectionList();
		
		int compsCounter = 1;
		List<ZCComponent> comps = new ArrayList<ZCComponent>();
		for(int i=0; i<zcSections.size(); i++) {
			ZCSection zcSection = zcSections.get(i);
			printInfo(zcSection.getSectionName());
			List<ZCComponent> zcComps = zcSection.getComponents();
			for(int j=0; j<zcComps.size(); j++) {
				ZCComponent comp = zcComps.get(j);
				comps.add(comp);
				printInfo("\t" + (compsCounter++) + ". " + comp.getComponentName() + "(" + comp.getType() + ")"); //No I18N
			}
		}

		String inp = getInput("\n\nType the serial no of Form/View..... Type -1 to quit..... Type 0 to go back to App Listing");//No I18N
		int selectedComp = -1000;
		try {
			selectedComp = Integer.parseInt(inp);
		} catch(Exception ee) {}

		if(selectedComp == 0) {
			printAppList();
		} else if(selectedComp == -1000 || selectedComp > compsCounter) {
			printCompsList();
		} else if(selectedComp == -1) {
			return;
		} else {
			ZCComponent selComp = comps.get(selectedComp-1);
			ZOHOCreator.setCurrentComponent(selComp);
			//printInfo("Selected COmp : " + selComp);
			if(selComp.getType().equals(ZCComponent.FORM)) {
				loadForm();
			} else if(selComp.getType().equals(ZCComponent.REPORT)) {
				printView();
			} else if(selComp.getType().equals(ZCComponent.CALENDAR)) {
				printCalendar();
			} else if(selComp.getType().equals(ZCComponent.PAGE)) {
				printHtmlView();
			}
		}
	}
	
	private static void printHtmlView() throws ZCException {
		// TODO Auto-generated method stub
		ZOHOCreator.loadSelectedHtmlView();
		ZCHtmlView htmlView = ZOHOCreator.getCurrentHtmlView();
		printInfo(htmlView.getHtmlContent());
	}

	public static void loadForm() throws ZCException {
		ZOHOCreator.loadSelectedForm();
		printForm();
	}
	
	public static void printForm() throws ZCException {
		ZCForm zcForm = ZOHOCreator.getCurrentForm();
		List<ZCField> fields = zcForm.getFields();
		for(int i=0; i<fields.size(); i++) {
			ZCField zcField = fields.get(i);
			String toAsk = "\n\n" + (i+1) + ") " + zcField.getDisplayName();//No I18N
			if(FieldType.isChoiceField(zcField.getType())) {
				toAsk = toAsk + zcField.getChoices();
			} else if(zcField.getType().equals(FieldType.DATE) || zcField.getType().equals(FieldType.DATE_TIME)) {
				toAsk = toAsk + "[dd-MMM-YYYY] For ex, 29-Aug-2013";//No I18N
			} else if(FieldType.isNumberField(zcField.getType())) {
				toAsk = toAsk + "[" + zcField.getType() + "]";//No I18N
			}
			
			if(FieldType.isMultiChoiceField(zcField.getType())) {
				toAsk = toAsk + "\n" + zcField.getRecordValue().getValues();//No I18N
	//		} else if(FieldType.isPhotoField(zcField.getType())) {
				
			} else {
				toAsk = toAsk + "\n" + zcField.getRecordValue().getValue();//No I18N
			}
				
			String val = getInput(toAsk);
			if(FieldType.isPhotoField(zcField.getType())) {
				zcField.getRecordValue().setFileValue(new File(val));
			} else if(!val.trim().equals("")) {//No I18N
				zcField.getRecordValue().setValue(val);
			}
		}
		List<ZCButton> buttons = zcForm.getButtons();
		for(int i=0; i<buttons.size(); i++) {
			ZCButton button = buttons.get(i);
			//printInfo("Button: " + button);
			if(button.getButtonType().equals(ZCButtonType.SUBMIT)) {
				ZCResponse response = button.click();
				Hashtable<ZCField, String> errorTable = response.getErrorMessagesTable();
				if(errorTable.keySet().size()>0) {
					printInfo("\n************** Errors found **************");//No I18N
					printInfo(errorTable.toString());
					loadForm();
				} else {
					printInfo("\n************** " + zcForm.getSuccessMessage() + " **************");//No I18N
				}
			}
		}
		String inp = getInput("\nType 1 to Add another entry.... -1 to quit.... 0 to go back to " + ZOHOCreator.getCurrentApplication().getAppName());//No I18N
		int inpValue = -1;
		try {
			inpValue = Integer.parseInt(inp);
		} catch(Exception ee) {}
		if(inpValue == 1) {
			loadForm();
		} else if(inpValue == 0) {
			printCompsList();
			return;
		} else if(inpValue == -1) {
			return;
		}
	}
	
	public static void printView() throws ZCException {
		ZOHOCreator.loadSelectedView();
		ZCView zcView = ZOHOCreator.getCurrentView();
		printRecords(zcView);
	}
	
	static void printCalendar() throws ZCException {
		ZOHOCreator.loadSelectedView();
		ZCView zcView = ZOHOCreator.getCurrentView();
		//HashMap<Date, List<ZCRecord>> eventsMap = zcView.getEventRecordsMap();
		printRecords(zcView);
	}
	
	private static void printRecords(List<ZCRecord> records) {
		for(int i=0; i<records.size(); i++) {
			ZCRecord record = records.get(i);
			/*
			if(zcView.isGrouped() && record.getGroupList().size()>0) {
				printInfo("************ " + record.getGroupList() + " ************" );
			}
			*/
			printInfo(record.getRecordId() + ": " + record.getPrimaryDisplay());//No I18N
			printInfo(record.getSecondaryDisplay() + "\n");//No I18N
		}
		
	}
	
	private static void printRecords(ZCView zcView) throws ZCException {
		List<ZCFilter> filters = zcView.getFilters();
		//printInfo(filters);
		
		if(zcView.isGrouped()) {
			List<ZCGroup> groups = zcView.getGroups();
			for(int i=0; i<groups.size(); i++) {
				ZCGroup group = groups.get(i);
				printInfo("************** " + group.getGroupHeaderValues() + " **************");//No I18N
				List<ZCRecord> records = group.getGroupRecords();
				printRecords(records);
			}
		} else {
			List<ZCRecord> records = zcView.getRecords();
			printRecords(records);
		}
		
		if(zcView.getType().equals(ZCComponent.CALENDAR)) {
			String inp = getInput("\n\nType 1 to load Next Month.... 2 for Previous month.... -1 to quit.... 0 to go back to " + ZOHOCreator.getCurrentApplication().getAppName());//No I18N
			int inpValue = -1;
			try {
				inpValue = Integer.parseInt(inp);
			} catch(Exception ee) {}
			if(inpValue == 1) {
				cal.add(Calendar.MONTH, 1);
			} else if(inpValue == 2) {
				cal.add(Calendar.MONTH, -1);
			} else if(inpValue == 0) {
				printCompsList();
				return;
			} else if(inpValue == -1) {
				return;
			}
			zcView.loadCalendarRecords(cal.get(Calendar.MONTH), cal.get(Calendar.YEAR));				
			printRecords(zcView);
		} else {
			//printInfo("isLastReached: " + zcView.isLastReached());
			if(!zcView.isLastReached()) {
				String inp = getInput("\n\nType 1 to load more.... 2 for actions.... -1 to quit.... 0 to go back to " + ZOHOCreator.getCurrentApplication().getAppName());//No I18N
				int inpValue = -1;
				try {
					inpValue = Integer.parseInt(inp);
				} catch(Exception ee) {}
				if(inpValue == 1) {
					zcView.loadMore();				
					printRecords(zcView);
				} else if(inpValue == 2) {
					printActions(zcView);
//				} else if(inpValue == 3) {
//					printRecord(zcView);
				} else if(inpValue == 0) {
					printCompsList();
					return;
				} else if(inpValue == -1) {
					return;
				}
			}
		}
	}
	
	private static void printInfo(String str) {
		//System.out.println(str);
	}
	
	private static void printActions(ZCView zcView) throws ZCException {
		printInfo("\n\nType one of these view actions"); //No I18N
		if(zcView.isAddAllowed()) {
			printInfo("Add");//No I18N
		}
		if(zcView.isBulkEditAllowed()) {
			printInfo("Edit");//No I18N
		}
		if(zcView.isDeleteAllowed()) {
			printInfo("Delete");//No I18N
		}
		if(zcView.isDuplicateAllowed()) {
			printInfo("Duplicate");//No I18N
		}
		List<ZCCustomAction> customActions = zcView.getHeaderCustomActions();
		for(int i=0; i<customActions.size(); i++) {
			ZCCustomAction zcCustomAction = customActions.get(i);
			printInfo(zcCustomAction.getName());
		}
		String inp = getInput("");
//		if(inp.equals("Add")) {
			
//		} else if(inp.equals("Edit")) {
			
//		} else 
		
		if(inp.equals("Delete")) {
			zcView.deleteRecords(getRecordIdsInput());
			printView();
		} else if(inp.equals("Duplicate")) {
			zcView.duplicateRecords(getRecordIdsInput());
		} else {
			for(int i=0; i<customActions.size(); i++) {
				ZCCustomAction zcCustomAction = customActions.get(i);
				if(inp.equals(zcCustomAction.getName())) {
					Long custId = zcCustomAction.getId();
					zcView.customAction(custId, getRecordIdsInput());
				}
			}
		} 
	}
	
	private static List<Long> getRecordIdsInput() {
		String recsInp = getInput("Enter Record IDs separated by comma");//No I18N
		String[] recs = recsInp.split("\\s*,\\s*");//No I18N
		List<Long> recIds = new ArrayList<Long>();
		for(int i=0; i<recs.length; i++) {
			try {
				recIds.add(Long.parseLong(recs[i]));
			} catch(Exception ee) {}
		}
		return recIds;
		
	}
	
	public static void main(String[] args) {

        Properties props = new Properties();
        try {
        	InputStream zcInpStream = new FileInputStream("zc.txt");//No I18N
        	props.load(zcInpStream);
        	Set<?> keys = props.keySet(); 
        	Iterator<?> keysIterator = keys.iterator();
        	while(keysIterator.hasNext()) {
        		String key = (String) keysIterator.next();
        		String value = props.getProperty(key);
        		if(key.equals("AccountsURL")) {//No I18N
        			ZOHOCreator.setAccountsURL(value);
        		} else if(key.equals("CreatorURL")) {//No I18N
        			ZOHOCreator.setCreatorURL(value);
        		} else if(key.equals("ServiceName")) {//No I18N
        			ZOHOCreator.setServiceName(value);
        		}
        	}
        } catch (IOException e) {
        	// TODO Auto-generated catch block
        	e.printStackTrace();
        }
		ZOHOUser user = null;//ZOHOCreator.getZohoUser();
		
		
		//printInfo(user.getAuthToken());
		//printInfo("Fetching Application List....");
		
		
		try {
			if(user == null) {
				String username = getInput("Enter Username");//No I18N
				String password = getInput("Enter password", true);//No I18N
				user = ZOHOCreator.login(username, password);
			} 

			ZOHOCreator.setCurrentNavigationListForApps(ZOHOCreator.getNavigationListForApps());
		ZOHOCreator.setCurrentAppList(ZOHOCreator.getPersonalApplicationList());
		printAppList();

		

		
		
		
		String inp = getInput("Do you want to logut? Type 1 to quit without logout..... 2 to logout and quit");//No I18N
		int inpValue = -1;
		try {
			inpValue = Integer.parseInt(inp);
		} catch(Exception ee) {}

		if(inpValue == 2) {
			ZOHOCreator.logout();
			printInfo("************** Successfully logged out... **************");//No I18N

		}

		} catch(ZCException exp) {
			exp.printStackTrace();
		}

		
		//printInfo("Fetching Section List....");
		//List<ZCSection> zcSections = ZOHOCreator.getSectionList(zcapps.get(2));
		////printInfo("Fetching Form....");
		//ZCForm zcForm = ZOHOCreator.getForm(zcSections.get(25).getComponents().get(0));
		
		//printInfo("Fetching View....");
		//ZCView zcView = ZOHOCreator.getView(zcSections.get(22).getComponents().get(1));
		

		//printInfo("Fetching View....");
		//ZCView zcView = ZOHOCreator.getView("all-fields", "All_fields_View", "charles");
		//printInfo(zcView);
		
		//printInfo("\n\n******************************\n\n");
		//printInfo(zcSections);
		//printInfo("\n\n******************************\n\n");
		////printInfo(zcForm);
		//printInfo("\n\n******************************\n\n");
		
		
		
	}
}
