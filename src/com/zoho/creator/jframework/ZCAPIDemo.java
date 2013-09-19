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

import com.zoho.creator.jframework.metadata.FieldType;
import com.zoho.creator.jframework.metadata.ZCApplication;
import com.zoho.creator.jframework.metadata.ZCButton;
import com.zoho.creator.jframework.metadata.ZCButtonType;
import com.zoho.creator.jframework.metadata.ZCComponent;
import com.zoho.creator.jframework.metadata.ZCCustomAction;
import com.zoho.creator.jframework.metadata.ZCField;
import com.zoho.creator.jframework.metadata.ZCFilter;
import com.zoho.creator.jframework.metadata.ZCForm;
import com.zoho.creator.jframework.metadata.ZCGroup;
import com.zoho.creator.jframework.metadata.ZCHtmlView;
import com.zoho.creator.jframework.metadata.ZCNavList;
import com.zoho.creator.jframework.metadata.ZCRecord;
import com.zoho.creator.jframework.metadata.ZCResponse;
import com.zoho.creator.jframework.metadata.ZCSection;
import com.zoho.creator.jframework.metadata.ZCSharedGroup;
import com.zoho.creator.jframework.metadata.ZCURL;
import com.zoho.creator.jframework.metadata.ZCView;
import com.zoho.creator.jframework.metadata.ZOHOCreator;
import com.zoho.creator.jframework.metadata.ZOHOUser;

public class ZCAPIDemo {
	private static Calendar cal = Calendar.getInstance();
	
	public static String getInput(String question) {
		return getInput(question, false);
	}

	public static String getInput(String question, boolean mask) {
	  System.out.println(question + "  ");
	  if(mask) {
		  char[] passwordChars = System.console().readPassword();
		  return  new String(passwordChars);
	  } else {
		  return System.console().readLine();
	  }
	}
	
