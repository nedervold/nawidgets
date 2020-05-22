package org.nedervold.nawidgets.editor;

import javax.swing.SpinnerModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nz.sodium.Stream;

public class ESpinnerImpl<V> extends EWidgetImpl<EAbstractSpinner<V>, V, SpinnerModel, ChangeListener> {
	ESpinnerImpl(final EAbstractSpinner<V> comp, final Stream<V> inp, final V init) {
		super(comp, inp, init);
	}

	@Override
	public void addSwingListener() {
		model.addChangeListener(swingListener);
	}

	@Override
	public ChangeListener createSwingListener() {
		return new ChangeListener() {
			private V value = null;

			@Override
			public void stateChanged(final ChangeEvent e) {
				value = getComponentValue();
				SwingUtilities.invokeLater(() -> {
					if (value != null) {
						userChangesSink.send(value);
						value = null;
					}
				});
			}
		};
	}

	@SuppressWarnings("unchecked")
	@Override
	public V getComponentValue() {
		return (V) component.getValue();
	}

	@Override
	public SpinnerModel getModel() {
		return component.getModel();
	}

	@Override
	public void removeSwingListener() {
		model.removeChangeListener(swingListener);
	}

	@Override
	public void setComponentValue(final V value) {
		component.setValue(value);
	}
}