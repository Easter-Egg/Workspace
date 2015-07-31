package utils;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.menus.WorkbenchWindowControlContribution;

public class ControlConnectionRouter extends
		WorkbenchWindowControlContribution {

	@Override
	protected Control createControl(Composite parent) {
		Combo c = new Combo(parent, SWT.READ_ONLY);
		c.add("Nomal");
		c.add("Manhattan");
		c.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent event) {
				if(c.getSelectionIndex() == 0){
					Command command = ((ICommandService) getWorkbenchWindow().getActivePage().getActiveEditor().getEditorSite().getService(ICommandService.class)).getCommand("FileBrowser.command.nomalConnectionRouter");
					final Event trigger = new Event();
					ExecutionEvent executionEvent = ((IHandlerService) getWorkbenchWindow().getActivePage().getActiveEditor().getEditorSite().getService(IHandlerService.class)).createExecutionEvent(command, trigger);
					try {
						command.executeWithChecks(executionEvent);
					} catch (ExecutionException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (NotDefinedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (NotEnabledException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (NotHandledException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else if (c.getSelectionIndex() == 1){
					Command command = ((ICommandService) getWorkbenchWindow().getActivePage().getActiveEditor().getEditorSite().getService(ICommandService.class)).getCommand("FileBrowser.command.manhattanConnectionRouter");
					final Event trigger = new Event();
					ExecutionEvent executionEvent = ((IHandlerService) getWorkbenchWindow().getActivePage().getActiveEditor().getEditorSite().getService(IHandlerService.class)).createExecutionEvent(command, trigger);
					try {
						command.executeWithChecks(executionEvent);
					} catch (ExecutionException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (NotDefinedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (NotEnabledException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (NotHandledException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
			}
		});
		return null;
	}


}
