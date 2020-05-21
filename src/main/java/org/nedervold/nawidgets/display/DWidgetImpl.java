package org.nedervold.nawidgets.display;

import javax.swing.SwingUtilities;

import nz.sodium.Cell;
import nz.sodium.Listener;
import nz.sodium.Operational;

public abstract class DWidgetImpl<S, V> {
	protected final S component;

	private final Listener sodiumListener;

	public DWidgetImpl(final S component, final Cell<V> inputCell) {
		this.component = component;
		sodiumListener = Operational.updates(inputCell).listen(value -> {
			SwingUtilities.invokeLater(() -> {
				setComponentValue(value);
			});
		});

		SwingUtilities.invokeLater(() -> {
			setComponentValue(inputCell.sample());
		});
	}

	public abstract void setComponentValue(V value);

	public void unlisten() {
		sodiumListener.unlisten();
	}
}
