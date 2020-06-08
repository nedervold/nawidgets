package org.nedervold.nawidgets.display;

import java.util.List;

import javax.swing.Box;
import javax.swing.JComponent;

import nz.sodium.Cell;

public class DBox<C extends JComponent> extends Box {

	public static class Impl<C extends JComponent> extends DWidgetImpl<DBox<C>, List<C>> {

		public Impl(final DBox<C> component, final Cell<List<C>> inputCell) {
			super(component, inputCell);
		}

		@Override
		public void setComponentValue(final List<C> value) {
			component.removeAll();
			for (final C comp : value) {
				component.add(comp);
				// TODO These two lines were added because they seem to work but I don't fully
				// understand the mechanism underlying them. Research it and document what's
				// supposed to be done and why.
				comp.revalidate();
				comp.repaint();
			}
			// TODO These two lines were added because they seem to work but I don't fully
			// understand the mechanism underlying them. Research it and document what's
			// supposed to be done and why.
			component.revalidate();
			component.repaint();
		}
	}

	private final Impl<C> impl;

	public DBox(final int orientation, final Cell<List<C>> inputCell) {
		super(orientation);
		impl = new Impl<>(this, inputCell);
	}

	public void unlisten() {
		impl.unlisten();
	}
}
