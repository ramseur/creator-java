Zoho Creator Java API 
======

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
	
	List<ZCComponent> comps = zcSections.get(0).getComponents();
	// lists the Forms, Reports and Pages in the first Section
	
	String type = comps.get(0).getType();	
	// One of ZCComponent.FORM , ZCComponent.REPORT , ZCComponent.CALENDAR or ZCComponent.PAGE

**Form**

	ZCForm form = ZOHOCreator.getForm(comps.get(0));
	List<ZCField> fields = form.getFields();


**Report or Calendar**

	ZCView view = ZOHOCreator.getView(comps.get(0));

**Page**

	ZCHtmlView htmlPage = ZOHOCreator.getHtmlView(comps.get(0));

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

		
