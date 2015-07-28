package views;

import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.parts.ScrollableThumbnail;
import org.eclipse.draw2d.parts.Thumbnail;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import editors.GraphEditor;
import org.eclipse.swt.layout.GridData;

public class ThumbNailView extends ViewPart {
	public static final String ID = "FileBrowser.thumbNailView";
	private IWorkbenchPage page;
	private GraphEditor ge;
	private Thumbnail thumbnail;
	private DisposeListener disposeListener;
	private Canvas canvas;
	
	public ThumbNailView() {
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		ge = new GraphEditor();
		ge = (GraphEditor) page.getActiveEditor();
		
		canvas = new Canvas(parent, SWT.BORDER);
		canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		LightweightSystem lws = new LightweightSystem(canvas);
		thumbnail = new Thumbnail(ge.getGraph().getContents());
		lws.setContents(thumbnail);
		
		disposeListener = new DisposeListener() {
	      @Override
	      public void widgetDisposed(DisposeEvent e) {
	         if (thumbnail != null) {
	             thumbnail.deactivate();
	              thumbnail = null;
	         }
	      }
	    };
	    ge.getGraph().addDisposeListener(disposeListener);
	}

	@Override
	public void setFocus() {
		canvas.setFocus();
	}
}
