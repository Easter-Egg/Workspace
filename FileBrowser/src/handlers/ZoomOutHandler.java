package handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.zest.core.widgets.Graph;

import editors.MyGraphicalEditor;

public class ZoomOutHandler extends AbstractHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IEditorPart editor = (MyGraphicalEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		Graph graph = ((MyGraphicalEditor) editor).getGraph();
		graph.getRootLayer().setScale(graph.getRootLayer().getScale()*0.9f);
		System.out.println("Zoom Out");
		return null;
	}
}
