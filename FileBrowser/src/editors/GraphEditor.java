package editors;

import java.io.File;
import java.net.URI;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.menus.IMenuService;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import utils.FileTreeLabelProvider;

@SuppressWarnings("unused")
public class GraphEditor extends EditorPart {
	public GraphEditor() {
	}
	public static final String ID = "FileBrowser.graphEditor";
	private Graph graph;
	
	private GraphConnection conn;
	private GraphConnection childConn;
	public ToolBarManager tm;

	private IWorkbenchPage page;
	
	private GraphConnection conn1;
	private GraphConnection conn2;
	private GraphConnection conn3;
	private GraphConnection conn4;
	
	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		ToolBar toolbar = new ToolBar(parent, SWT.None);
		toolbar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		ToolBarManager tm = new ToolBarManager(toolbar);
		this.tm = tm;		
		((IMenuService) getEditorSite().getService(IMenuService.class)).populateContributionManager(tm, "toolbar:FileBrowser.graphEditor");
		
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		Bundle bundle = FrameworkUtil.getBundle(FileTreeLabelProvider.class);
		URL url = FileLocator.find(bundle, new Path("icons/folder.png"), null);
		ImageDescriptor imageDcr = ImageDescriptor.createFromURL(url);
		Image folderImage = imageDcr.createImage();
		
		url = FileLocator.find(bundle, new Path("icons/File.ico"), null);
		imageDcr = ImageDescriptor.createFromURL(url);
		Image fileImage = imageDcr.createImage();
		
		FileStoreEditorInput fsInput = (FileStoreEditorInput)getEditorInput();
		URI uri = fsInput.getURI();
		File file = new File(uri);
		
		graph = new Graph(parent, SWT.NONE);
		graph.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		graph.addListener(SWT.Resize, new Listener(){
			@Override
			public void handleEvent(Event event) {
				graph.setLayoutAlgorithm(new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
			}
		});
		
		GraphNode root = new GraphNode(graph, SWT.NONE, file.getName(), folderImage);
		
		if(file.listFiles().length != 0){
			for(File childFile : file.listFiles()){
				if(childFile.isDirectory()){
					// 폴더 노드 생성
					GraphNode childNode = new GraphNode(graph, SWT.NONE, childFile.getName(), folderImage);
					conn = new GraphConnection(graph, ZestStyles.CONNECTIONS_SOLID, root, childNode);
					if(childFile.listFiles() != null){
						for(File grandChildFile : childFile.listFiles()){
							if(grandChildFile.isDirectory()){
								// 폴더 노드 생성
								GraphNode grandChildNode = new GraphNode(graph, SWT.NONE, grandChildFile.getName(), folderImage);
								childConn = new GraphConnection(graph, ZestStyles.CONNECTIONS_SOLID, childNode, grandChildNode);
							}
							else {
								//파일 노드 생성
								GraphNode grandChildNode = new GraphNode(graph, SWT.NONE, grandChildFile.getName(), fileImage);
								childConn = new GraphConnection(graph, ZestStyles.CONNECTIONS_SOLID, childNode, grandChildNode);
							}
						}
					}
				}
				
				else {
					// 파일 노드 생성
					GraphNode childNode = new GraphNode(graph, SWT.NONE, childFile.getName(), fileImage);
					conn = new GraphConnection(graph, ZestStyles.CONNECTIONS_SOLID, root, childNode);
				}
			}		
		}
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