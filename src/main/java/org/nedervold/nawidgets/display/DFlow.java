package org.nedervold.nawidgets.display;

import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;

import nz.sodium.Cell;

public class DFlow<C extends JComponent> extends JPanel {

	public static class Impl<C extends JComponent> extends DWidgetImpl<DFlow<C>, List<C>> {

		public Impl(final DFlow<C> component, final Cell<List<C>> inputCell) {
			super(component, inputCell);
		}

		@Override
		public void setComponentValue(final List<C> value) {
			component.removeAll();
			for (final C comp : value) {
				component.add(comp);
			}
			// TODO These two lines were added because they seem to work but I don't fully
			// understand the mechanism underlying them. Research it and document what's
			// supposed to be done and why.
			component.revalidate();
			component.repaint();
		}
	}

	private final Impl<C> impl;

	public DFlow(final Cell<List<C>> inputCell) {
		super(new FlowLayout(), true);
		impl = new Impl<>(this, inputCell);
	}

	@Override
	public void removeNotify() {
		impl.unlisten();
		super.removeNotify();
	}
}
