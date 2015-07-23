package editors;

import java.io.File;
import java.net.URI;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;

@SuppressWarnings("unused")
public class GraphEditor extends EditorPart {
	public GraphEditor() {
	}
	public static final String ID = "FileBrowser.graphEditor";
	private Graph graph;
	
	private GraphConnection conn;
	private GraphConnection childConn;
	private GraphConnection conn1;
	private GraphConnection conn2;
	private GraphConnection conn3;
	private GraphConnection conn4;
	
	private SelectionListener selectionListener = new SelectionListener(){

		@Override
		public void widgetSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
			System.out.println(e.item);
			//((GraphNode) e.item).
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	};

	static class NewGraphNode extends GraphNode {
		public NewGraphNode(Graph graph, int none, String string) {
			super(graph, none, string);
			setSize(100, 60);
		}
	}
	
	@Override
	public void createPartControl(Composite parent) {		
		
		FileStoreEditorInput fsInput = (FileStoreEditorInput)getEditorInput();
		URI uri = fsInput.getURI();
		File file = new File(uri);
		
		graph = new Graph(parent, SWT.NONE);
		//graph resize > redraw
		graph.addListener(SWT.Resize, new Listener(){

			@Override
			public void handleEvent(Event event) {
				graph.setLayoutAlgorithm(new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
				
			}
			
		});
		
		GraphNode root = new NewGraphNode(graph, SWT.NONE, file.getName());
		
		for(File childFile : file.listFiles()){
			GraphNode childNode = new NewGraphNode(graph, SWT.NONE, childFile.getName());
			conn = new GraphConnection(graph, ZestStyles.CONNECTIONS_SOLID, root, childNode);
			if(childFile.isDirectory()){
				for(File grandChildFile : childFile.listFiles()){
					GraphNode grandChildeNode = new NewGraphNode(graph, SWT.NONE, grandChildFile.getName());
					childConn = new GraphConnection(graph, ZestStyles.CONNECTIONS_SOLID, childNode, grandChildeNode);
				}
			}
		}		
		graph.setLayoutAlgorithm(new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
		graph.addSelectionListener(selectionListener);
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
		setPartName(input.getName());
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