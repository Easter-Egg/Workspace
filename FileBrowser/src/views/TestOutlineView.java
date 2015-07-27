package views;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.ScaledGraphics;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.zest.core.widgets.Graph;

import editors.GraphEditor;

public class TestOutlineView extends ViewPart {
	
	private FigureCanvas thumbnail;
	private Text text;
	private IWorkbenchPage page;
	public static final String ID = "FileBrowser.testOutlineView";
	
	public TestOutlineView() {
		
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		
		page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        thumbnail = new FigureCanvas(parent, SWT.NONE); 
        thumbnail.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        thumbnail.setBackground(ColorConstants.white);
        
        thumbnail.addListener(SWT.Paint, new Listener(){
			@Override
			public void handleEvent(Event event) {
				if(page.getActiveEditor() instanceof GraphEditor){
					System.out.println(event);
					Graph g = new Graph(parent, SWT.NONE);
					g = ((GraphEditor) page.getActiveEditor()).getGraph();
					g.setPreferredSize(event.getBounds().width, event.getBounds().height);
					thumbnail.setContents(g.getContents());
				}
			}
        });
        
		text = new Text(parent, SWT.READ_ONLY | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		text.setText("print infomation of selected file");
	}

	public void setFocus() {
		text.setFocus();
	}
	
	public Text getText(){
		return text;
	}
	
	public FigureCanvas getThumbNail(){
		return thumbnail;
	}
	
}
