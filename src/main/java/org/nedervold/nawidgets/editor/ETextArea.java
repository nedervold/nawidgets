package org.nedervold.nawidgets.editor;

import javax.swing.JTextArea;

import nz.sodium.Cell;
import nz.sodium.Stream;

public class ETextArea extends JTextArea implements Editor<String> {

	private final ETextComponentImpl<ETextArea> impl;

	public ETextArea(final Stream<String> inputStream, final String initText, final int height, final int width) {
		super(initText, height, width);
		impl = new ETextComponentImpl<>(this, inputStream, initText);
	}

	@Override
	public Cell<String> outputCell() {
		return impl.outputCell;
	}

	public void unlisten() {
		impl.unlisten();
	}
}
