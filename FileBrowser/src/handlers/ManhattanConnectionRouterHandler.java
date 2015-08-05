package handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.FanRouter;
import org.eclipse.draw2d.ManhattanConnectionRouter;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.zest.core.widgets.GraphConnection;

import editors.MyGraphicalEditor;

public class ManhattanConnectionRouterHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		FanRouter f = new FanRouter();
		IEditorPart editor = (MyGraphicalEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		ManhattanConnectionRouter m = new ManhattanConnectionRouter();

		for (int i = 0; i < ((MyGraphicalEditor) editor).getGraph().getConnections().size(); i++) {
			Connection gf = ((GraphConnection) ((MyGraphicalEditor) editor).getGraph().getConnections().get(i)).getConnectionFigure();
			gf.setConnectionRouter(f);
			f.route(gf);
			f.setNextRouter(m);
		}
		System.out.println("Changed ManhattanConnectionRouter");
		return null;
	}

}
