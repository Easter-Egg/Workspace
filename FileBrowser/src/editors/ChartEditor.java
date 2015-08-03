package editors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URI;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.KeyEvent;
import org.eclipse.draw2d.KeyListener;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.nebula.visualization.xygraph.dataprovider.CircularBufferDataProvider;
import org.eclipse.nebula.visualization.xygraph.figures.Trace;
import org.eclipse.nebula.visualization.xygraph.figures.Trace.PointStyle;
import org.eclipse.nebula.visualization.xygraph.figures.XYGraph;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.part.EditorPart;

public class ChartEditor extends EditorPart{
	
	public static final String ID = "FileBrowser.chartEditor";
	private Canvas canvas;
	private XYGraph xyGraph;

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
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
	public void doSave(IProgressMonitor monitor) {
		
	}

	@Override
	public void doSaveAs() {
		
	}

	@SuppressWarnings("resource")
	@Override
	public void createPartControl(Composite parent) {
		canvas = new Canvas(parent, SWT.BORDER);
		canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		final LightweightSystem lws = new LightweightSystem(canvas);
		
		int i = 0;
		
		double xValue[] = new double[11];
		double yValue[] = new double[11];
		xyGraph = new XYGraph();
		
		xyGraph.getXAxisList().get(0).setTitle("X 축");
		xyGraph.getYAxisList().get(0).setTitle("Y 축");
		
		IEditorInput editorInput = getEditorInput();
		FileStoreEditorInput fsInput = (FileStoreEditorInput)editorInput;
		URI uri = fsInput.getURI();
		File file = new File(uri);
		xyGraph.setTitle(file.getName());
		
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			StringTokenizer st = new StringTokenizer(line, ",");
			xyGraph.getXAxisList().get(0).setTitle(st.nextToken());
			xyGraph.getYAxisList().get(0).setTitle(st.nextToken());
			
			while((line = br.readLine()) != null){
				st = new StringTokenizer(line, ",");
				xValue[i] = Double.parseDouble(st.nextToken());
				yValue[i] = Double.parseDouble(st.nextToken());
				i++;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		CircularBufferDataProvider traceDataProvider = new CircularBufferDataProvider(false);
		traceDataProvider.setBufferSize(i);		
		traceDataProvider.setCurrentXDataArray(xValue);
		traceDataProvider.setCurrentYDataArray(yValue);
		
		Trace trace = new Trace(xyGraph.getYAxisList().get(0).getTitle(), xyGraph.primaryXAxis, xyGraph.primaryYAxis, traceDataProvider);			
		trace.setPointStyle(PointStyle.CIRCLE);
		
		xyGraph.addTrace(trace);
		xyGraph.performAutoScale();
		
		xyGraph.setFocusTraversable(true);
		xyGraph.setRequestFocusEnabled(true);

		xyGraph.getPlotArea().addMouseListener(new MouseListener.Stub() {
			@Override
			public void mousePressed(org.eclipse.draw2d.MouseEvent me) {
				xyGraph.requestFocus();
			}
		});
		
		xyGraph.addKeyListener(new KeyListener.Stub() {
			@Override
			public void keyPressed(final KeyEvent ke) {
				if((ke.getState() == SWT.CONTROL) && (ke.keycode == 's')) {
					final ImageLoader loader = new ImageLoader();
					loader.data = new ImageData[] { xyGraph.getImage().getImageData() };
					final FileDialog dialog = new FileDialog(Display.getDefault().getShells()[0], SWT.SAVE);
					dialog.setFilterNames(new String[] { "PNG Files", "All Files (*.*)" });
					dialog.setFilterExtensions(new String[] { "*.png", "*.*" }); // Windows
					final String path = dialog.open();
					if ((path != null) && !path.equals("")) {
						loader.save(path, SWT.IMAGE_PNG);
					}
				}
			}
		});
		
		canvas.addMouseWheelListener(new MouseWheelListener(){

			@Override
			public void mouseScrolled(MouseEvent e) {
				if((e.stateMask & SWT.CTRL) == 0)
					return;
				
				if(e.count > 0){
					xyGraph.getPlotArea().zoomInOut(true, true, e.x, e.y, e.count * 0.1 / 3);
				}
				
				if(e.count < 0){
					xyGraph.getPlotArea().zoomInOut(true, true, e.x, e.y, e.count * 0.1 / 3);
				}
			}
		});
		
		lws.setContents(xyGraph);
	}

	@Override
	public void setFocus() {
		canvas.setFocus();
	}

}
