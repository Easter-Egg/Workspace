package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.NumberFormat;
import java.util.StringTokenizer;

import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.part.EditorInputTransfer;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.experimental.chart.swt.ChartComposite;

import editors.ChartEditor;

public class GraphEditorDropAdapter extends DropTargetAdapter {
	private IWorkbenchWindow window;
	
	public GraphEditorDropAdapter(IWorkbenchWindow window){
		this.window = window;
	}

	@Override
	public void dragEnter(DropTargetEvent event) {
		if (event.detail == DND.DROP_DEFAULT) {
			 if ((event.operations & DND.DROP_COPY) != 0) {
				 event.detail = DND.DROP_COPY;
			 }
			 else {
				 event.detail = DND.DROP_NONE;
			 }
		 }
	}

	@Override
	public void dragLeave(DropTargetEvent event) {
	}

	@Override
	public void dragOver(DropTargetEvent event) {
		event.feedback = DND.FEEDBACK_NONE;
	}

	@Override
	public void drop(DropTargetEvent event) {
		if(EditorInputTransfer.getInstance().isSupportedType(event.currentDataType)){
			if(event.data == null)
				return;
			
			EditorInputTransfer.EditorInputData[] editorInputs = (EditorInputTransfer.EditorInputData[]) event.data;
			IEditorInput input = editorInputs[0].input;
			String editorId = editorInputs[0].editorId;
			
			if((window.getActivePage().getActiveEditor() instanceof ChartEditor) && editorId.equals(ChartEditor.ID)){
				ChartComposite cc =((ChartEditor) window.getActivePage().getActiveEditor()).getXYGraph();
				
				FileStoreEditorInput fsInput = (FileStoreEditorInput) input;
				File file = new File(fsInput.getURI());
				XYSeriesCollection addedSeries = createDataset(file);
				
				if(file.getName().equals(cc.getChart().getTitle())){
					System.out.println("Already Opened.");
					return;
				}
				
				for(int i = 0 ; i < cc.getChart().getXYPlot().getDatasetCount() ; i++){
					if(cc.getChart().getXYPlot().getDataset(i).equals(addedSeries)){
						System.out.println("Already added.");
						return;
					}
				}
				
				cc.getChart().getXYPlot().setDataset(cc.getChart().getXYPlot().getDatasetCount(), addedSeries);
				XYLineAndShapeRenderer renderer0 = new XYLineAndShapeRenderer();
				renderer0.setSeriesPaint(0, null);
				NumberFormat format = NumberFormat.getNumberInstance();
			    format.setMaximumFractionDigits(4);
				XYItemLabelGenerator generator = new StandardXYItemLabelGenerator(
						StandardXYItemLabelGenerator.DEFAULT_ITEM_LABEL_FORMAT,
			                format, format);
				
				renderer0.setBaseItemLabelGenerator(generator);
			    renderer0.setBaseItemLabelsVisible(true);
				cc.getChart().getXYPlot().setRenderer(cc.getChart().getXYPlot().getDatasetCount() - 1, renderer0);
			}
			else{
				try {
					window.getActivePage().openEditor(input, editorId);
				} catch (PartInitException e) {
					e.printStackTrace();
				}
			}
		}
		event.detail = DND.DROP_COPY;
	}
	
	@SuppressWarnings({ "resource", "unused" })
	private XYSeriesCollection createDataset(File file) {  
		final XYSeriesCollection result = new XYSeriesCollection();
		double x,y = 0;
		
		try{
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			StringTokenizer st = new StringTokenizer(line, ",");
			String xTitle = st.nextToken();
			String yTitle = st.nextToken();
			
			final XYSeries seriesY = new XYSeries(yTitle + "(" + file.getName() + ")");

			while((line = br.readLine()) != null){
				st = new StringTokenizer(line, ",");
				x = Double.parseDouble(st.nextToken());
				while (st.hasMoreTokens()) { y = Double.parseDouble(st.nextToken()); }
				seriesY.add(x, y);
			}
			
			result.addSeries(seriesY);
		} catch (Exception e) {
			
		}
		
		return result;  
	}
}
