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

	public DTextArea(final int rows, final int columns, final Cell<String> inputCell) {
		super(inputCell.sample(), rows, columns);
		impl = new Impl(this, inputCell);
		setEditable(false);
	}

	public void unlisten() {
		impl.unlisten();
	}

}
