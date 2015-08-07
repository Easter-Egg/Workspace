package editors;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.parts.GraphicalEditorWithPalette;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseWheelListener;
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
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.menus.IMenuService;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.GridLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.HorizontalShift;
import org.eclipse.zest.layouts.algorithms.HorizontalTreeLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import utils.FileModel;
import utils.FileTreeLabelProvider;
import utils.PaletteViewCreator;
import views.TestOutlineView;

@SuppressWarnings("unused")
public class MyGraphicalEditor extends GraphicalEditorWithPalette{
	
	private static PaletteRoot PALETTE_MODEL;
	public final static String ID = "FileBrowser.myGraphicalEditor";
	
	private FileStoreEditorInput fsInput;
	
	private Graph graph;
	private GraphConnection conn;
	private GraphConnection childConn;
	
	private ToolBarManager tm;
	private IWorkbenchPage page;
	private TestOutlineView olv;
	private GraphNode root;
	
	private List<FileModel> fileModelList;	

	public MyGraphicalEditor() {
		setEditDomain(new DefaultEditDomain(this));
	}
	
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
	}
	
	public void commandStackChanged(EventObject event) {
		firePropertyChange(IEditorPart.PROP_DIRTY);
		super.commandStackChanged(event);
	}

	@Override
	protected void initializeGraphicalViewer() {
		GraphicalViewer viewer = getGraphicalViewer();
		Composite parent = ((Composite) viewer.getControl());
		
		parent.setLayout(new GridLayout(1, false));
		ToolBar toolbar = new ToolBar(parent, SWT.None);
		toolbar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		ToolBarManager tm = new ToolBarManager(toolbar);
		this.tm = tm;		
		((IMenuService) getEditorSite().getService(IMenuService.class)).populateContributionManager(tm, "toolbar:FileBrowser.MyGraphicalEditor");
		
		page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		olv = (TestOutlineView) page.findView("FileBrowser.testOutlineView");
		
		Bundle bundle = FrameworkUtil.getBundle(FileTreeLabelProvider.class);
		URL url = FileLocator.find(bundle, new Path("icons/Folder.ico"), null);
		ImageDescriptor imageDcr = ImageDescriptor.createFromURL(url);
		Image folderImage = imageDcr.createImage();
		
		url = FileLocator.find(bundle, new Path("icons/File.ico"), null);
		imageDcr = ImageDescriptor.createFromURL(url);
		Image fileImage = imageDcr.createImage();
		
		IEditorInput input = (IEditorInput) getEditorInput();
		Object obj = input.getAdapter(FileStoreEditorInput.class);
		
		if(obj != null)
			fsInput = (FileStoreEditorInput) obj;
		
		URI uri = fsInput.getURI();
		File file = new File(uri);
		
		fileModelList = new ArrayList<FileModel>();
		
		graph = new Graph(parent, SWT.NONE);
		graph.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		graph.addListener(SWT.Resize, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (graph.getLayoutAlgorithm().getClass().equals(HorizontalTreeLayoutAlgorithm.class)) {
					graph.setLayoutAlgorithm(new HorizontalTreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
				} else if (graph.getLayoutAlgorithm().getClass().equals(TreeLayoutAlgorithm.class)) {
					graph.setLayoutAlgorithm(new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
				} else if (graph.getLayoutAlgorithm().getClass().equals(HorizontalShift.class)) {
					graph.setLayoutAlgorithm(new HorizontalShift(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
				} else if (graph.getLayoutAlgorithm().getClass().equals(GridLayoutAlgorithm.class)) {
					graph.setLayoutAlgorithm(new GridLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
				}
			}
		});	
		graph.addMouseWheelListener( new MouseWheelListener() {
			@Override
			public void mouseScrolled(org.eclipse.swt.events.MouseEvent e) {
				 if (( e.stateMask & SWT.CTRL ) == 0)
	                    return; 
				 
                if (e.count > 0) {
                	System.out.println("Zoom In");
                	graph.getRootLayer().setScale(graph.getRootLayer().getScale()*1.1f);
                } else if (e.count < 0) {
					System.out.println("Zoom Out");
					graph.getRootLayer().setScale(graph.getRootLayer().getScale()*0.9f);
                }
			}
        } );
		
		if(file.getParent() == null){
			root = new GraphNode(graph, SWT.NONE, file.toString(), folderImage);
			setPartName(file.toString());
		}
		else {
			root = new GraphNode(graph, SWT.NONE, file.getName(), folderImage);
		}
		
		if(file.listFiles() != null){
			for(File childFile : file.listFiles()){
				if(childFile.isDirectory()){
					GraphNode childNode = new GraphNode(graph, SWT.NONE, childFile.getName(), folderImage);
					conn = new GraphConnection(graph, ZestStyles.CONNECTIONS_SOLID, root, childNode);
					if(childFile.listFiles() != null){
						for(File grandChildFile : childFile.listFiles()){
							if(grandChildFile.isDirectory()){
								GraphNode grandChildNode = new GraphNode(graph, SWT.NONE, grandChildFile.getName(), folderImage);
								childConn = new GraphConnection(graph, ZestStyles.CONNECTIONS_SOLID, childNode, grandChildNode);
							}
							else {
								GraphNode grandChildNode = new GraphNode(graph, SWT.NONE, grandChildFile.getName(), fileImage);
								childConn = new GraphConnection(graph, ZestStyles.CONNECTIONS_SOLID, childNode, grandChildNode);
							}
						}
					}
				}
				
				else {
					GraphNode childNode = new GraphNode(graph, SWT.NONE, childFile.getName(), fileImage);
					conn = new GraphConnection(graph, ZestStyles.CONNECTIONS_SOLID, root, childNode);
				}
			}		
		}
		
		else {
			// do Nothing
		}
		
		graph.setLayoutAlgorithm(new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
		
		graph.addMouseWheelListener(new MouseWheelListener() {
			
			@Override
			public void mouseScrolled(MouseEvent e) {
				if((e.stateMask & SWT.CTRL) == 0)
					return;
				
				if(e.count > 0)
					graph.getRootLayer().setScale(graph.getRootLayer().getScale()*1.1f);
				
				if(e.count < 0)
					graph.getRootLayer().setScale(graph.getRootLayer().getScale()*0.9f);
			}
		});
		
		
		graph.addFocusListener(new FocusListener(){
			@Override
			public void focusGained(FocusEvent e) {
				olv.getCanvas().redraw();
			}
			
			@Override
			public void focusLost(FocusEvent e) {
				olv.getCanvas().redraw();
			}
		});
		
		graph.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetSelected(SelectionEvent e) {
				if(e.item instanceof GraphNode){
					GraphNode selectedNode = (GraphNode) e.item;
					fileModelList.clear();
					
					if(!selectedNode.getTargetConnections().isEmpty()){
						GraphConnection gc = (GraphConnection) selectedNode.getTargetConnections().get(0);
						GraphNode srcOfSelectedNode = (GraphNode) gc.getSource();
						File nodeFile = new File(file.getPath() + "\\" + selectedNode.getText());
						
						fileModelList.add(new FileModel("파일명", selectedNode.getText()));
						fileModelList.add(new FileModel("상위폴더", srcOfSelectedNode.getText()));
						fileModelList.add(new FileModel("경로", nodeFile.getPath()));
						fileModelList.add(new FileModel("크기", nodeFile.length() + " Bytes"));
					}
					else{
						fileModelList.add(new FileModel("파일명", selectedNode.getText()));
						fileModelList.add(new FileModel("상위폴더", file.getParent()));
						fileModelList.add(new FileModel("경로", file.getPath()));
						fileModelList.add(new FileModel("크기", file.length() + " Bytes"));
					}
					olv.getTableViewer().setInput(fileModelList);
					olv.getTableViewer().refresh();
				}
				
				else {
					fileModelList.clear();
					olv.getTableViewer().setInput(fileModelList);
					olv.getTableViewer().refresh();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		
	}

	@Override
	protected PaletteRoot getPaletteRoot() {
		PaletteViewCreator pvc = new PaletteViewCreator();
		
		if(PALETTE_MODEL == null)
			PALETTE_MODEL = pvc.createPaletteRoot();
			
		return PALETTE_MODEL;
	}
	
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
		setPartName(input.getName());
	}
	
	public Graph getGraph(){
		return graph;
	}
	
	@Override
	public void dispose(){
		graph.dispose();
		fileModelList.clear();
		olv.getCanvas().redraw();
		olv.getTableViewer().setInput(fileModelList);
		olv.getTableViewer().refresh();
	}
	
	@Override
	public void setFocus(){
		graph.setFocus();
	}
}
