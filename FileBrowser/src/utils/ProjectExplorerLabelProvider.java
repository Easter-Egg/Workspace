package utils;

import org.eclipse.jface.viewers.LabelProvider;

public class ProjectExplorerLabelProvider extends LabelProvider{

	@Override
	public String getText(Object element) {
		ParentBean bean = (ParentBean) element;
		return bean.getName();
	}
}
