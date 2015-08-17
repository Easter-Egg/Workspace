package handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import utils.SelectSeriesForAxesDialog;

public class SelectSeriesForAxisHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		SelectSeriesForAxesDialog ssad = new SelectSeriesForAxesDialog(page.getActiveEditor().getEditorSite().getShell());
		ssad.open();
		return null;
	}
}
