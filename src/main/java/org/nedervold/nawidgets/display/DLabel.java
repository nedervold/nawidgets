package org.nedervold.nawidgets.display;

import javax.swing.JLabel;

import nz.sodium.Cell;

public class DLabel extends JLabel {
	public static class Impl extends DWidgetImpl<DLabel, String> {

		public Impl(final DLabel component, final Cell<String> inputCell) {
			super(component, inputCell);
		}

		@Override
		public void setComponentValue(final String value) {
			component.setText(value);
		}
	}

	private final Impl impl;

	public DLabel(final Cell<String> inputCell) {
		super(inputCell.sample());
		impl = new Impl(this, inputCell);
	}

	@Override
	public void removeNotify() {
		impl.unlisten();
		super.removeNotify();
	}
}
