package handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import utils.ChartStyleChangeDialog;

public class ChartStyleChangeHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		ChartStyleChangeDialog cscd = new ChartStyleChangeDialog(page.getActiveEditor().getEditorSite().getShell());
		cscd.open();
		return null;
	}
}
