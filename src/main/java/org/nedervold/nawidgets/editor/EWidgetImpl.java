package org.nedervold.nawidgets.editor;

import javax.swing.SwingUtilities;

import nz.sodium.Cell;
import nz.sodium.Listener;
import nz.sodium.Stream;
import nz.sodium.StreamSink;

/**
 * @param <S>
 *            The underlying Swing component
 * @param <V>
 *            The value type of this component
 * @param <M>
 *            The underlying Swing data model type; may be the component itself.
 * @param <L>
 *            The underlying Swing listener type.
 */
public abstract class EWidgetImpl<S, V, M, L> {
	protected final S component;

	protected final L swingListener;

	private final Listener sodiumListener;

	protected final M model;

	protected final Cell<V> outputCell;

	private final Cell<Boolean> userChangesAllowed;

	protected final StreamSink<V> userChangesSink;

	public EWidgetImpl(final S component, final Stream<V> inputStream, final V initValue) {
		this.component = component;

		// Set up userChangesAllowed.
		final StreamSink<Integer> decrementPending = new StreamSink<>();
		final Stream<Integer> incrementPending = inputStream.map(v -> 1);
		final Stream<Integer> pendingDeltas = incrementPending.orElse(decrementPending);
		final Cell<Integer> pendingExternalChanges = pendingDeltas.accum(0, (d, b) -> b + d);
		userChangesAllowed = pendingExternalChanges.map(b -> b == 0);

		// Set up userChanges and outputCell.
		this.userChangesSink = new StreamSink<>();
		this.outputCell = userChangesSink.gate(userChangesAllowed).orElse(inputStream).hold(initValue);

		this.swingListener = createSwingListener();
		this.model = getModel();
		addSwingListener();

		sodiumListener = inputStream.listen(value -> {
			SwingUtilities.invokeLater(() -> {
				removeSwingListener();
				setComponentValue(value);
				addSwingListener();
				decrementPending.send(-1);
			});
		});
	}

	public abstract void addSwingListener();

	public abstract L createSwingListener();

	public abstract V getComponentValue();

	public abstract M getModel();

	public abstract void removeSwingListener();

	public abstract void setComponentValue(V value);

	public void unlisten() {
		sodiumListener.unlisten();
	}
}
