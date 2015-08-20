package editors;
import java.io.File;
import java.net.URI;
import java.util.Random;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.part.EditorPart;

public class BMPViewer extends EditorPart{
	public static final String ID = "FileBrowser.BMPViewer";
	public ScrollBar hBar = null;
	public ScrollBar vBar = null;
	public Point origin = new Point (0, 0);
	public ToolBarManager tm;
	private Image image;
	public Canvas canvas;
	@SuppressWarnings("unused")
	private IWorkbenchPage page;
	
	public BMPViewer() {
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
		page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
/*
		IEditorInput editorInput = getEditorInput();
		FileStoreEditorInput fsInput = (FileStoreEditorInput)editorInput;
		URI uri = fsInput.getURI();
		File file = new File(uri);
		image = new Image(parent.getDisplay(), file.getAbsolutePath());*/
		
		Random r = new Random();
		int i = 0;
		//float j = 0;
		PaletteData palette = new PaletteData(0xFF, 0x00FF, 0x0000FF);
		ImageData imageData = new ImageData(128, 64, 8, palette);
		for(int x = 0 ; x < 128 ; x++){
	        for(int y = 0 ; y < 64 ; y++){
	        	i = r.nextInt(255);
	        	//j = r.nextFloat() * r.nextInt(10);
	            imageData.setPixel(x, y, i);
	        }
	    }
		
		image = new Image(parent.getDisplay(), imageData);
		canvas = new Canvas(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.NONE);
		hBar = canvas.getHorizontalBar();
		vBar = canvas.getVerticalBar();
		
		canvas.addListener (SWT.Paint, new Listener () {
			@Override
			public void handleEvent (Event e) {
				GC gc = e.gc;
				gc.drawImage (image, origin.x, origin.y);
				canvas.setSize(128 + canvas.getVerticalBar().getSize().x, 64 + canvas.getHorizontalBar().getSize().y);
				Rectangle rect = image.getBounds();
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
	}
	
	@Override
	public void dispose(){
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
