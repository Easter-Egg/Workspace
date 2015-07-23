package editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;

public class GraphEditor extends EditorPart {
	public GraphEditor() {
	}
	public static final String ID = "FileBrowser.graphEditor";
	private Graph graph;
	private GraphConnection conn1;
	private GraphConnection conn2;
	private GraphConnection conn3;
	private GraphConnection conn4;

	static class NewGraphNode extends GraphNode {
		public NewGraphNode(Graph graph, int none, String string) {
			super(graph, none, string);
			setSize(100, 60);
		}

	}
	@Override
	public void createPartControl(Composite parent) {
		
		graph = new Graph(parent, SWT.NONE);
		//graph resize > redraw
		graph.addListener(SWT.Resize, new Listener(){

			@Override
			public void handleEvent(Event event) {
				System.out.println("graph Resizing");
				graph.setLayoutAlgorithm(new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
				
			}
			
		});
		@SuppressWarnings("unused")
		GraphNode node1 = new NewGraphNode(graph, SWT.NONE, "Node 1");
		GraphNode node2 = new NewGraphNode(graph, SWT.NONE, "Node 2");
		GraphNode node3 = new NewGraphNode(graph, SWT.NONE, "Node 3");
		GraphNode node4 = new NewGraphNode(graph, SWT.NONE, "Node 4");
		GraphNode node5 = new NewGraphNode(graph, SWT.NONE, "Node 5");
		
		conn1 = new GraphConnection(graph, ZestStyles.CONNECTIONS_SOLID, node2, node3);
		conn2 = new GraphConnection(graph, ZestStyles.CONNECTIONS_SOLID, node2, node4);
		conn3 = new GraphConnection(graph, ZestStyles.CONNECTIONS_SOLID, node3, node5);
		conn4 = new GraphConnection(graph, SWT.NONE, node4, node5);
	
		//change Connection style
		conn1.setText("redLine");
		conn2.setHighlightColor(parent.getDisplay().getSystemColor(SWT.COLOR_GREEN));
		conn3.setLineWidth(4);
		conn1.changeLineColor(parent.getDisplay().getSystemColor(SWT.COLOR_DARK_RED));
		// Manhattan Connection 
		/*FanRouter f = new FanRouter(); 
		ManhattanConnectionRouter m = new ManhattanConnectionRouter(); 
			
		conn1.getConnectionFigure().setConnectionRouter(f); 
		f.route(conn1.getConnectionFigure()); 
		f.setNextRouter(m); 
		
		conn2.getConnectionFigure().setConnectionRouter(f); 
		f.route(conn2.getConnectionFigure()); 
		f.setNextRouter(m); 

		conn3.getConnectionFigure().setConnectionRouter(f); 
		f.route(conn3.getConnectionFigure()); 
		f.setNextRouter(m); 
			
		conn4.getConnectionFigure().setConnectionRouter(f); 
		f.route(conn4.getConnectionFigure()); 
		f.setNextRouter(m); */
		
		graph.setLayoutAlgorithm(new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
		
	}

	@Override
	public void setFocus() {
	}
	
	public Graph getGraph(){
		return graph;
	}
	public GraphConnection getConn1(){
		return conn1;
	}
	public GraphConnection getConn2(){
		return conn2;
	}
	public GraphConnection getConn3(){
		return conn3;
	}
	public GraphConnection getConn4(){
		return conn4;
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		// TODO Auto-generated method stub
		setSite(site);
		setInput(input);
		setPartName("GRAPH");
	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}
}