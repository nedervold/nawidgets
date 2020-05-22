package org.nedervold.nawidgets;

import javax.swing.SwingUtilities;

public class App {

	public static void main(final String[] args) {
		SwingUtilities.invokeLater(() -> {
			new DemoDisplayFrame();
			new DemoEditorFrame();
			new DemoUtilsFrame();
			new Tmp();
		});
	}
}
