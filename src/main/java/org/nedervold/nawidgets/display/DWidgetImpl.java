package org.nedervold.nawidgets.display;

import javax.swing.SwingUtilities;

import nz.sodium.Cell;
import nz.sodium.Listener;
import nz.sodium.Operational;
import nz.sodium.Stream;

public abstract class DWidgetImpl<S, V> {
	public final S component;

	public final Stream<V> inputStream;

	public final Listener listener;

	public DWidgetImpl(final S component, final Cell<V> inputCell) {
		this.component = component;
		this.inputStream = Operational.updates(inputCell);
		listener = inputStream.listen(value -> {
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
		listener.unlisten();
	}
}
