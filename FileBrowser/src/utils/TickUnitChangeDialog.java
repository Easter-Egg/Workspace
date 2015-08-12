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
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.XYPlot;

import editors.ChartEditor;

public class TickUnitChangeDialog extends Dialog {

	private IWorkbenchPage page;
	private ChartEditor ce;
	private XYPlot plot;
	private Text text;
	private Text text_1;
	private Double xTickUnit;
	private Double yTickUnit;
	
	public TickUnitChangeDialog(Shell parent) {
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
		
		Label lblXTick = new Label(container, SWT.NONE);
		lblXTick.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1));
		lblXTick.setText("X Tick (Defalut = 100)");
		
		text = new Text(container, SWT.BORDER);
		text.setText("100");
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		text.addSegmentListener(new SegmentListener(){

			@Override
			public void getSegments(SegmentEvent event) {
				xTickUnit = Double.parseDouble(text.getText());
			}
			
		});
		
		Label lblYYick = new Label(container, SWT.NONE);
		lblYYick.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1));
		lblYYick.setText("Y Tick (Defalut = 0.005)");
		
		text_1 = new Text(container, SWT.BORDER);
		text_1.setText("0.005");
		text_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		text_1.addSegmentListener(new SegmentListener(){

			@Override
			public void getSegments(SegmentEvent event) {
				yTickUnit = Double.parseDouble(text_1.getText());
			}
			
		});
		
		
		
		return container;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Tick Unit");
	}

	@Override
	protected Point getInitialSize() {
		return new Point(220, 160);
	}

	@Override
	protected void buttonPressed(int buttonId) {
		super.buttonPressed(buttonId);
		if(buttonId == IDialogConstants.OK_ID){
			((NumberAxis) plot.getDomainAxis()).setTickUnit(new NumberTickUnit(xTickUnit));
			((NumberAxis) plot.getRangeAxis()).setTickUnit(new NumberTickUnit(yTickUnit));
		}
	}
}