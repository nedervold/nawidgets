package org.nedervold.nawidgets.editor;

import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nz.sodium.Cell;
import nz.sodium.Stream;

public class ESlider extends JSlider implements Editor<Integer> {
	static class Impl extends EWidgetImpl<ESlider, Integer, ESlider, ChangeListener> {

		public Impl(final ESlider component, final Stream<Integer> inputStream, final Integer initialValue) {
			super(component, inputStream, initialValue);
		}

		@Override
		public void addSwingListener() {
			component.addChangeListener(swingListener);
		}

		@Override
		public ChangeListener createSwingListener() {
			return new ChangeListener() {
				Integer value = null;

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

		@Override
		public Integer getComponentValue() {
			return component.getValue();
		}

		@Override
		public ESlider getModel() {
			return component;
		}

		@Override
		public void removeSwingListener() {
			component.removeChangeListener(swingListener);
		}

		@Override
		public void setComponentValue(final Integer value) {
			component.setValue(value);
		}
	}

	private final Impl impl;

	public ESlider(final int orientation, final int min, final int max, final Stream<Integer> inputStream,
			final int value) {
		super(orientation, min, max, value);
		impl = new Impl(this, inputStream, value);
	}

	@Override
	public Cell<Integer> outputCell() {
		return impl.outputCell;
	}

	public void unlisten() {
		impl.unlisten();
	}

}
