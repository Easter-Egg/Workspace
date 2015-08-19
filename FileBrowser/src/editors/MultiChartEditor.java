package editors;

import java.io.File;
import java.net.URI;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;

import utils.ChartCompositeWithKeyListener;

public class MultiChartEditor extends MultiPageEditorPart{
	public final static String ID = "FileBrowser.MultiChartEditor";
	private ChartEditor firstChartEditor;
	private File file;
	private ChartCompositeWithKeyListener cc;
	
	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return super.isDirty();
	}

	@Override
	protected void createPages() {
		IEditorInput editorInput = getEditorInput();
		FileStoreEditorInput fsInput = (FileStoreEditorInput) editorInput;
		URI uri = fsInput.getURI();
		
		file = new File(uri);
		firstChartEditor = new ChartEditor();
		
		try {
			addPage(firstChartEditor, editorInput);
			addPage(createChartPage());
			//addPage(createChartPage());
			//addPage(createChartPage());
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		setPartName(file.getName());	
		setPageText(0, file.getName());
		setPageText(1, "Series 1");
		//setPageText(2, "Series 2");
		//setPageText(3, "Series 3");
		
		
		addPageChangedListener(new IPageChangedListener() {
			
			@Override
			public void pageChanged(PageChangedEvent event) {
				// TODO Auto-generated method stub
				
				if(getActivePage() == 0){
					System.out.println(getActivePage());
					for(int i = 0 ; i <firstChartEditor.getXYGraph().getChart().getXYPlot().getSeriesCount() ; i++)
						firstChartEditor.getXYGraph().getChart().getXYPlot().getRenderer().setSeriesVisible(i, true);
				}
				
				else if(getActivePage() == 1){
					System.out.println(getActivePage());
					firstChartEditor.getXYGraph().getChart().getXYPlot().getRenderer().setSeriesVisible(1, false);
			        firstChartEditor.getXYGraph().getChart().getXYPlot().getRenderer().setSeriesVisible(2, false);
			        cc.setChart(firstChartEditor.getXYGraph().getChart());
				}
			}
		});

	}
	
	private Control createChartPage(){
		cc = new ChartCompositeWithKeyListener(getContainer(), SWT.NONE, null, true);
		cc.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		return cc;
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		
	}

	@Override
	public void doSaveAs() {
		
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

}
