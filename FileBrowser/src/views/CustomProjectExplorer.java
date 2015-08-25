package views;

import org.eclipse.ui.navigator.CommonNavigator;

import utils.NavigatorRoot;

public class CustomProjectExplorer extends CommonNavigator {
	public static final String ID = "FileBrowser.CustomProjectExplorer";
	@Override
	public Object getInitialInput(){
		System.out.println("Set the root");
		return NavigatorRoot.getInstance();
	}
	
}
