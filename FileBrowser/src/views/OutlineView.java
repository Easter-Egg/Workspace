package views;

import java.io.File;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.MessagePage;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.PageBookView;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;

import editors.GraphEditor;
import editors.ImageEditor;
import editors.MyTextEditor;

public class OutlineView extends PageBookView {
	public OutlineView() {
	}
	public static final String ID = "FileBrowser.outlineView";
	private IEditorInput ei;
	private FileStoreEditorInput fsei;
	private MessagePage messagePage;

	@Override
	protected IPage createDefaultPage(PageBook book) {
		// TODO Auto-generated method stub
		messagePage = new MessagePage();
		initPage(messagePage);
		messagePage.setMessage("");
		messagePage.createControl(book);
		return messagePage;
	}

	@Override
	protected PageRec doCreatePage(IWorkbenchPart part) {
		messagePage = new MessagePage();
		initPage(messagePage);
		
		ei = ((IEditorPart) part).getEditorInput();
		fsei = (FileStoreEditorInput) ei;
		File file = new File(fsei.getURI().getPath());
		
		if(part instanceof GraphEditor){
			GraphEditor ge = (GraphEditor) part;
			ge.getGraph().addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					if(e.item instanceof GraphNode){
						GraphNode selectedNode = (GraphNode) e.item;
						if(!selectedNode.getTargetConnections().isEmpty()){
							GraphConnection gc = (GraphConnection) selectedNode.getTargetConnections().get(0);
							GraphNode srcOfSelectedNode = (GraphNode) gc.getSource();
							messagePage.setMessage("파일명 : " + selectedNode.getText() + "\n상위폴더 : " + srcOfSelectedNode.getText());
						}
						else {
							messagePage.setMessage("파일명 : " + selectedNode.getText() + "\n상위폴더 : " + file.getParent());
						}
					}
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
			messagePage.setMessage("파일명 : " + file.getName() + "\n상위폴더 : " + file.getParent());
			messagePage.createControl(getPageBook());
			return new PageRec(part, messagePage);
		}
		
		else{
			messagePage.setMessage("파일명 : " + file.getName() + "\n파일크기 : " + file.length() + " Bytes");
			messagePage.createControl(getPageBook());
			return new PageRec(part, messagePage);
		}
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
		
		return (part instanceof MyTextEditor || part instanceof ImageEditor || part instanceof GraphEditor);
	}
	
	public void partBroughtToTop(IWorkbenchPart part) {
	     partActivated(part);
	}

	public MessagePage getMessagePage(){
		return messagePage;
	}
}
