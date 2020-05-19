package org.nedervold.nawidgets.display;

import javax.swing.JTextArea;

import nz.sodium.Cell;

public class DTextArea extends JTextArea {
	public static class Impl extends DWidgetImpl<DTextArea, String> {

		public Impl(final DTextArea component, final Cell<String> inputCell) {
			super(component, inputCell);
		}

		@Override
		public void setComponentValue(final String value) {
			component.setText(value);
		}
	}

	private final Impl impl;

	public DTextArea(int rows, int columns, Cell<String> inputCell) {
		super(inputCell.sample(), rows, columns);
		impl = new Impl(this, inputCell);
		setEditable(false);
	}

	@Override
	public void removeNotify() {
		impl.unlisten();
		super.removeNotify();
	}

}
