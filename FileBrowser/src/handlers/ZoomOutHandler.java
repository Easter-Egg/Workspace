package handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.zest.core.widgets.Graph;

import editors.GraphEditor;

public class ZoomOutHandler extends AbstractHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IEditorPart editor = (GraphEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		Graph graph = ((GraphEditor) editor).getGraph();
		ZoomManager zoomManager = new ZoomManager(graph.getRootLayer(), graph.getViewport());
		zoomManager.setZoomAsText("-100%");
		System.out.println("ZOOMOUT");
		return null;
	}
}
