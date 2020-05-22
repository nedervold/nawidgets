package org.nedervold.nawidgets.editor;

import javax.swing.SpinnerNumberModel;

import nz.sodium.Stream;

public class EIntegerSpinner extends EAbstractSpinner<Integer> {
	public EIntegerSpinner(final int minValue, final int maxValue, final int stepSize,
			final Stream<Integer> inputStream, final int initValue) {
		super(new SpinnerNumberModel(initValue, minValue, maxValue, stepSize), inputStream, initValue);
	}
}
