package utils;


import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.jfree.chart.plot.XYPlot;

import editors.ChartEditor;

public class SelectSeriesForAxesDialog extends Dialog {

	private IWorkbenchPage page;
	private ChartEditor ce;
	private XYPlot plot;
	private String selectedSeriesForX;
	private String selectedSeriesForY;
	
	public SelectSeriesForAxesDialog(Shell parent) {
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
		lblDataset.setText("X Axis");
		
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
				selectedSeriesForX = dataSetListCombo.getText();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
			
		});
		
		Label lblLine = new Label(container, SWT.NONE);
		lblLine.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblLine.setText("Y Axis");
		
		Combo dataSetListCombo1 = new Combo(container, SWT.BORDER);
		dataSetListCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		for(int j = 0 ; j < plot.getDatasetCount() ; j++){
			for(int i = 0 ; i < plot.getDataset(j).getSeriesCount() ; i++){
				dataSetListCombo1.add(plot.getDataset(j).getSeriesKey(i).toString());
			}
		}
		
		
		dataSetListCombo1.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedSeriesForY = dataSetListCombo1.getText();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
			
		});
		
		
		return container;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Select Series for Axis");
	}

	@Override
	protected Point getInitialSize() {
		return new Point(220, 160);
	}

	@Override
	protected void buttonPressed(int buttonId) {
		super.buttonPressed(buttonId);
		if(buttonId == IDialogConstants.OK_ID){
			System.out.println(selectedSeriesForX + ", " + selectedSeriesForY);
			
			int dataSetIndex = 0;
			int seriesIndex = 0;
			
			for(int j = 0 ; j < plot.getDatasetCount() ; j++){
				for(int i = 0 ; i < plot.getDataset(j).getSeriesCount() ; i++){
					if(selectedSeriesForX.equals(plot.getDataset(j).getSeriesKey(i).toString())){
						dataSetIndex = j; seriesIndex = i;
					}
				}
			}
		}
	}
}
