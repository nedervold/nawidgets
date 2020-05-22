package org.nedervold.nawidgets.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.SwingUtilities;

import nz.sodium.Stream;

public class EAbstractButtonImpl<C extends AbstractButton>
		extends EWidgetImpl<C, Boolean, ButtonModel, ActionListener> {
	public EAbstractButtonImpl(final C comp, final Stream<Boolean> inp, final Boolean init) {
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
}