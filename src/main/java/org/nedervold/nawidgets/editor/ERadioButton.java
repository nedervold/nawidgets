package org.nedervold.nawidgets.editor;

import javax.swing.JRadioButton;

import nz.sodium.Cell;
import nz.sodium.Stream;

public class ERadioButton extends JRadioButton {
	final EAbstractButtonImpl<ERadioButton> impl;

	public ERadioButton(final String label, final Stream<Boolean> inputStream, final Boolean initVal) {
		super(label, initVal);
		impl = new EAbstractButtonImpl<>(this, inputStream, initVal);
	}

	@Override
	public void removeNotify() {
		impl.unlisten();
		super.removeNotify();
	}

	public Cell<Boolean> value() {
		return impl.outputCell;
	}
}
