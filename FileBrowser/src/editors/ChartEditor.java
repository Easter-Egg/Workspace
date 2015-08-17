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
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.nebula.visualization.xygraph.figures.XYGraph;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.menus.IMenuService;
import org.eclipse.ui.part.EditorPart;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYPointerAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.experimental.chart.swt.ChartComposite;
import org.jfree.ui.TextAnchor;

import utils.ChartCompositeWithKeyListener;
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
	private List<String> yTitles;
	private List<XYSeries> seriesY;
	private ChartComposite cc;
	private IWorkbenchPage page;
	private TestOutlineView olv;
	private List<FileModel> pointCoordinate;
	private Random r = new Random(10);
	private ToolBarManager tm;
	private XYPointerAnnotation pointer;
	private XYPlot plot;
	
	private ChartMouseListener chartMouseListener = new ChartMouseListener(){

		@Override
		public void chartMouseClicked(ChartMouseEvent arg0) {
			if(arg0.getChart().getXYPlot().getAnnotations().size() == 0)
				plot.addAnnotation(pointer);
			
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
		}
		
	};
	

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
	
	@SuppressWarnings({ "resource" })
	private XYSeriesCollection createDataset(File file) {
		final XYSeriesCollection result = new XYSeriesCollection();
		double x = 0, y = 0;
		int i = 0; 
		yTitles = new ArrayList<String>();
		seriesY = new ArrayList<XYSeries>();
		
		try{
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			StringTokenizer st = new StringTokenizer(line, ",");
			xTitle = st.nextToken();
			
			while(st.hasMoreTokens()) {
				yTitles.add(st.nextToken() + " - " + file.getName());
				seriesY.add(new XYSeries(yTitles.get(i)));
				i++; 
			}
			
			while((line = br.readLine()) != null){
				i = 0 ;
				st = new StringTokenizer(line, ",");
				x = Double.parseDouble(st.nextToken());
				while (st.hasMoreTokens()) {
					y = Double.parseDouble(st.nextToken());
					seriesY.get(i).add(x, y);
					i++;
				}
			}
			
			for(i = 0 ; i < seriesY.size() ; i++)
				result.addSeries(seriesY.get(i));
			
		} catch (Exception e) { }
		
		return result;  
	}  
	 
	private JFreeChart createChart(final XYSeriesCollection dataset, final String title) {
		final XYLineAndShapeRenderer renderer1 = new XYLineAndShapeRenderer(false, true);
		final NumberAxis domainAxis = new NumberAxis("X Axis");
		final NumberAxis rangeAxis = new NumberAxis("Y Axis");
		plot = new XYPlot(dataset, domainAxis, rangeAxis, renderer1);
		
		NumberFormat format = NumberFormat.getNumberInstance();
	    format.setMaximumFractionDigits(4);
		XYItemLabelGenerator generator = new StandardXYItemLabelGenerator(
				StandardXYItemLabelGenerator.DEFAULT_ITEM_LABEL_FORMAT,
	                format, format);
		
		renderer1.setBaseItemLabelGenerator(generator);
	    renderer1.setBaseItemLabelsVisible(true);
	    
		double annoX = Double.parseDouble(dataset.getX(0, 5).toString());
		double annoY = Double.parseDouble(dataset.getY(0, 5).toString());
		String label = "("+ annoX + ", " + annoY + ")";
		pointer = new XYPointerAnnotation(label, annoX, annoY, 3.0 * Math.PI / 4.0);
		
		pointer.setBaseRadius(45.0);
	    pointer.setTipRadius(5.0);
	    pointer.setPaint(Color.blue);
	    pointer.setLabelOffset(10.0);
	    pointer.setTextAnchor(TextAnchor.HALF_ASCENT_RIGHT);
	    
	    plot.addAnnotation(pointer);

		JFreeChart chart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT, plot, true);
	    plot.setDomainPannable(true);
	    plot.setRangePannable(true);
		return chart;  
	} 

	@Override
	public void createPartControl(Composite parent) {
		
		parent.setLayout(new GridLayout(1, false));
		ToolBar toolbar = new ToolBar(parent, SWT.None);
		toolbar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		ToolBarManager tm = new ToolBarManager(toolbar);
		this.tm = tm;		
		((IMenuService) getEditorSite().getService(IMenuService.class)).populateContributionManager(tm, "toolbar:FileBrowser.ChartEditor");
		
		page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		olv = (TestOutlineView) page.findView("FileBrowser.testOutlineView");
		IEditorInput editorInput = getEditorInput();
		FileStoreEditorInput fsInput = (FileStoreEditorInput)editorInput;
		URI uri = fsInput.getURI();
		File file = new File(uri);
		
		pointCoordinate = new ArrayList<FileModel>();
		
		final XYSeriesCollection dataset = createDataset(file);  
		final JFreeChart chart = createChart(dataset, file.getName());
		cc = new ChartCompositeWithKeyListener(parent, SWT.NONE, chart, true);
		cc.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		cc.setHorizontalAxisTrace(true);
		cc.setVerticalAxisTrace(true);
		cc.addChartMouseListener(chartMouseListener);
	}

	@Override
	public void setFocus() {
		cc.setFocus();
	}
	
	public ChartComposite getXYGraph(){
		return cc;
	}
	
	@Override
	public void dispose(){
		cc.getChart().getXYPlot().getAnnotations().clear();
		pointCoordinate.clear();
		olv.getTableViewer().refresh();
		cc.removeChartMouseListener(chartMouseListener);
	}
}
