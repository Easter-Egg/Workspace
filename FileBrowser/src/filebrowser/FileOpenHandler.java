package filebrowser;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.StatusLineContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.FileStoreEditorInput;

public class FileOpenHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		FileDialog fd = new FileDialog(page.getActivePart().getSite()
				.getShell(), SWT.OPEN);
		fd.setText("Open File");
		
		String fileName = fd.open();
	
		File file = new File(fileName);
		IPath ipath = new Path(file.getAbsolutePath());
		IFileStore fs = EFS.getLocalFileSystem().getStore(ipath);
		FileStoreEditorInput fileStoreEditorInput = new FileStoreEditorInput(fs);
		try {

			if (file.getName().endsWith(".txt")) {
				page.openEditor(fileStoreEditorInput, MyTextEditor.ID, false);
			}

			if ((file.getName().endsWith(".jpg") || file.getName().endsWith(".png"))) {
				page.openEditor(fileStoreEditorInput, ImageEditor.ID, false).getAdapter(OutlineView.class);
			}
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		
		IWorkbenchPartSite site = page.getActivePart().getSite();
		IActionBars bars = null;
		
		if(site instanceof IViewSite)
			bars = ((IViewSite) page.getActivePart().getSite()).getActionBars();
		
		else if(site instanceof IEditorSite)
			bars = ((IEditorSite) page.getActivePart().getSite()).getActionBars();
		
		bars.getStatusLineManager().setMessage(file.getName() + " is opened.");
		StatusLineContributionItem statusBars = ((StatusLineContributionItem) bars.getStatusLineManager().find("Size"));
		statusBars.setText(file.length() + " Bytes");
		
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd, hh:mm:ss:SSS a"); 
		System.out.println(sdf.format(dt).toString() +  " [" + fileName + "]");
		
		return null;
	}
}
