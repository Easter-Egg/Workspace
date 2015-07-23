package views;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.part.ViewPart;

import utils.FileOpenAction;
import utils.FileTreeContentProvider;
import utils.FileTreeLabelProvider;

@SuppressWarnings("restriction")
public class BrowserView extends ViewPart {
	
	private TreeViewer treeViewer;
	private Tree tree;
	public static final String ID = "FileBrowser.browserView";
	
	private IDoubleClickListener l = new IDoubleClickListener() {
		
		@Override
		public void doubleClick(DoubleClickEvent event) {
			String path = event.getSelection().toString();
			FileOpenAction foa = FileOpenAction.getInstance();
			foa.run();
			
			Date dt = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd, hh:mm:ss:SSS a"); 
			System.out.println(sdf.format(dt).toString() +  " " + path);
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
		Menu menu = menuManager.createContextMenu(treeViewer.getTree());
		treeViewer.getTree().setMenu(menu);
		getSite().registerContextMenu(menuManager, treeViewer);
		
		getSite().setSelectionProvider(treeViewer);
		treeViewer.addDoubleClickListener(l);
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
		return treeViewer;
	}
}
