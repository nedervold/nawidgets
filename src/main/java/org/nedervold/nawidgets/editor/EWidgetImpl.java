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

	protected final L componentListener;

	private final StreamSink<Integer> decrementPending = new StreamSink<>();

	private final Listener listener;

	protected final M model;

	protected final Cell<V> outputCell;

	private final Cell<Boolean> userChangesAllowed;

	protected final StreamSink<V> userChangesSink;

	public EWidgetImpl(final S component, final Stream<V> inputStream, final V initValue) {
		this.component = component;

		// Set up userChangesAllowed.
		final Stream<Integer> incrementPending = inputStream.map(v -> 1);
		final Stream<Integer> pendingDeltas = incrementPending.orElse(decrementPending);
		final Cell<Integer> pendingExternalChanges = pendingDeltas.accum(0, (d, b) -> b + d);
		userChangesAllowed = pendingExternalChanges.map(b -> b == 0);

		// Set up userChanges and outputCell.
		this.userChangesSink = new StreamSink<>();
		this.outputCell = userChangesSink.gate(userChangesAllowed).orElse(inputStream).hold(initValue);

		this.componentListener = createComponentListener();
		this.model = getModel();
		addComponentListener();

		listener = inputStream.listen(value -> {
			SwingUtilities.invokeLater(() -> {
				removeComponentListener();
				setComponentValue(value);
				addComponentListener();
				decrementPending.send(-1);
			});
		});
	}

	public abstract void addComponentListener();

	public abstract L createComponentListener();

	public abstract V getComponentValue();

	public abstract M getModel();

	public abstract void removeComponentListener();

	public abstract void setComponentValue(V value);

	public void unlisten() {
		listener.unlisten();
	}
}
