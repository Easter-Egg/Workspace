package utils;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Shape;
import java.awt.Stroke;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
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
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.util.ShapeUtilities;

import editors.ChartEditor;
import org.eclipse.swt.widgets.Button;

public class ChartStyleChangeDialog extends Dialog {

	private int seriesSelectedIndex;
	private RGB colorValue;
	private IWorkbenchPage page;
	private ChartEditor ce;
	private XYPlot plot;
	private XYLineAndShapeRenderer renderer;
	private Stroke stroke;
	private Color newColor;
	private Shape shape;
	private boolean lineVisible;
	
	public ChartStyleChangeDialog(Shell parent) {
		super(parent);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		ce = (ChartEditor) page.getActiveEditor();
		plot = ce.getXYGraph().getChart().getXYPlot();
		seriesSelectedIndex = 0;
		
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.numColumns = 2;
		
		Label lblDataset = new Label(container, SWT.NONE);
		lblDataset.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblDataset.setText("Dataset");
		
		Combo dataSetListCombo = new Combo(container, SWT.BORDER);
		dataSetListCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		for(int i = 0 ; i < ce.getXYGraph().getChart().getXYPlot().getDatasetCount() ; i++){
			dataSetListCombo.add(ce.getXYGraph().getChart().getXYPlot().getLegendItems().get(i).getLabel());
		}
		
		
		dataSetListCombo.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetSelected(SelectionEvent e) {
				seriesSelectedIndex = dataSetListCombo.getSelectionIndex();
				renderer = (XYLineAndShapeRenderer) plot.getRenderer(seriesSelectedIndex);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
			
		});
		
		Label lblLine = new Label(container, SWT.NONE);
		lblLine.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblLine.setText("Color");
		
		final ColorSelector colorSelector = new ColorSelector(container);
		
		colorSelector.getButton().addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {

                System.out.println(" selected color :: " + colorSelector.getColorValue());
                colorValue = colorSelector.getColorValue();

            }
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {

            }
        });
		
		Label lblLine_1 = new Label(container, SWT.NONE);
		lblLine_1.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblLine_1.setText("Line");
		
		Combo LineCombo = new Combo(container, SWT.NONE);
		LineCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		LineCombo.add("Solid");
		LineCombo.add("Dashed");
		
		LineCombo.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetSelected(SelectionEvent e) {
				if(LineCombo.getSelectionIndex() == 0){
					stroke = new BasicStroke(2.0f);
				}
				else if(LineCombo.getSelectionIndex() == 1){
					stroke = new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
					        1.0f, new float[] {6.0f, 6.0f}, 0.0f);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
			
		});
		
		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Shape");
		
		Combo ShapeCombo = new Combo(container, SWT.NONE);
		ShapeCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		ShapeCombo.add("Cross");
		ShapeCombo.add("Diamond");
		ShapeCombo.add("Triangle");
		
		Button btnLineVisible = new Button(container, SWT.CHECK);
		btnLineVisible.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnLineVisible.setText("Line Visible");
		btnLineVisible.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				lineVisible = btnLineVisible.getSelection();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		new Label(container, SWT.NONE);
		
		ShapeCombo.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetSelected(SelectionEvent e) {
				if(ShapeCombo.getSelectionIndex() == 0)
					shape = ShapeUtilities.createDiagonalCross(3, 1);
				
				else if(ShapeCombo.getSelectionIndex() == 1)
					 shape = ShapeUtilities.createDiamond(5);
				
				else if(ShapeCombo.getSelectionIndex() == 2)
					shape = ShapeUtilities.createUpTriangle(5);
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
		newShell.setText("Style Change");
	}

	@Override
	protected Point getInitialSize() {
		return new Point(220, 350);
	}

	@Override
	protected void buttonPressed(int buttonId) {
		super.buttonPressed(buttonId);
		if(buttonId == IDialogConstants.OK_ID){
			newColor = new Color(colorValue.red, colorValue.green, colorValue.blue);
			renderer.setSeriesPaint(0, newColor);
			renderer.setSeriesStroke(0, stroke);
			renderer.setSeriesShape(0, shape);
			if(lineVisible)
				renderer.setSeriesLinesVisible(0, true);
		}
	}
}
