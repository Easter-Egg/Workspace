package handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import utils.TickUnitChangeDialog;

public class TickUnitChangeHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		TickUnitChangeDialog tucd = new TickUnitChangeDialog(page.getActiveEditor().getEditorSite().getShell());
		tucd.open();
		return null;
	}
}
