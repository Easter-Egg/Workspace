package views;

import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.parts.Thumbnail;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import editors.MyGraphicalEditor;
import utils.FileModel;

public class TestOutlineView extends ViewPart {
	
	public static final String ID = "FileBrowser.testOutlineView";
	
	private Canvas canvas;
	private Thumbnail thumbnail;
	private Text text;
	private IWorkbenchPage page;
	private MyGraphicalEditor ge;
	private TableViewer tableviewer;
	
	private DisposeListener disposeListener = new DisposeListener() {
	      @Override
	      public void widgetDisposed(DisposeEvent e) {
	         if (thumbnail != null) {
	             thumbnail.deactivate();
	              thumbnail = null;
	         }
	      }
	    };
	
	public TestOutlineView() {
		
	}

	@Override
	public void createPartControl(Composite parent) {
		page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		parent.setLayout(new GridLayout(1, false));
		
		canvas = new Canvas(parent, SWT.BORDER);
		canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		LightweightSystem lws = new LightweightSystem(canvas);
		
		canvas.addListener(SWT.Paint, new Listener(){
			@Override
			public void handleEvent(Event event) {
				if(page.getActiveEditor() instanceof MyGraphicalEditor) {
					ge = (MyGraphicalEditor) page.getActiveEditor();
					thumbnail = new Thumbnail(ge.getGraph().getContents());
					lws.setContents(thumbnail);
					ge.getGraph().addDisposeListener(disposeListener);
				}
			}
		});
		
		createViewer(parent);
	}
	
	public void createViewer(Composite parent){
		tableviewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		createColumns(parent, tableviewer);
		final Table table = tableviewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		tableviewer.setContentProvider(new ArrayContentProvider());
		tableviewer.setInput(null);
		
	    GridData gridData = new GridData();
	    gridData.verticalAlignment = GridData.FILL;
	    gridData.horizontalSpan = 2;
	    gridData.grabExcessHorizontalSpace = true;
	    gridData.grabExcessVerticalSpace = true;
	    gridData.horizontalAlignment = GridData.FILL;
	    tableviewer.getControl().setLayoutData(gridData);
	}
	
	public void createColumns(final Composite parent, final TableViewer viewer){
		
		TableViewerColumn col = createTableViewerColumn("Attribute", 120, 0);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText (Object element){
				FileModel fm = (FileModel) element;
				return fm.getAttribute();
			}
		});
		
		col = createTableViewerColumn("Value", 120, 0);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText (Object element){
				FileModel fm = (FileModel) element;
				return fm.getValue();
			}
		});
	}
	
	private TableViewerColumn createTableViewerColumn(String title, int bound, final int colNumber) {
	    final TableViewerColumn viewerColumn = new TableViewerColumn(tableviewer,
	        SWT.NONE);
	    final TableColumn column = viewerColumn.getColumn();
	    column.setText(title);
	    column.setWidth(bound);
	    column.setResizable(true);
	    column.setMoveable(true);
	    return viewerColumn;
	}

	public void setFocus() {
		canvas.setFocus();
	}
	
	public Text getText(){
		return text;
	}
	
	public Canvas getCanvas(){
		return canvas;
	}
	
	public TableViewer getTableViewer(){
		return tableviewer;
	}
	
}
