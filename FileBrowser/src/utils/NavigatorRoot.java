package utils;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.PlatformObject;

public class NavigatorRoot extends PlatformObject{
	
	private volatile static NavigatorRoot instance;
	private ParentBean[] parentBean;
	public final static String ID = "FileBrowser.NavigatorRoot";
	
	public static NavigatorRoot getInstance() {
		if (instance == null) {
			synchronized (NavigatorRoot.class) {
				if (instance == null) {
					instance = new NavigatorRoot();
				}
			}
		}
		return instance;
	}

	public Object[] getParentBeans(){
		parentBean = new ParentBean[2];
		for(int i = 0 ; i < 2 ; i++){
			parentBean[i] = new ParentBean();
			parentBean[i].setName("Test_" + i);
		}
		return parentBean;
	}
	
	public IAdaptable getDefaultPageInput() { 
		System.out.println("Set the input");
		return getInstance();
	} 
}
