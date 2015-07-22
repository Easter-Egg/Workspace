package filebrowser;

import java.io.File;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.MessagePage;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.PageBookView;

public class OutlineView extends PageBookView {
	public OutlineView() {
	}
	public static final String ID = "FileBrowser.outlineView";
	private IEditorInput ei;
	private FileStoreEditorInput fsei;

	@Override
	protected IPage createDefaultPage(PageBook book) {
		// TODO Auto-generated method stub
		MessagePage messagePage = new MessagePage();
		initPage(messagePage);
		messagePage.setMessage("");
		messagePage.createControl(book);
		return messagePage;
	}

	@Override
	protected PageRec doCreatePage(IWorkbenchPart part) {
		MessagePage messagePage = new MessagePage();
		initPage(messagePage);
		
		if(part instanceof IEditorPart){
			ei = ((IEditorPart) part).getEditorInput();
			fsei = (FileStoreEditorInput) ei;
		}
		File file = new File(fsei.getURI().getPath());
		messagePage.setMessage("파일명 : " + file.getName() + "\n파일크기 : " + file.length() + " Bytes");
		messagePage.createControl(getPageBook());
		return new PageRec(part, messagePage);
	}

	@Override
	protected void doDestroyPage(IWorkbenchPart part, PageRec pageRecord) {
		// TODO Auto-generated method stub
		pageRecord.page.dispose();
	}

	@Override
	protected IWorkbenchPart getBootstrapPart() {
		return null;
	}

	@Override
	protected boolean isImportant(IWorkbenchPart part) {
		// TODO Auto-generated method stub
		
		return (part instanceof MyTextEditor || part instanceof ImageEditor);
	}
	
	public void partBroughtToTop(IWorkbenchPart part) {
	     partActivated(part);
	}

}
