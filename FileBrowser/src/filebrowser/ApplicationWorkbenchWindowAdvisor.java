package filebrowser;

import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.part.EditorInputTransfer;

import utils.GraphEditorDropAdapter;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new ApplicationActionBarAdvisor(configurer);
    }
    
    public void preWindowOpen() {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setInitialSize(new Point(1280, 720)); 
        configurer.setShowCoolBar(true);
        configurer.setShowStatusLine(true);
        configurer.setTitle("File Browser");
        
        configurer.addEditorAreaTransfer(EditorInputTransfer.getInstance());
        configurer.configureEditorAreaDropListener(new GraphEditorDropAdapter(configurer.getWindow()));
    }
    
    @Override
    public void postWindowOpen() {
    	IStatusLineManager statusline = getWindowConfigurer().getActionBarConfigurer().getStatusLineManager();
    	statusline.setMessage(null, "Status line is ready");
    	
    }
}
