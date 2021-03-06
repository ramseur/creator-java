**[Download latest build] (https://docs.zoho.com/file/z1g2048e9c8a4c2c044d283f69773dcf86892)**
====


**Zoho Creator Java API**

* [ZCAPIDemo.java - Reference Implementation] (https://raw.github.com/zohocreator/java/master/ZCAPIDemo.java)
* [Javadoc] (http://zohocreator.github.io/java/docs/index.html)


Note: ZCAPIDemo.java and dependant Jars are included in the above build zip


**Login**

	ZOHOUser user = ZOHOCreator.getZohoUser();
	if(user == null) {
		String username = "Get Username Here";
		String password = "Get Password Here";
		user = ZOHOCreator.login(username, password);
	}
		

**Personal Applications List**

	List<ZCApplication> zcapps = ZOHOCreator.getPersonalApplicationList()); 


**List of Sections, Forms, Reports and Pages in an Application**

	List<ZCSection> zcSections = ZOHOCreator.getSectionList(ZCApplication zcApp);
	// Fetches the list of Sections in an application
	
	ZCSection firstSection = zcSections.get(0);
	List<ZCComponent> comps = firstSection.getComponents();
	// lists the Forms, Reports and Pages in the first Section
	
	ZCComponent firstComponent = comps.get(0);
	String type = firstComponent.getType();	
	// One of ZCComponent.FORM , ZCComponent.REPORT , ZCComponent.CALENDAR or ZCComponent.PAGE



**Form**

	ZCForm form = ZOHOCreator.getForm(comps.get(0));
	List<ZCField> fields = form.getFields();

	FieldType ftype = zcField.getType();
	// Check field type

	ZCField firstField = fields.get(0);
	firstField.getRecordValue().setValue("Input Value");
	// Setting Input value
	
	List<ZCButton> buttons = zcForm.getButtons();
	// Fetches all the buttons of form
	for(int i=0; i<buttons.size(); i++) {
		ZCButton button = buttons.get(i);
		if(button.getButtonType().equals(ZCButtonType.SUBMIT)) {
			ZCResponse response = button.click();
			// Submits the input value...
			Hashtable<ZCField, String> errorTable = response.getErrorMessagesTable();
			// Error, if any will be set in this Hashtable
		}
	}
	
**Deluge Events**

	boolean hasOnLoad = form.hasOnLoad(); 
	if(hasOnLoad) {
		form.onLoad();
	}
	
Client has to implement [ZCFormEvent] (http://zohocreator.github.io/java/docs/index.html?com/zoho/creator/jframework/metadata/ZCFormEvent.html) interface. 
Response Document will be set through setResponseDocument method. After the above network call, Client will have to pass the responseDocument in the below method.
	
	ZOHOCreator.callFormEvents(responseDocument, form);
	// All Deluge tasks such as Show/Hide, Enable/Disable, Set Value, Add/Append/Clear items, Alert will be called through the implemented methods
	
Similarly it has to be done for On User Input also

	boolean hasOnUserInput = field.isHasOnUserInput();
	if(hasOnUserInput) {
		form.onUserInput(field);
	}

**Report or Calendar**

	ZCView zcView = ZOHOCreator.getView(comps.get(0));
	List<ZCFilter> filters = zcView.getFilters();
	// Gets all the filters 
	List<ZCGroup> groups = zcView.getGroups();
	// Gets all the groups
	
	List<ZCRecord> records = zcView.getRecords();
	// Gets first 50 records in Report. If it's a calendar, it will fetch all the records of current month

	ZCGroup firstGroup = groups.get(0);
	List<ZCRecord> records = firstGroup.getGroupRecords();
	// Gets first 50 records in the first group
	
	List<ZCRecord> nextSetOfRecords = zcView.loadMore();				
	// Gets the next 50 records

	Calendar cal = Calendar.getInstance();
	cal.add(Calendar.MONTH, 1);
	zcView.loadCalendarRecords(cal.get(Calendar.MONTH), cal.get(Calendar.YEAR));				
	// Fetches the next month records

	zcView.deleteRecords(List<Long> recordIDs);
	zcView.duplicateRecords(List<Long> recordIDs);
	List<ZCCustomAction> customActions = zcView.getHeaderCustomActions();
	zcView.customAction(customActions.get(0).getId(), List<Long> recordIDs);




**Page**

	ZCHtmlView htmlPage = ZOHOCreator.getHtmlView(comps.get(0));
	String htmlContent = htmlPage.getHtmlContent();

**Shared Applications List**

	List<ZCApplication> sharedAppsWithMe = ZOHOCreator.getSharedApplicationList(); 



**Shared Applications with a Group**

	ZCNavList navList = ZOHOCreator.getNavigationListForApps(); 
	// Fetches the Navigation for Shared Apps with Groups and Workspace Apps.
	
	List<ZCSharedGroup> sharedWithGroupList = navList.getSharedWithGroupList(); 
	// Fetches the List of Groups in which user is present
	
	List<ZCApplication> sharedAppsWithGroup = ZOHOCreator.getSharedApplicationList(ZCSharedGroup sharedGroup); 
	// Fetches the list of Applications in that particular Group

**Workspace Applications**

	List<String> sharedWithWorkSpaceList = navList.getSharedWithWorkSpaceList(); 
	// Fetches the List of Workspaces in which user is a developer


**Javadoc of objects used above**

* [ZCApplication] (http://zohocreator.github.io/java/docs/index.html?com/zoho/creator/jframework/metadata/ZCApplication.html) 
* [ZCSection] (http://zohocreator.github.io/java/docs/index.html?com/zoho/creator/jframework/metadata/ZCSection.html) 
* [ZCComponent] (http://zohocreator.github.io/java/docs/index.html?com/zoho/creator/jframework/metadata/ZCComponent.html) 
* [ZCForm] (http://zohocreator.github.io/java/docs/index.html?com/zoho/creator/jframework/metadata/ZCForm.html) 
* [ZCField] (http://zohocreator.github.io/java/docs/index.html?com/zoho/creator/jframework/metadata/ZCField.html) 
* [FieldType] (http://zohocreator.github.io/java/docs/index.html?com/zoho/creator/jframework/metadata/FieldType.html) 
* [ZCButton] (http://zohocreator.github.io/java/docs/index.html?com/zoho/creator/jframework/metadata/ZCButton.html) 
* [ZCView] (http://zohocreator.github.io/java/docs/index.html?com/zoho/creator/jframework/metadata/ZCView.html) 
* [ZCFilter] (http://zohocreator.github.io/java/docs/index.html?com/zoho/creator/jframework/metadata/ZCFilter.html) 
* [ZCGroup] (http://zohocreator.github.io/java/docs/index.html?com/zoho/creator/jframework/metadata/ZCGroup.html) 
* [ZCRecord] (http://zohocreator.github.io/java/docs/index.html?com/zoho/creator/jframework/metadata/ZCRecord.html) 
* [ZCCustomAction] (http://zohocreator.github.io/java/docs/index.html?com/zoho/creator/jframework/metadata/ZCCustomAction.html) 
* [ZCHtmlView] (http://zohocreator.github.io/java/docs/index.html?com/zoho/creator/jframework/metadata/ZCHtmlView.html) 
* [ZCNavList] (http://zohocreator.github.io/java/docs/index.html?com/zoho/creator/jframework/metadata/ZCNavList.html) 
* [ZOHOUser] (http://zohocreator.github.io/java/docs/index.html?com/zoho/creator/jframework/metadata/ZOHOUser.html) 
		
