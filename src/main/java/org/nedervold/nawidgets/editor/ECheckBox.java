package org.nedervold.nawidgets.editor;

import javax.swing.JCheckBox;

import nz.sodium.Cell;
import nz.sodium.Stream;

public class ECheckBox extends JCheckBox {

	final EAbstractButtonImpl<ECheckBox> impl;

	public ECheckBox(final String label, final Stream<Boolean> inputStream, final Boolean initVal) {
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
