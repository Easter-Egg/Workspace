package utils;

import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorInputTransfer;

public class GraphEditorDropAdapter extends DropTargetAdapter {
	private IWorkbenchWindow window;
	
	public GraphEditorDropAdapter(IWorkbenchWindow window){
		this.window = window;
	}

	@Override
	public void dragEnter(DropTargetEvent event) {
		if (event.detail == DND.DROP_DEFAULT) {
			 if ((event.operations & DND.DROP_COPY) != 0) {
				 event.detail = DND.DROP_COPY;
			 }
			 else {
				 event.detail = DND.DROP_NONE;
			 }
		 }
	}

	@Override
	public void dragLeave(DropTargetEvent event) {
		System.out.println(">>>>>>>> Drag Leave");
	}

	@Override
	public void dragOver(DropTargetEvent event) {
		event.feedback = DND.FEEDBACK_NONE;
	}

	@Override
	public void drop(DropTargetEvent event) {
		System.out.println(">>>>>>>> Dropped");
		if(EditorInputTransfer.getInstance().isSupportedType(event.currentDataType)){
			EditorInputTransfer.EditorInputData[] editorInputs = (EditorInputTransfer.EditorInputData[]) event.data;
			IEditorInput input = editorInputs[0].input;
			String editorId = editorInputs[0].editorId; 
			try {
				window.getActivePage().openEditor(input, editorId);
			} catch (PartInitException e) {
				e.printStackTrace();
			}
			event.detail = DND.DROP_COPY;
		}
	}
}
