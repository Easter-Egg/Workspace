package editors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.TextPresentation;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.menus.IMenuService;
import org.eclipse.ui.part.EditorPart;

public class MyTextEditor extends EditorPart {
	public static final String ID = "FileBrowser.MyTextEditor";
	private TextViewer textViewer;
	private int firstLineLength = -1;

	public MyTextEditor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setInput(input);
		setSite(site);
		setPartName(input.getName());
	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		GridLayout layout = new GridLayout(1, false);
		parent.setLayout(layout);
		
		ToolBar toolbar = new ToolBar(parent, SWT.FLAT | SWT.HORIZONTAL);
		toolbar.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		ToolBarManager toolbarman = new ToolBarManager(toolbar);
		IMenuService menuService = (IMenuService) getSite().getService(IMenuService.class);
		menuService.populateContributionManager(toolbarman, "toolbar:FileBrowser.MyTextEditor");
		
		
		textViewer = new TextViewer(parent, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.None);
		textViewer.getTextWidget().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		StyledText styledText = textViewer.getTextWidget();
		
		String content = readFileContents();
		Document document = new Document(content);
		textViewer.setDocument(document);

		if(firstLineLength > 0){
			TextPresentation style = new TextPresentation();
			Color red = new Color(null, 255, 0, 0);
			style.addStyleRange(new StyleRange(0, firstLineLength, red, null, SWT.BOLD));
			textViewer.changeTextPresentation(style, true);
		}
	}

	private String readFileContents() {
		FileStoreEditorInput fsInput = (FileStoreEditorInput)getEditorInput();
		URI uri = fsInput.getURI();
		File file = new File(uri);
		StringBuffer buffer = new StringBuffer();
		String line = "";
		try( BufferedReader reader = new BufferedReader(new FileReader(file)); )
		{
			while((line = reader.readLine()) != null){
				buffer.append(line + "\n");
				if(firstLineLength < 0)
					firstLineLength = line.length();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}

	@Override
	public void setFocus() {
		textViewer.getControl().setFocus();
	}

}
