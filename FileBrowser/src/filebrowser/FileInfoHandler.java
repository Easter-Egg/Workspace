package filebrowser;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

public class FileInfoHandler extends AbstractHandler {

	private Canvas srcCanvas;
	private Image srcImage;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		ImageEditor ie = (ImageEditor) page.getActiveEditor();
		srcCanvas = ((ImageEditor) page.getActiveEditor()).getCanvas();
		srcImage = ((ImageEditor) page.getActiveEditor()).getImage();

		if (srcImage == null)
			return null;

		
		System.out.println(">>>>> File name : " + ie.getTitle() + " width : " + srcImage.getBounds().width + ", height : " + srcImage.getBounds().height + " (canvas : " + srcCanvas.getClientArea().width + "x" + srcCanvas.getClientArea().height +")");
		return null;
	}
}