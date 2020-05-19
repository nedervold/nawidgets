package org.nedervold.nawidgets.display;

import javax.swing.JProgressBar;

import nz.sodium.Cell;

public class DProgressBar extends JProgressBar {

	public static class Impl extends DWidgetImpl<DProgressBar, Integer> {
		public Impl(final DProgressBar component, final Cell<Integer> inputCell) {
			super(component, inputCell);
		}

		@Override
		public void setComponentValue(final Integer value) {
			component.setValue(value);

		}
	}

	private final Impl impl;

	public DProgressBar(final int orient, final int min, final int max, final Cell<Integer> inputCell) {
		super(orient, min, max);
		impl = new Impl(this, inputCell);
	}

	@Override
	public void removeNotify() {
		impl.unlisten();
		super.removeNotify();
	}

}
