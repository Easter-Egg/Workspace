package editors;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URI;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.nebula.visualization.xygraph.figures.XYGraph;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DragDetectEvent;
import org.eclipse.swt.events.DragDetectListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.part.EditorPart;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYPointerAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.experimental.chart.swt.ChartComposite;
import org.jfree.ui.TextAnchor;

import utils.FileModel;
import views.TestOutlineView;

@SuppressWarnings("unused")
public class ChartEditor extends EditorPart{
	public ChartEditor() {
	}
	
	public static final String ID = "FileBrowser.chartEditor";
	private Canvas canvas;
	private XYGraph xyGraph;
	private String xTitle, yTitle;
	private ChartComposite cc;
	private IWorkbenchPage page;
	private TestOutlineView olv;
	private List<FileModel> pointCoordinate;

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
	private XYSeriesCollection createDataset(File file) {  
		final XYSeriesCollection result = new XYSeriesCollection();
		double x,y = 0;
		
		try{
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			StringTokenizer st = new StringTokenizer(line, ",");
			xTitle = st.nextToken();
			yTitle = st.nextToken();
			final XYSeries seriesY = new XYSeries(file.getName() + " - " + yTitle);

			while((line = br.readLine()) != null){
				st = new StringTokenizer(line, ",");
				x = Double.parseDouble(st.nextToken());
				while (st.hasMoreTokens()) {y = Double.parseDouble(st.nextToken()); }
				seriesY.add(x, y);
			}
			result.addSeries(seriesY);
		} catch (Exception e) { }
		return result;  
	}  
	 
	private JFreeChart createChart(final XYSeriesCollection dataset, final String title) {
		final XYItemRenderer renderer1 = new StandardXYItemRenderer();
		renderer1.setSeriesPaint(0, null);
		final ValueAxis domainAxis = new NumberAxis("Max Value");
		final ValueAxis rangeAxis = new NumberAxis("GC Time");
		final XYPlot plot = new XYPlot(dataset, domainAxis, rangeAxis, renderer1);
		
		NumberFormat format = NumberFormat.getNumberInstance();
	    format.setMaximumFractionDigits(4);
		XYItemLabelGenerator generator = new StandardXYItemLabelGenerator(
				StandardXYItemLabelGenerator.DEFAULT_ITEM_LABEL_FORMAT,
	                format, format);
		
		renderer1.setBaseItemLabelGenerator(generator);
	    renderer1.setBaseItemLabelsVisible(true);
	    
		double annoX = Double.parseDouble(dataset.getX(0, 4).toString());
		double annoY = Double.parseDouble(dataset.getY(0, 4).toString());
		String label = "("+ annoX + ", " + annoY + ")";
		final XYPointerAnnotation pointer = new XYPointerAnnotation(label, annoX, annoY, 3.0 * Math.PI / 4.0);
		
		pointer.setBaseRadius(45.0);
	    pointer.setTipRadius(5.0);
	    pointer.setPaint(Color.blue);
	    pointer.setLabelOffset(10.0);
	    pointer.setTextAnchor(TextAnchor.HALF_ASCENT_RIGHT);
	    
	    plot.addAnnotation(pointer);
	    
		JFreeChart chart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT, plot, true);
		return chart;  
	} 

	@Override
	public void createPartControl(Composite parent) {
		
		page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		olv = (TestOutlineView) page.findView("FileBrowser.testOutlineView");
		Random r = new Random(10);
		IEditorInput editorInput = getEditorInput();
		FileStoreEditorInput fsInput = (FileStoreEditorInput)editorInput;
		URI uri = fsInput.getURI();
		File file = new File(uri);
		
		pointCoordinate = new ArrayList<FileModel>();
		
		final XYSeriesCollection dataset = createDataset(file);  
		final JFreeChart chart = createChart(dataset, file.getName());
		
		cc = new ChartComposite(parent, SWT.NONE, chart, true);
		cc.addChartMouseListener(new ChartMouseListener(){

			@Override
			public void chartMouseClicked(ChartMouseEvent arg0) {
				XYPointerAnnotation anno = ((XYPointerAnnotation) arg0.getChart().getXYPlot().getAnnotations().get(0));
				ChartEntity entity = arg0.getEntity();
				pointCoordinate.clear();
				if (entity != null && (entity instanceof XYItemEntity)) {
	                XYItemEntity item = (XYItemEntity) entity;
	                double xValue = item.getDataset().getXValue(item.getSeriesIndex(), item.getItem());
	                double yValue = item.getDataset().getYValue(item.getSeriesIndex(), item.getItem());
	                String text = "("+ xValue + ", " + yValue + ")";
	                anno.setAngle(r.nextDouble() *  Math.PI / r.nextDouble());
	                anno.setText(text);
	                anno.setX(xValue);
	                anno.setY(yValue);
	                
	                pointCoordinate.add(new FileModel("X", xValue+""));
	                pointCoordinate.add(new FileModel("Y", yValue+""));
	                olv.getTableViewer().setInput(pointCoordinate);
					olv.getTableViewer().refresh();
				}
			}

			@Override
			public void chartMouseMoved(ChartMouseEvent arg0) {
				// TODO Auto-generated method stub
			}
			
		});
		cc.addMouseWheelListener(new MouseWheelListener(){

			@Override
			public void mouseScrolled(MouseEvent e) {
				if (( e.stateMask & SWT.CTRL ) == 0)
	                    return; 
				 
				if (e.count > 0) {
					System.out.println("Zoom in");
					cc.zoomInBoth(e.x, e.y);
				} else if (e.count < 0) {
					System.out.println("Zoom out");
					cc.zoomOutBoth(e.x, e.y);
				}
			}
			
		});
	}
		/*
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
		
		xyGraph.addKeyListener(keyListener);
		canvas.addMouseWheelListener(mouseWheelListener);
		ToolbarArmedXYGraph toolbarArmedXYGraph = new ToolbarArmedXYGraph(xyGraph);
		toolbarArmedXYGraph.getToolbar().addSeparator();
		lws.setContents(toolbarArmedXYGraph);
	}*/

	@Override
	public void setFocus() {
		//canvas.setFocus();
		cc.setFocus();
	}
	
	public ChartComposite getXYGraph(){
		//return xyGraph;
		return cc;
	}
	
	@Override
	public void dispose(){
		//canvas.removeMouseWheelListener(mouseWheelListener);
		//xyGraph.removeKeyListener(keyListener);
		cc.getChart().getXYPlot().getAnnotations().clear();
		pointCoordinate.clear();
		olv.getTableViewer().refresh();
	}
}
