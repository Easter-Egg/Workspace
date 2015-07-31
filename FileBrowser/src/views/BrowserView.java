package views;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.part.EditorInputTransfer;
import org.eclipse.ui.part.ViewPart;

import editors.GraphEditor;
import utils.FileOpenAction;
import utils.FileTreeContentProvider;
import utils.FileTreeLabelProvider;

@SuppressWarnings("restriction")
public class BrowserView extends ViewPart {
	
	private StructuredViewer treeViewer;
	private Tree tree;
	public static final String ID = "FileBrowser.browserView";
	
	private IDoubleClickListener l = new IDoubleClickListener() {
		
		@Override
		public void doubleClick(DoubleClickEvent event) {
			String path = event.getSelection().toString();
			Date dt = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd, hh:mm:ss:SSS a"); 
			System.out.println(sdf.format(dt).toString() +  " " + path);
			FileOpenAction foa = FileOpenAction.getInstance();
			foa.run();
		}
	};
	
	public BrowserView() {
		
	}
	

	@Override
	public void createPartControl(Composite parent) {
		tree = new Tree(parent, SWT.V_SCROLL | SWT.H_SCROLL);

		int curStyle = OS.GetWindowLong(tree.handle, OS.GWL_STYLE);
		int newStyle = curStyle | OS.TVS_HASLINES;
		OS.SetWindowLong(tree.handle, OS.GWL_STYLE, newStyle);
		
		treeViewer = new TreeViewer(tree);
		treeViewer.setContentProvider(new FileTreeContentProvider());
		treeViewer.setLabelProvider(new FileTreeLabelProvider());
		treeViewer.setInput(File.listRoots());
		treeViewer.setSorter(new ViewerSorter(){
			public int compare(Viewer viewer, Object p1, Object p2){
				File file1 = (File)p1;
				File file2 = (File)p2;
				return file1.compareTo(file2);
			}
		});
		
		MenuManager menuManager = new MenuManager();
		Menu menu = menuManager.createContextMenu(((TreeViewer) treeViewer).getTree());
		((TreeViewer) treeViewer).getTree().setMenu(menu);
		getSite().registerContextMenu(menuManager, treeViewer);
		
		getSite().setSelectionProvider(treeViewer);
		treeViewer.addDoubleClickListener(l);
		initDragAndDrop(treeViewer);
	}
	
	protected void initDragAndDrop(final StructuredViewer viewer){
		int operations = DND.DROP_COPY | DND.DROP_DEFAULT;
		Transfer[] transferTypes = new Transfer[]{EditorInputTransfer.getInstance()};
		
		DragSourceListener DragListener = new DragSourceListener(){

			@Override
			public void dragStart(DragSourceEvent event) {
				IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
				event.doit = (selection.size() > 0) ? true : false;
			}

			@Override
			public void dragSetData(DragSourceEvent event) {
				if(EditorInputTransfer.getInstance().isSupportedType(event.dataType)){
					IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
					Object selectedObjects = selection.toArray()[0];
					File file = new File(selectedObjects.toString());
					IPath ipath = new Path(file.getAbsolutePath());
					IFileStore fs = EFS.getLocalFileSystem().getStore(ipath);
					FileStoreEditorInput fileStoreEditorInput = new FileStoreEditorInput(fs);
					EditorInputTransfer.EditorInputData inputs[] = new EditorInputTransfer.EditorInputData[1];
					EditorInputTransfer.EditorInputData data = EditorInputTransfer.createEditorInputData(GraphEditor.ID, fileStoreEditorInput);
					inputs[0] = data;
					event.data = inputs;
					return;
				}
				event.doit = false;
			}

			@Override
			public void dragFinished(DragSourceEvent event) {
				System.out.println("Drag Finished");
			}
		};
		viewer.addDragSupport(operations, transferTypes, DragListener);
	}

	@Override
	public void setFocus() {
		treeViewer.getControl().setFocus();
	}

	@Override
	public void dispose() {
		super.dispose();
		treeViewer.removeDoubleClickListener(l);
	}
	
	public TreeViewer getTreeViewer(){
		return (TreeViewer) treeViewer;
	}
}
