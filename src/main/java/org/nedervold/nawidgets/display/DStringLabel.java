package org.nedervold.nawidgets.display;

import javax.swing.JLabel;

import nz.sodium.Cell;

public class DStringLabel extends JLabel {
	public static class Impl extends DWidgetImpl<DStringLabel, String> {

		public Impl(final DStringLabel component, final Cell<String> inputCell) {
			super(component, inputCell);
		}

		@Override
		public void setComponentValue(final String value) {
			component.setText(value);
		}
	}

	public final Impl impl;

	public DStringLabel(final Cell<String> inputCell) {
		super(inputCell.sample());
		impl = new Impl(this, inputCell);
	}

	@Override
	public void removeNotify() {
		impl.unlisten();
		super.removeNotify();
	}
}
