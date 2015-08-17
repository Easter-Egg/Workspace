package utils;


import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SegmentEvent;
import org.eclipse.swt.events.SegmentListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.jfree.chart.plot.XYPlot;

import editors.ChartEditor;

public class RangeSetDailog extends Dialog {

	private IWorkbenchPage page;
	private ChartEditor ce;
	private XYPlot plot;
	private Text text;
	private Text text_1;
	private Text text_2;
	private Text text_3;
	private double xAxisStart;
	private double xAxisEnd;
	private double yAxisStart;
	private double yAxisEnd;
	
	public RangeSetDailog(Shell parent) {
		super(parent);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		ce = (ChartEditor) page.getActiveEditor();
		plot = ce.getXYGraph().getChart().getXYPlot();
		
		xAxisStart = plot.getDomainAxis().getRange().getLowerBound();
		xAxisEnd = plot.getDomainAxis().getRange().getUpperBound();
		
		yAxisStart = plot.getRangeAxis().getRange().getLowerBound();
		yAxisEnd = plot.getRangeAxis().getRange().getUpperBound();
		
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.numColumns = 2;
		
		Label lblXAxis = new Label(container, SWT.NONE);
		lblXAxis.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblXAxis.setText("X Axis");
		new Label(container, SWT.NONE);
		
		Label lblFrom = new Label(container, SWT.NONE);
		lblFrom.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFrom.setText("From");
		
		text = new Text(container, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		text.setText("" + xAxisStart);
		text.addSegmentListener(new SegmentListener() {
			
			@Override
			public void getSegments(SegmentEvent event) {
				if(text.getText() != null)
					xAxisStart = Double.parseDouble(text.getText());
				
				else
					xAxisStart = plot.getDomainAxis().getRange().getLowerBound();
			}
		});
		
		Label lblTo = new Label(container, SWT.NONE);
		lblTo.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTo.setText("to");
		
		text_1 = new Text(container, SWT.BORDER);
		text_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		text_1.setText("" + xAxisEnd);
		text_1.addSegmentListener(new SegmentListener() {
			
			@Override
			public void getSegments(SegmentEvent event) {
				if(text_1.getText() != null)
					xAxisEnd = Double.parseDouble(text_1.getText());
				
				else
					xAxisEnd = plot.getDomainAxis().getRange().getUpperBound();
			}
		});
		
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
		Label lblYAxis = new Label(container, SWT.NONE);
		lblYAxis.setText("Y Axis");
		new Label(container, SWT.NONE);
		
		Label lblFrom_1 = new Label(container, SWT.NONE);
		lblFrom_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFrom_1.setText("From");
		
		text_2 = new Text(container, SWT.BORDER);
		text_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		text_2.setText("" + yAxisStart);
		text_2.addSegmentListener(new SegmentListener() {
			
			@Override
			public void getSegments(SegmentEvent event) {
				if(text_2.getText() != null)
					yAxisStart = Double.parseDouble(text_2.getText());
				
				else
					yAxisStart = plot.getRangeAxis().getRange().getLowerBound();
			}
		});
		
		Label lblTo_1 = new Label(container, SWT.NONE);
		lblTo_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTo_1.setText("to");
		
		text_3 = new Text(container, SWT.BORDER);
		text_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		text_3.setText("" + yAxisEnd);
		text_3.addSegmentListener(new SegmentListener() {
			
			@Override
			public void getSegments(SegmentEvent event) {
				if(text_3.getText() != null)
					yAxisEnd = Double.parseDouble(text_3.getText());
				
				else
					yAxisEnd = plot.getRangeAxis().getRange().getUpperBound(); 
			}
		});
		
		return container;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Range Setting");
	}

	@Override
	protected Point getInitialSize() {
		return new Point(220, 350);
	}

	@Override
	protected void buttonPressed(int buttonId) {
		super.buttonPressed(buttonId);
		if(buttonId == IDialogConstants.OK_ID){
			plot.getDomainAxis().setRange(xAxisStart, xAxisEnd);
			plot.getRangeAxis().setRange(yAxisStart, yAxisEnd);
		}
	}
}
