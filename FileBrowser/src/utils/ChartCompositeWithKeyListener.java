package utils;

import java.io.File;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.experimental.chart.swt.ChartComposite;

public class ChartCompositeWithKeyListener extends ChartComposite implements KeyListener
{	
	private MouseWheelListener mouseWheelListener = new MouseWheelListener(){

		@Override
		public void mouseScrolled(MouseEvent e) {
			if (( e.stateMask & SWT.CTRL ) == 0)
                    return; 
			 
			if (e.count > 0) {
				ChartCompositeWithKeyListener.this.zoomInBoth(e.x, e.y);
			} else if (e.count < 0) {
				ChartCompositeWithKeyListener.this.zoomOutBoth(e.x, e.y);
			}
		}		
	};
	
	public ChartCompositeWithKeyListener(Composite comp, int style, JFreeChart chart, boolean useBuffer){
        super(comp, style, chart, useBuffer);
        addSWTListener(this);
       // this.setDomainZoomable(false);
       // this.setRangeZoomable(false);
        this.addMouseWheelListener(mouseWheelListener);
    }   

    @Override
    public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
    	if((e.stateMask & SWT.CONTROL) == 0)
    		return;
    	
    	if((e.keyCode == 's')){
    		System.out.println("Open file dialog for save chart to PNG");
    		final FileDialog dialog = new FileDialog(Display.getDefault().getShells()[0], SWT.SAVE);
			dialog.setFilterNames(new String[] { "PNG Files", "All Files (*.*)" });
			dialog.setFilterExtensions(new String[] { "*.png", "*.*" });
			final String path = dialog.open();
			if ((path != null) && !path.equals("")) {
				File img = new File(path);
				try {
					ChartUtilities.saveChartAsPNG(img, this.getChart(), this.getSize().x, this.getSize().y);
					System.out.println("Image(PNG) saved at " + path);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
    	}
    	if((e.keyCode == 'r')){
    		System.out.println("Clear Zoom Factor");
    		restoreAutoDomainBounds();
    		restoreAutoRangeBounds();
    	}
    }

    @Override
    public void keyReleased(org.eclipse.swt.events.KeyEvent e) {
    	
    }
    
    @Override
    public void dispose(){
		this.removeMouseWheelListener(mouseWheelListener);
    }
}