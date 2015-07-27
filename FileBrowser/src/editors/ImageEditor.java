package editors;
import java.io.File;
import java.net.URI;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.menus.IMenuService;
import org.eclipse.ui.part.EditorPart;

import views.TestOutlineView;

public class ImageEditor extends EditorPart{
	public static final String ID = "FileBrowser.ImageEditor";
	public ScrollBar hBar = null;
	public ScrollBar vBar = null;
	public Point origin = new Point (0, 0);
	public ToolBarManager tm;
	private Image image;
	public Canvas canvas;
	private IWorkbenchPage page;
	
	public ImageEditor() {
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
		setInput(input);
		setPartName(input.getName());
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		ToolBar toolbar = new ToolBar(parent, SWT.None);
		toolbar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		ToolBarManager tm = new ToolBarManager(toolbar);
		this.tm = tm;		
		((IMenuService) getEditorSite().getService(IMenuService.class)).populateContributionManager(tm, "toolbar:FileBrowser.ImageEditor");
		
		page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

		IEditorInput editorInput = getEditorInput();
		FileStoreEditorInput fsInput = (FileStoreEditorInput)editorInput;
		URI uri = fsInput.getURI();
		File file = new File(uri);
		image = new Image(parent.getDisplay(), file.getAbsolutePath());
		canvas = new Canvas(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.None);
		canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		hBar = canvas.getHorizontalBar();
		
		vBar = canvas.getVerticalBar();
		
		canvas.addListener (SWT.Resize,  new Listener() {
			@Override
			public void handleEvent(Event e) {
				Rectangle rect = image.getBounds();
				Rectangle client = canvas.getClientArea();
				
				hBar.setMaximum (rect.width);
				vBar.setMaximum (rect.height);
				hBar.setThumb (Math.min(rect.width, client.width));
				vBar.setThumb (Math.min(rect.height, client.height));
				
				int hPage = rect.width - client.width;
				int vPage = rect.height - client.height;
				int hSel = hBar.getSelection();
				int vSel = vBar.getSelection();
				
				if (hSel >= hPage){
					if (hPage <= 0) 
						hSel = 0;
					origin.x = -hSel;
				}
				
				if (vSel >= vPage){
					if (vPage <= 0) 
						vSel = 0;
					origin.y = -vSel;
				}
				
				canvas.redraw ();
			}
		});
		
		canvas.addListener (SWT.Paint, new Listener () {
			@Override
			public void handleEvent (Event e) {
				GC gc = e.gc;
				gc.drawImage (image, origin.x, origin.y);
				Rectangle rect = image.getBounds ();
				Rectangle client = canvas.getClientArea ();
				int marginWidth = client.width - rect.width;
				if (marginWidth > 0) {
					gc.fillRectangle (rect.width, 0, marginWidth, client.height);
				}
				int marginHeight = client.height - rect.height;
				if (marginHeight > 0) {
					gc.fillRectangle (0, rect.height, client.width, marginHeight);
				}
			}
		});
		
		hBar.addListener(SWT.Selection, new Listener(){
			@Override
			public void handleEvent(Event event) {
				int hSel = hBar.getSelection();
				int destX = -hSel - origin.x;
				Rectangle rect = image.getBounds();
				canvas.scroll(destX, 0, 0, 0, rect.width, rect.height, false);
				origin.x = -hSel;
			}
		});
		vBar.addListener(SWT.Selection, new Listener(){
			@Override
			public void handleEvent(Event event) {
				int vSel = vBar.getSelection();
				int destY = -vSel - origin.y;
				Rectangle rect = image.getBounds();
				canvas.scroll(0, destY, 0, 0, rect.width, rect.height, false);
				origin.y = -vSel;
			}
		});
		
		canvas.addFocusListener(new FocusListener(){
			@Override
			public void focusGained(FocusEvent e) {
				TestOutlineView olv = (TestOutlineView) page.findView("FileBrowser.testOutlineView");
				olv.getText().setText("File Name : " + file.getName() + "\nFile Size : " + file.length() + " Bytes");
			}
			@Override
			public void focusLost(FocusEvent e) {
			}
		});
		
		MenuManager menuManager = new MenuManager();
		Menu menu = menuManager.createContextMenu(canvas);
		canvas.setMenu(menu);
		getSite().registerContextMenu(menuManager, getSite().getSelectionProvider());
	}
	
	@Override
	public void dispose(){
		((IMenuService) getEditorSite().getService(IMenuService.class)).releaseContributions(tm);
	}

	@Override
	public void setFocus() {
		canvas.setFocus();
	}
	
	public Image getImage(){
		return image;
	}
	
	public Canvas getCanvas(){
		return canvas;
	}
}
