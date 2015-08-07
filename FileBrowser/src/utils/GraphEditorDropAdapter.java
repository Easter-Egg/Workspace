package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.eclipse.nebula.visualization.xygraph.dataprovider.CircularBufferDataProvider;
import org.eclipse.nebula.visualization.xygraph.figures.Trace;
import org.eclipse.nebula.visualization.xygraph.figures.Trace.PointStyle;
import org.eclipse.nebula.visualization.xygraph.figures.XYGraph;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.part.EditorInputTransfer;

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
		System.out.println(">>>>>>>> Drag Leave");
	}

	@Override
	public void dragOver(DropTargetEvent event) {
		event.feedback = DND.FEEDBACK_NONE;
	}

	@SuppressWarnings({ "unused", "resource" })
	@Override
	public void drop(DropTargetEvent event) {
		System.out.println(">>>>>>>> Dropped");
		
		if(EditorInputTransfer.getInstance().isSupportedType(event.currentDataType)){
			if(event.data == null)
				return;
			
			EditorInputTransfer.EditorInputData[] editorInputs = (EditorInputTransfer.EditorInputData[]) event.data;
			IEditorInput input = editorInputs[0].input;
			String editorId = editorInputs[0].editorId;
			
			if((window.getActivePage().getActiveEditor() instanceof ChartEditor) && editorId.equals(ChartEditor.ID)){
				XYGraph xyg = ((ChartEditor) window.getActivePage().getActiveEditor()).getXYGraph();
				
				if(input.getName().equals(xyg.getTitle())){
					System.out.println("Already Opened.");
					return;
				}
				
				FileStoreEditorInput fsInput = (FileStoreEditorInput) input;
				
				int i = 0;
				double x,y;
				File file = new File(fsInput.getURI());
				String xTitle = null;
				String yTitle = null;
				
				FileReader fr;
				BufferedReader br;
				
				ArrayList<Double> xList = new ArrayList<Double>();
				ArrayList<Double> yList = new ArrayList<Double>();
				try {
					fr = new FileReader(file);
					br = new BufferedReader(fr);
					String line = br.readLine();
					StringTokenizer st = new StringTokenizer(line, ",");
					xTitle = st.nextToken();
					yTitle = st.nextToken();
					
					for(Trace t : xyg.getPlotArea().getTraceList()){
						if(t.getName().equals(file.getName() + " - " + yTitle)){
							System.out.println("Already added");
							return;
						}
					}
									
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
				Trace trace = new Trace(file.getName() + " - " + yTitle, xyg.primaryXAxis, xyg.primaryYAxis, traceDataProvider);			
				trace.setPointStyle(PointStyle.TRIANGLE);
				
				xyg.addTrace(trace);
				xyg.performAutoScale();				
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
}
