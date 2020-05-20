package org.nedervold.nawidgets.editor;

import javax.swing.JTextField;

import nz.sodium.Cell;
import nz.sodium.Stream;

public class ETextField extends JTextField {

	private final ETextComponentImpl<ETextField> impl;

	public ETextField(final Stream<String> inputStream, final String initText, final int width) {
		super(initText, width);
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
