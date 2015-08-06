package utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteSeparator;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class PaletteViewCreator {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PaletteRoot createPaletteRoot() {
		PaletteRoot root = new PaletteRoot();
		PaletteGroup controls = new PaletteGroup("controls");
		root.add(controls);

		ToolEntry tool = new SelectionToolEntry();
		controls.add(tool);

		root.setDefaultEntry(tool);

		controls.add(new MarqueeToolEntry());

		PaletteSeparator separator = new PaletteSeparator("com.swtjface.flowchart.palette.separator");
		controls.add(separator);

		controls.add(new ConnectionCreationToolEntry("Connection", "Create a new Connection", null,
				AbstractUIPlugin.imageDescriptorFromPlugin("com.swtface.flowchart", "icons/realtions.gif"),
				AbstractUIPlugin.imageDescriptorFromPlugin("com.swtface.flowchart", "icons.connection.gif")));

		PaletteDrawer drawer = new PaletteDrawer("New Component",
				AbstractUIPlugin.imageDescriptorFromPlugin("com.swtface.flowchart", "icons.connection.gif"));
		List entries = new ArrayList();

		CombinedTemplateCreationEntry term = new CombinedTemplateCreationEntry("Terminator", "Start of End Component",
				null, AbstractUIPlugin.imageDescriptorFromPlugin("com.swtface.flowchart", "icons/tables.gif"),
				AbstractUIPlugin.imageDescriptorFromPlugin("com.swtface.flowchart", "icons/realtions.gif"));
		entries.add(term);

		drawer.addAll(entries);
		root.add(drawer);
		return root;
	}
}