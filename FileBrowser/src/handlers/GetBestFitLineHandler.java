package handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import utils.GetBestFitDialog;

public class GetBestFitLineHandler extends AbstractHandler{

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		GetBestFitDialog gbfd = new GetBestFitDialog(page.getActiveEditor().getEditorSite().getShell());
		gbfd.open();
		return null;
	}

}
