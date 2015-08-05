package handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.zest.core.widgets.Graph;

import editors.MyGraphicalEditor;

public class ZoomInHandler extends AbstractHandler {
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IEditorPart editor = (MyGraphicalEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		Graph graph = ((MyGraphicalEditor) editor).getGraph();
		graph.getRootLayer().setScale(graph.getRootLayer().getScale()*1.1f);
		System.out.println("Zomm In");
		return null;
	}
}