	public static void printAppList() {
		List<ZCApplication> zcapps = ZOHOCreator.getCurrentAppList().getApps();
		for(int i=0; i<zcapps.size(); i++) {
			System.out.println((i+1) + ". " + zcapps.get(i).getAppName());
		}
		String inp = getInput("Type the serial no of the app..... Type -1 to quit..... Type 0 to go to Home Page Listing");
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
	
	public static void printNavList() {
		ZCNavList navList = ZOHOCreator.getCurrentNavigationListForApps();
		String inp = getInput("\n\nType the serial no..... \n1 for Personal Applications\n2 for Shared Apps with me\n3 for shared apps with groups\n4 for Workspace apps\n-1 to quit");
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
				System.out.println((i+1) + ") " + sharedWithGroup.getGroupName());
			}
			inp = getInput("Type the serial no of group.....  Type -1 to quit...... Type 0 to go back... \n");
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
				System.out.println((i+1) + ") " + workSpaceName);
			}
			inp = getInput("Type the serial no of the workspace.....  Type -1 to quit...... Type 0 to go back... \n");
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
		}
	}
	
	public static void printCompsList() {
		ZOHOCreator.loadSelectedApplication();
		List<ZCSection> zcSections = ZOHOCreator.getCurrentSectionList();
		
		int compsCounter = 1;
		List<ZCComponent> comps = new ArrayList<ZCComponent>();
		for(int i=0; i<zcSections.size(); i++) {
			ZCSection zcSection = zcSections.get(i);
			System.out.println(zcSection.getSectionName());
			List<ZCComponent> zcComps = zcSection.getComponents();
			for(int j=0; j<zcComps.size(); j++) {
				ZCComponent comp = zcComps.get(j);
				comps.add(comp);
				System.out.println("\t" + (compsCounter++) + ". " + comp.getComponentName() + "(" + comp.getType() + ")");
			}
		}

		String inp = getInput("\n\nType the serial no of Form/View..... Type -1 to quit..... Type 0 to go back to App Listing");
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
			//System.out.println("Selected COmp : " + selComp);
			if(selComp.getType().equals(ZCComponent.FORM)) {
				printForm();
			} else if(selComp.getType().equals(ZCComponent.VIEW)) {
			} else if(selComp.getType().equals(ZCComponent.REPORT)) {
				printView();
			} else if(selComp.getType().equals(ZCComponent.CALENDAR)) {
				printCalendar();
			} else if(selComp.getType().equals(ZCComponent.HTML_VIEW)) {
			} else if(selComp.getType().equals(ZCComponent.PAGE)) {
				printHtmlView();
			}
		}
	}
	
	private static void printHtmlView() {
		// TODO Auto-generated method stub
		ZOHOCreator.loadSelectedHtmlView();
		ZCHtmlView htmlView = ZOHOCreator.getCurrentHtmlView();
		System.out.println(htmlView.getHtmlContent());
	}

	public static void printForm() {
		ZOHOCreator.loadSelectedForm();
		ZCForm zcForm = ZOHOCreator.getCurrentForm();
		List<ZCField> fields = zcForm.getFields();
		for(int i=0; i<fields.size(); i++) {
			ZCField zcField = fields.get(i);
			String toAsk = "\n\n" + (i+1) + ") " + zcField.getDisplayName();
			if(FieldType.isChoiceField(zcField.getType())) {
				toAsk = toAsk + zcField.getChoices();
			} else if(zcField.getType().equals(FieldType.DATE) || zcField.getType().equals(FieldType.DATE_TIME)) {
				toAsk = toAsk + "[dd-MMM-YYYY] For ex, 29-Aug-2013";
			} else if(FieldType.isNumberField(zcField.getType())) {
				toAsk = toAsk + "[" + zcField.getType() + "]";
			}
			
			if(FieldType.isMultiChoiceField(zcField.getType())) {
				toAsk = toAsk + "\n" + zcField.getRecordValue().getValues();
			} else if(FieldType.isPhotoField(zcField.getType())) {
				
			} else {
				toAsk = toAsk + "\n" + zcField.getRecordValue().getValue();
			}
				
			String val = getInput(toAsk);
			if(FieldType.isPhotoField(zcField.getType())) {
				zcField.getRecordValue().setFileValue(new File(val));
			} else if(!val.trim().equals("")) {
				zcField.getRecordValue().setValue(val);
			}
		}
		List<ZCButton> buttons = zcForm.getButtons();
		for(int i=0; i<buttons.size(); i++) {
			ZCButton button = buttons.get(i);
			//System.out.println("Button: " + button);
			if(button.getButtonType().equals(ZCButtonType.SUBMIT)) {
				ZCResponse response = button.click();
				Hashtable<ZCField, String> errorTable = response.getErrorMessagesTable();
				if(errorTable.keySet().size()>0) {
					System.out.println("\n************** Errors found **************");
					System.out.println(errorTable);
					printForm();
				} else {
					System.out.println("\n************** " + zcForm.getSuccessMessage() + " **************");
				}
			}
		}
		String inp = getInput("\nType 1 to Add another entry.... -1 to quit.... 0 to go back to " + ZOHOCreator.getCurrentApplication().getAppName());
		int inpValue = -1;
		try {
			inpValue = Integer.parseInt(inp);
		} catch(Exception ee) {}
		if(inpValue == 1) {
			printForm();
		} else if(inpValue == 0) {
			printCompsList();
			return;
		} else if(inpValue == -1) {
			return;
		}
	}
	
	public static void printView() {
		ZOHOCreator.loadSelectedView();
		ZCView zcView = ZOHOCreator.getCurrentView();
		printRecords(zcView);
	}
	
	static void printCalendar() {
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
				System.out.println("************ " + record.getGroupList() + " ************" );
			}
			*/
			System.out.println(record.getRecordId() + ": " + record.getPrimaryDisplay());
			System.out.println(record.getSecondaryDisplay() + "\n");
		}
		
	}
	
	private static void printRecords(ZCView zcView) {
		List<ZCFilter> filters = zcView.getFilters();
		//System.out.println(filters);
		
		if(zcView.isGrouped()) {
			List<ZCGroup> groups = zcView.getGroups();
			for(int i=0; i<groups.size(); i++) {
				ZCGroup group = groups.get(i);
				System.out.println("************** " + group.getGroupHeaderValues() + " **************");
				List<ZCRecord> records = group.getGroupRecords();
				printRecords(records);
			}
		} else {
			List<ZCRecord> records = zcView.getRecords();
			printRecords(records);
		}
		
		if(zcView.getType().equals(ZCComponent.CALENDAR)) {
			String inp = getInput("\n\nType 1 to load Next Month.... 2 for Previous month.... -1 to quit.... 0 to go back to " + ZOHOCreator.getCurrentApplication().getAppName());
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
			//System.out.println("isLastReached: " + zcView.isLastReached());
			if(!zcView.isLastReached()) {
				String inp = getInput("\n\nType 1 to load more.... 2 for actions.... -1 to quit.... 0 to go back to " + ZOHOCreator.getCurrentApplication().getAppName());
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
	
	
	private static void printActions(ZCView zcView) {
		System.out.println("\n\nType one of these view actions");
		if(zcView.isAddAllowed()) {
			System.out.println("Add");
		}
		if(zcView.isBulkEditAllowed()) {
			System.out.println("Edit");
		}
		if(zcView.isDeleteAllowed()) {
			System.out.println("Delete");
		}
		if(zcView.isDuplicateAllowed()) {
			System.out.println("Duplicate");
		}
		List<ZCCustomAction> customActions = zcView.getHeaderCustomActions();
		for(int i=0; i<customActions.size(); i++) {
			ZCCustomAction zcCustomAction = customActions.get(i);
			System.out.println(zcCustomAction.getName());
		}
		String inp = getInput("");
		if(inp.equals("Add")) {
			
		} else if(inp.equals("Edit")) {
			
		} else if(inp.equals("Delete")) {
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
		String recsInp = getInput("Enter Record IDs separated by comma");
		String[] recs = recsInp.split("\\s*,\\s*");
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
        	InputStream zcInpStream = new FileInputStream("zc.txt");
        	props.load(zcInpStream);
        	Set<?> keys = props.keySet(); 
        	Iterator<?> keysIterator = keys.iterator();
        	while(keysIterator.hasNext()) {
        		String key = (String) keysIterator.next();
        		String value = props.getProperty(key);
        		if(key.equals("AccountsURL")) {
        			ZOHOCreator.setAccountsURL(value);
        		} else if(key.equals("CreatorURL")) {
        			ZOHOCreator.setCreatorURL(value);
        		} else if(key.equals("ServiceName")) {
        			ZOHOCreator.setServiceName(value);
        		}
        	}
        } catch (IOException e) {
        	// TODO Auto-generated catch block
        	e.printStackTrace();
        }
		ZOHOUser user = ZOHOCreator.getZohoUser();
		
		if(user == null) {
			String username = getInput("Enter Username");//No I18N
			String password = getInput("Enter password", true);//No I18N
			user = ZOHOCreator.login(username, password);
		} 
		
		//System.out.println(user.getAuthToken());
		//System.out.println("Fetching Application List....");
		
		
		
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
			System.out.println("************** Successfully logged out... **************");

		}

		
		
		//System.out.println("Fetching Section List....");
		//List<ZCSection> zcSections = ZOHOCreator.getSectionList(zcapps.get(2));
		////System.out.println("Fetching Form....");
		//ZCForm zcForm = ZOHOCreator.getForm(zcSections.get(25).getComponents().get(0));
		
		//System.out.println("Fetching View....");
		//ZCView zcView = ZOHOCreator.getView(zcSections.get(22).getComponents().get(1));
		

		//System.out.println("Fetching View....");
		//ZCView zcView = ZOHOCreator.getView("all-fields", "All_fields_View", "charles");
		//System.out.println(zcView);
		
		//System.out.println("\n\n******************************\n\n");
		//System.out.println(zcSections);
		//System.out.println("\n\n******************************\n\n");
		////System.out.println(zcForm);
		//System.out.println("\n\n******************************\n\n");
		
		
		
	}
}
