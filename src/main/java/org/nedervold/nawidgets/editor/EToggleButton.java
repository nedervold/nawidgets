package org.nedervold.nawidgets.editor;

import javax.swing.JToggleButton;

import nz.sodium.Cell;
import nz.sodium.Stream;

public class EToggleButton extends JToggleButton {
	final EAbstractButtonImpl<EToggleButton> impl;

	public EToggleButton(final String label, final Stream<Boolean> inputStream, final Boolean initVal) {
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
