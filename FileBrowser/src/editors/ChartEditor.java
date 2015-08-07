package editors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.KeyEvent;
import org.eclipse.draw2d.KeyListener;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.nebula.visualization.xygraph.dataprovider.CircularBufferDataProvider;
import org.eclipse.nebula.visualization.xygraph.figures.ToolbarArmedXYGraph;
import org.eclipse.nebula.visualization.xygraph.figures.Trace;
import org.eclipse.nebula.visualization.xygraph.figures.Trace.PointStyle;
import org.eclipse.nebula.visualization.xygraph.figures.XYGraph;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
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
	public ChartEditor() {
	}
	
	public static final String ID = "FileBrowser.chartEditor";
	private Canvas canvas;
	private XYGraph xyGraph;
	// private String xTitle, yTitle;

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
	/*
	@SuppressWarnings("resource")
	private XYSeriesCollection createDataset(File file) {  
		final XYSeriesCollection result = new XYSeriesCollection();
		
		double x,y,y2;
		
		final XYSeries seriesY = new XYSeries("Y");
		final XYSeries seriesY2 = new XYSeries("Y2");
		
		try{
			
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			StringTokenizer st = new StringTokenizer(line, ",");
			xTitle = st.nextToken();
			yTitle = st.nextToken();

			while((line = br.readLine()) != null){
				st = new StringTokenizer(line, ",");
				x = Double.parseDouble(st.nextToken());
				y = Double.parseDouble(st.nextToken());
				y2 = Double.parseDouble(st.nextToken());
				
				seriesY.add(x, y);
				seriesY2.add(x, y2);
			}
			
		} catch (Exception e) {
			
		}
		result.addSeries(seriesY);
		result.addSeries(seriesY2);
		return result;  
	}  
	 
	private JFreeChart createChart(final XYSeriesCollection dataset, final String title) {
		final XYItemRenderer renderer1 = new StandardXYItemRenderer();
		final ValueAxis domainAxis = new NumberAxis("Max Value");
		final ValueAxis rangeAxis = new NumberAxis("GC Time");
		final XYPlot plot = new XYPlot(dataset, domainAxis, rangeAxis, renderer1);
		final XYTextAnnotation annotation = new XYTextAnnotation("Hello!", 500.0, 0.005);
		plot.addAnnotation(annotation);
		JFreeChart chart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT, plot, true);
		
		return chart;  
	}  */

	@SuppressWarnings({ "resource", "unused"})
	@Override
	public void createPartControl(Composite parent) {
		/*
		IEditorInput editorInput = getEditorInput();
		FileStoreEditorInput fsInput = (FileStoreEditorInput)editorInput;
		URI uri = fsInput.getURI();
		File file = new File(uri);
		
		final XYSeriesCollection dataset = createDataset(file);  
		final JFreeChart chart = createChart(dataset, file.getName());
		
		ChartComposite cc = new ChartComposite(parent, SWT.NONE, chart, true);
		
		
		System.out.println(chart.getXYPlot().getAnnotations());*/
		
		canvas = new Canvas(parent, SWT.BORDER);
		canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		final LightweightSystem lws = new LightweightSystem(canvas);
		int i = 0;
		double x,y;
		
		ArrayList<Double> xList = new ArrayList<Double>();
		ArrayList<Double> yList = new ArrayList<Double>();
		
		xyGraph = new XYGraph();
		IEditorInput editorInput = getEditorInput();
		FileStoreEditorInput fsInput = (FileStoreEditorInput)editorInput;
		URI uri = fsInput.getURI();
		File file = new File(uri);
		xyGraph.setTitle(file.getName());
		
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			String temp = null;
			StringTokenizer st = new StringTokenizer(line, ",");
			xyGraph.getXAxisList().get(0).setTitle(st.nextToken());
			xyGraph.getYAxisList().get(0).setTitle(st.nextToken());
			
			while((line = br.readLine()) != null){
				st = new StringTokenizer(line, ",");
				x = Double.parseDouble(st.nextToken());
				y = Double.parseDouble(st.nextToken());
				
				xList.add(x);
				yList.add(y);
				i++;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		double xValue[] = new double[xList.size()];
		double yValue[] = new double[yList.size()];
		
		for(i = 0 ; i < xList.size() ; i++)
			xValue[i] = xList.get(i);
		
		for(i = 0 ; i < yList.size() ; i++)
			yValue[i] = yList.get(i);
		
		
		CircularBufferDataProvider traceDataProvider = new CircularBufferDataProvider(false);
		traceDataProvider.setBufferSize(i);		
		traceDataProvider.setCurrentXDataArray(xValue);
		traceDataProvider.setCurrentYDataArray(yValue);
		Trace trace = new Trace(xyGraph.getYAxisList().get(0).getTitle(), xyGraph.primaryXAxis, xyGraph.primaryYAxis, traceDataProvider);			
		trace.setPointStyle(PointStyle.CIRCLE);
		trace.setLineWidth(4);
		trace.setPointSize(10);

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
					dialog.setFilterExtensions(new String[] { "*.png", "*.*" });
					final String path = dialog.open();
					if ((path != null) && !path.equals("")) {
						loader.save(path, SWT.IMAGE_PNG);
					}
				}
				
				else if((ke.getState() == SWT.CONTROL) && (ke.keycode == 'r')) {
					xyGraph.performAutoScale();
				}
			}
		});
		
		canvas.addMouseWheelListener(new MouseWheelListener(){

			@Override
			public void mouseScrolled(org.eclipse.swt.events.MouseEvent e) {
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
		
		ToolbarArmedXYGraph toolbarArmedXYGraph = new ToolbarArmedXYGraph(xyGraph);
		toolbarArmedXYGraph.getToolbar().addSeparator();
		lws.setContents(toolbarArmedXYGraph);
	}

	@Override
	public void setFocus() {
		canvas.setFocus();
	}
	
	public XYGraph getXYGraph(){
		return xyGraph;
	}
}
