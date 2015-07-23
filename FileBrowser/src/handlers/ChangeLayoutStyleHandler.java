package handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.HorizontalTreeLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;

import editors.GraphEditor;

public class ChangeLayoutStyleHandler extends AbstractHandler {
	int layoutState = 1;
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		IEditorPart editor = (GraphEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		
		if(layoutState == 1){
			((GraphEditor) editor).getGraph().setLayoutAlgorithm(new HorizontalTreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
			System.out.println("Changed HorizontalTreeLayoutAlgorithm");
			layoutState += 1;
		}
		else{
			((GraphEditor) editor).getGraph().setLayoutAlgorithm(new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
			System.out.println("Changed TreeLayoutAlgorithm");
			layoutState -= 1;
		}
		
		return null;
	}
	
	public int getLayoutState(){
		return layoutState;
	}
	
}
