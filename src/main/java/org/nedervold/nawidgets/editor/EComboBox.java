package org.nedervold.nawidgets.editor;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;

import javax.swing.JComboBox;
import javax.swing.SwingUtilities;

import nz.sodium.Cell;
import nz.sodium.Stream;

public class EComboBox<E> extends JComboBox<E> {
	public static class Impl<T> extends EWidgetImpl<EComboBox<T>, T, EComboBox<T>, ItemListener> {
		public Impl(final EComboBox<T> component, final Stream<T> inp, final T initValue) {
			super(component, inp, initValue);
		}

		@Override
		public void addSwingListener() {
			component.addItemListener(swingListener);
		}

		@Override
		public ItemListener createSwingListener() {
			return new ItemListener() {
				private T value = null;

				@Override
				public void itemStateChanged(final ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						value = getComponentValue();
						SwingUtilities.invokeLater(() -> {
							if (value != null) {
								userChangesSink.send(value);
								value = null;
							}
						});
					}
					;
				}
			};
		}

		@SuppressWarnings("unchecked")
		@Override
		public T getComponentValue() {
			return (T) component.getSelectedItem();
		}

		@Override
		public EComboBox<T> getModel() {
			return component;
		}

		@Override
		public void removeSwingListener() {
			component.removeItemListener(swingListener);
		}

		@Override
		public void setComponentValue(final T value) {
			component.setSelectedItem(value);
		}
	}

	private final Impl<E> impl;

	public EComboBox(final Stream<E> inputStream, final E[] universe) {
		super(universe);
		impl = new Impl<>(this, inputStream.filter(s -> Arrays.asList(universe).contains(s)), universe[0]);
	}

	@Override
	public void removeNotify() {
		impl.unlisten();
		super.removeNotify();
	}

	public Cell<E> value() {
		return impl.outputCell;
	}
}
