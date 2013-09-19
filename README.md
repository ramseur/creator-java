Zoho Creator Java API
====

Usage 
===

Login
==

	ZOHOUser user = ZOHOCreator.getZohoUser();
	if(user == null) {
		String username = "Get Username Here";
		String password = "Get Password Here";
		user = ZOHOCreator.login(username, password);
	}
		
Gettting Personal Applications List
==
		List<ZCApplication> zcapps = ZOHOCreator.getPersonalApplicationList()); 


Gettting Shared Applications List
==
		List<ZCApplication> sharedAppsWithMe = ZOHOCreator.getSharedApplicationList(); 


Gettting Shared Applications with a particular Group
==
		ZCNavList navList = ZOHOCreator.getNavigationListForApps(); // Gets the Navigation for Shared Apps with Groups and Workspace Apps.
		List<ZCSharedGroup> sharedWithGroupList = navList.getSharedWithGroupList(); // Gets the List of Groups in which user is present
    List<ZCApplication> sharedAppsWithGroup = ZOHOCreator.getSharedApplicationList(ZCSharedGroup sharedGroup); // Gets the list of Applications in that particular Group

Gettting Workspace Applications 
==
		List<String> sharedWithWorkSpaceList = navList.getSharedWithWorkSpaceList(); // Gets the List of Workspaces in which user is a developer

		
