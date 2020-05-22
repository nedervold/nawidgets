package org.nedervold.nawidgets.editor;

import javax.swing.JSpinner;
import javax.swing.SpinnerModel;

import nz.sodium.Cell;
import nz.sodium.Stream;

public abstract class EAbstractSpinner<V> extends JSpinner implements Editor<V> {
	protected ESpinnerImpl<V> impl;

	protected EAbstractSpinner(final SpinnerModel spinnerModel, final Stream<V> inputStream, final V initValue) {
		super(spinnerModel);
		impl = new ESpinnerImpl<>(this, inputStream, initValue);
	}

	@Override
	public Cell<V> outputCell() {
		return impl.outputCell;
	}

	@Override
	public void removeNotify() {
		impl.unlisten();
		super.removeNotify();
	}
}
