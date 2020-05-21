package org.nedervold.nawidgets.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonModel;
import javax.swing.JCheckBox;
import javax.swing.SwingUtilities;

import nz.sodium.Cell;
import nz.sodium.Stream;

public class ECheckBox extends JCheckBox {
	public static class Impl extends EWidgetImpl<ECheckBox, Boolean, ButtonModel, ActionListener> {
		public Impl(final ECheckBox comp, final Stream<Boolean> inp, final Boolean init) {
			super(comp, inp, init);
		}

		@Override
		public void addSwingListener() {
			model.addActionListener(swingListener);
		}

		@Override
		public ActionListener createSwingListener() {
			return new ActionListener() {
				private Boolean value = null;

				@Override
				public void actionPerformed(final ActionEvent e) {
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
		public Boolean getComponentValue() {
			return component.isSelected();
		}

		@Override
		public ButtonModel getModel() {
			return component.getModel();
		}

		@Override
		public void removeSwingListener() {
			model.removeActionListener(swingListener);
		}

		@Override
		public void setComponentValue(final Boolean value) {
			component.setSelected(value);
		}
	};

	final Impl impl;

	public ECheckBox(final String label, final Stream<Boolean> inputStream, final Boolean initVal) {
		super(label, initVal);
		impl = new Impl(this, inputStream, initVal);
	}

	@Override
	public void removeNotify() {
		impl.unlisten();
		super.removeNotify();
	}

	public Cell<Boolean> value() {
		return impl.outputCell;
	}
}
