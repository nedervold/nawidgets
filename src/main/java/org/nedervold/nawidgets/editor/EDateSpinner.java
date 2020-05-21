package org.nedervold.nawidgets.editor;

import java.util.Date;

import javax.swing.SpinnerDateModel;

import nz.sodium.Stream;

public class EDateSpinner extends ESpinner<Date> {
	public EDateSpinner(final Date minValue, final Date maxValue, final int stepSize, final Stream<Date> inputStream,
			final Date initValue) {
		super(new SpinnerDateModel(initValue, minValue, maxValue, stepSize), inputStream, initValue);
	}
}
