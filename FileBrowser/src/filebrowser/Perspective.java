package filebrowser;

import java.io.PrintStream;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

import views.BrowserView;
import views.PathView;
import views.GraphView;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		layout.setFixed(true);
		layout.addStandaloneView(BrowserView.ID, true, IPageLayout.LEFT, 0.2f, IPageLayout.ID_EDITOR_AREA);
		layout.addStandaloneView(PathView.ID, false, IPageLayout.TOP, 0.03f, BrowserView.ID);
		setConsole();
		layout.addStandaloneView(IConsoleConstants.ID_CONSOLE_VIEW, true, IPageLayout.BOTTOM, 0.8f,IPageLayout.ID_EDITOR_AREA);
		layout.addView("FileBrowser.outlineView", IPageLayout.RIGHT, 0.8f, IPageLayout.ID_EDITOR_AREA);
		//layout.addStandaloneView(GraphView.ID, true, IPageLayout.BOTTOM, 0.7f, IPageLayout.ID_EDITOR_AREA);
	}
	
	public void setConsole(){
		MessageConsole console = new MessageConsole("Console", null);
		ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[]{console});
		MessageConsoleStream stream = console.newMessageStream();
		PrintStream ps = new PrintStream(stream);
		System.setOut(ps);
	}
}
