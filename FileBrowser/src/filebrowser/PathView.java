package filebrowser;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

public class PathView extends ViewPart {
	public PathView() {
	}

	public static final String ID = "FileBrowser.pathView";
	private Text textBox;

	private ISelectionListener listener = new ISelectionListener() {
		public void selectionChanged(IWorkbenchPart part, ISelection sel) {
			if (!(sel instanceof IStructuredSelection))
				return;
			
			IStructuredSelection ss = (IStructuredSelection) sel;
			Object firstElement = ss.getFirstElement();
			if(firstElement == null)
				return;
			String path = firstElement.toString();

			textBox.setText(path);
		}
	};

	@Override
	public void createPartControl(Composite parent) {
		textBox = new Text(parent, SWT.MULTI | SWT.BORDER | SWT.WRAP);
		textBox.setLayoutData(new GridData(GridData.FILL_BOTH));

		getSite().getPage().addSelectionListener(listener);
	}

	@Override
	public void setFocus() {

	}
	
	@Override
	public void dispose() {
		getSite().getPage().removeSelectionListener(listener);
	}
}