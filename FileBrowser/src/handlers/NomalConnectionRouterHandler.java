package handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionRouter;
import org.eclipse.draw2d.FanRouter;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.zest.core.widgets.GraphConnection;

import utils.NullConnectionRouter;
import editors.GraphEditor;

public class NomalConnectionRouterHandler extends AbstractHandler{
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		FanRouter f = new FanRouter(); 
		IEditorPart editor = (GraphEditor) PlatformUI.getWorkbench().
				getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		ConnectionRouter n = new NullConnectionRouter();
		
		for(int i=0 ; i  < ((GraphEditor)editor).getGraph().getConnections().size() ; i++){
			Connection gf = ((GraphConnection) ((GraphEditor)editor).getGraph().getConnections().get(i)).getConnectionFigure();
			gf.setConnectionRouter(f);
			f.route(gf);
			f.setNextRouter(n);
		}
		
		System.out.println("Changed Nomal ConnectionRouter");
		return null;
	}

}