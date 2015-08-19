package utils;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import filebrowser.Activator;

public class CustomPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage{

	public CustomPreferencePage() {
		super(GRID);
	}
	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	    setDescription("A demonstration of a preference page implementation");		
	}

	@Override
	protected void createFieldEditors() {
		addField(new DirectoryFieldEditor("PATH", "&Directory preference:", getFieldEditorParent()));
		
		addField(new BooleanFieldEditor("BOOLEAN_VALUE", "&Boolean preference", getFieldEditorParent()));

	    addField(new RadioGroupFieldEditor("CHOICE", "A multiple-choice preference", 1,
		        new String[][] { { "&Choice 1", "choice1" },
	            { "C&hoice 2", "choice2" } }, getFieldEditorParent()));
	    addField(new StringFieldEditor("MySTRING1", "A &text preference:", getFieldEditorParent()));
	    addField(new StringFieldEditor("MySTRING2", "A &text preference:",  getFieldEditorParent()));
	}

}
