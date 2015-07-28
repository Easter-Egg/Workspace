package views;

import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.parts.Thumbnail;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import editors.GraphEditor;

public class TestOutlineView extends ViewPart {
	
	public static final String ID = "FileBrowser.testOutlineView";
	
	private Canvas canvas;
	private Thumbnail thumbnail;
	private Text text;
	private IWorkbenchPage page;
	private GraphEditor ge;
	
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
				if(page.getActiveEditor() instanceof GraphEditor) {
					ge = (GraphEditor) page.getActiveEditor();
					thumbnail = new Thumbnail(ge.getGraph().getContents());
					lws.setContents(thumbnail);
					ge.getGraph().addDisposeListener(disposeListener);
				}
			}
		});
        
		text = new Text(parent, SWT.READ_ONLY | SWT.MULTI | SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		text.setText("print infomation of selected file");
	}

	public void setFocus() {
		text.setFocus();
	}
	
	public Text getText(){
		return text;
	}
	
	public Canvas getCanvas(){
		return canvas;
	}
	
}
