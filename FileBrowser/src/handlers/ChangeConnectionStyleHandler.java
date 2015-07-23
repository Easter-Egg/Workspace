package handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.draw2d.ConnectionRouter;
import org.eclipse.draw2d.FanRouter;
import org.eclipse.draw2d.ManhattanConnectionRouter;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.zest.core.widgets.GraphConnection;

import editors.GraphEditor;
import utils.NullConnectionRouter;

public class ChangeConnectionStyleHandler extends AbstractHandler{
	int ConnectionState = 1;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		
		FanRouter f = new FanRouter(); 
		ManhattanConnectionRouter m = new ManhattanConnectionRouter();
		
		IEditorPart editor = (GraphEditor) PlatformUI.getWorkbench().
				getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if(ConnectionState == 1){
			System.out.println("Changed ManhattanConnectionRouter");
			GraphConnection conn1 = ((GraphEditor)editor).getConn1();
			conn1.getConnectionFigure().setConnectionRouter(f); 
			f.route(conn1.getConnectionFigure()); 
			f.setNextRouter(m);
			
			GraphConnection conn2 = ((GraphEditor)editor).getConn2();
			conn2.getConnectionFigure().setConnectionRouter(f); 
			f.route(conn2.getConnectionFigure()); 
			f.setNextRouter(m);
			
			GraphConnection conn3 = ((GraphEditor)editor).getConn3();
			conn3.getConnectionFigure().setConnectionRouter(f); 
			f.route(conn3.getConnectionFigure()); 
			f.setNextRouter(m);
			
			GraphConnection conn4 = ((GraphEditor)editor).getConn4();
			conn4.getConnectionFigure().setConnectionRouter(f); 
			f.route(conn4.getConnectionFigure()); 
			f.setNextRouter(m);
			ConnectionState += 1;
		}
		else{
			System.out.println("Changed NullConnectionRouter");
			ConnectionRouter n = new NullConnectionRouter();
			GraphConnection conn1 = ((GraphEditor)editor).getConn1();
			conn1.getConnectionFigure().setConnectionRouter(f); 
			f.route(conn1.getConnectionFigure()); 
			f.setNextRouter(n);
			
			GraphConnection conn2 = ((GraphEditor)editor).getConn2();
			conn2.getConnectionFigure().setConnectionRouter(f); 
			f.route(conn2.getConnectionFigure()); 
			f.setNextRouter(n);
			
			GraphConnection conn3 = ((GraphEditor)editor).getConn3();
			conn3.getConnectionFigure().setConnectionRouter(f); 
			f.route(conn3.getConnectionFigure()); 
			f.setNextRouter(n);
			
			GraphConnection conn4 = ((GraphEditor)editor).getConn4();
			conn4.getConnectionFigure().setConnectionRouter(f); 
			f.route(conn4.getConnectionFigure()); 
			f.setNextRouter(n);
			ConnectionState -= 1;
		}
		
		return null;
	
	}
}
	
 
