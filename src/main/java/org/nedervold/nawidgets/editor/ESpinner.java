package org.nedervold.nawidgets.editor;

import javax.swing.JSpinner;
import javax.swing.SpinnerModel;

import nz.sodium.Cell;
import nz.sodium.Stream;

public abstract class ESpinner<V> extends JSpinner {
	protected ESpinnerImpl<V> impl;

	protected ESpinner(final SpinnerModel spinnerModel, final Stream<V> inputStream, final V initValue) {
		super(spinnerModel);
		impl = new ESpinnerImpl<>(this, inputStream, initValue);
	}

	@Override
	public void removeNotify() {
		impl.unlisten();
		super.removeNotify();
	}

	public Cell<V> value() {
		return impl.outputCell;
	}
}
