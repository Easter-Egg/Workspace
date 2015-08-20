package editors;

import java.io.File;
import java.net.URI;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;

public class MultiChartEditor extends MultiPageEditorPart{
	public final static String ID = "FileBrowser.MultiChartEditor";
	private ChartEditor firstChartEditor;
	private File file;
	
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
			addPage(new ChartEditor(), editorInput);
			addPage(new ChartEditor(), editorInput);
			addPage(new ChartEditor(), editorInput);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		setPartName(file.getName());	
		setPageText(0, file.getName());
		setPageText(1, "Series 1");
		setPageText(2, "Series 2");
		setPageText(3, "Series 3");
		
		
		addPageChangedListener(new IPageChangedListener() {
			
			@Override
			public void pageChanged(PageChangedEvent event) {
				
				if(getActivePage() == 0){
					System.out.println(getActivePage());
				}
				
				else if(getActivePage() == 1){
					System.out.println(getActivePage());
					((ChartEditor) getActiveEditor()).getXYGraph().getChart().getXYPlot().getRenderer().setSeriesVisible(1, false);
					((ChartEditor) getActiveEditor()).getXYGraph().getChart().getXYPlot().getRenderer().setSeriesVisible(2, false);
				}
				
				else if(getActivePage() == 2){
					((ChartEditor) getActiveEditor()).getXYGraph().getChart().getXYPlot().getRenderer().setSeriesVisible(0, false);
					((ChartEditor) getActiveEditor()).getXYGraph().getChart().getXYPlot().getRenderer().setSeriesVisible(2, false);
				}
				
				else if(getActivePage() == 3){
					((ChartEditor) getActiveEditor()).getXYGraph().getChart().getXYPlot().getRenderer().setSeriesVisible(0, false);
					((ChartEditor) getActiveEditor()).getXYGraph().getChart().getXYPlot().getRenderer().setSeriesVisible(1, false);
				}
			}
		});

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
