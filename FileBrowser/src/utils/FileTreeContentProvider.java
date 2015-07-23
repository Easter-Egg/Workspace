package utils;

import java.io.File;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class FileTreeContentProvider implements ITreeContentProvider {

	@Override
	public void dispose() {

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return (File[]) inputElement; 					
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		File file = (File) parentElement;
		return file.listFiles(); 						
	}

	@Override
	public Object getParent(Object element) {
		File file = (File) element;
		return file.getParentFile(); 					
	}

	@Override
	public boolean hasChildren(Object element) {
		File file = (File) element;
		
		if (file.isDirectory())							
			return true;
		
		else
			return false;
	}
}
