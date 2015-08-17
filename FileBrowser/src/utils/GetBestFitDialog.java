package utils;


import java.awt.BasicStroke;
import java.awt.Color;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.jfree.chart.annotations.XYLineAnnotation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.statistics.Regression;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import editors.ChartEditor;

public class GetBestFitDialog extends Dialog {

	private IWorkbenchPage page;
	private ChartEditor ce;
	private XYPlot plot;
	private XYSeriesCollection dataset;
	private boolean withAverage;
	private String selectedSeries;
	
	public GetBestFitDialog(Shell parent) {
		super(parent);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		ce = (ChartEditor) page.getActiveEditor();
		plot = ce.getXYGraph().getChart().getXYPlot();
		
		
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.numColumns = 2;
		
		Label lblDataset = new Label(container, SWT.NONE);
		lblDataset.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblDataset.setText("Dataset");
		
		Combo dataSetListCombo = new Combo(container, SWT.BORDER);
		dataSetListCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		for(int j = 0 ; j < plot.getDatasetCount() ; j++){
			for(int i = 0 ; i < plot.getDataset(j).getSeriesCount() ; i++){
				dataSetListCombo.add(plot.getDataset(j).getSeriesKey(i).toString());
			}
		}
		
		dataSetListCombo.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedSeries = dataSetListCombo.getText();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
			
		});
		
		Button btnLineVisible = new Button(container, SWT.CHECK);
		btnLineVisible.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnLineVisible.setText("With Mean line");
		btnLineVisible.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				withAverage = btnLineVisible.getSelection();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		return container;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Best Fit");
	}

	@Override
	protected Point getInitialSize() {
		return new Point(220, 160);
	}

	@Override
	protected void buttonPressed(int buttonId) {
		super.buttonPressed(buttonId);
		if(buttonId == IDialogConstants.OK_ID){
			
			int dataSetIndex = 0;
			int seriesIndex = 0;
			
			for(int j = 0 ; j < plot.getDatasetCount() ; j++){
				for(int i = 0 ; i < plot.getDataset(j).getSeriesCount() ; i++){
					if(selectedSeries.equals(plot.getDataset(j).getSeriesKey(i).toString())){
						dataSetIndex = j; seriesIndex = i;
					}
				}
			}
			
			System.out.println(dataSetIndex + ", " + seriesIndex);
			
			dataset = (XYSeriesCollection) plot.getDataset(dataSetIndex);
			
			double[] result = Regression.getOLSRegression(dataset, 0);
			double startX = dataset.getXValue(seriesIndex, 0);
			double endX = dataset.getXValue(seriesIndex, dataset.getItemCount(seriesIndex)-1);
			XYLineAnnotation bestFit = new XYLineAnnotation(startX, result[0], endX, endX*result[1] + result[0]);
			bestFit.setToolTipText("Best fit");
			plot.addAnnotation(bestFit);
			
			if(withAverage){
				XYSeries xys = dataset.getSeries(seriesIndex);
				double sum = 0;
				int count = xys.getItemCount();
				double avg = 0;
				
				for(int i = 0 ; i < count ; i++)
					sum += xys.getY(i).doubleValue();
				
				avg = sum/count;
				XYLineAnnotation meanLine = new XYLineAnnotation(startX, avg, endX, avg, new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
				        1.0f, new float[] {6.0f, 6.0f}, 0.0f), Color.black);
				meanLine.setToolTipText("Average");
				plot.addAnnotation(meanLine);
			}
		}
	}
}
