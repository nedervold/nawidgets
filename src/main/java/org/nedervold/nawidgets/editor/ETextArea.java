package org.nedervold.nawidgets.editor;

import javax.swing.JTextArea;

import nz.sodium.Cell;
import nz.sodium.Stream;

public class ETextArea extends JTextArea {

	private final ETextComponentImpl<ETextArea> impl;

	public ETextArea(final Stream<String> inputStream, final String initText, final int height, final int width) {
		super(initText, height, width);
		impl = new ETextComponentImpl<>(this, inputStream, initText);
	}

	@Override
	public void removeNotify() {
		impl.unlisten();
		super.removeNotify();
	}

	public Cell<String> value() {
		return impl.outputCell;
	}
}
