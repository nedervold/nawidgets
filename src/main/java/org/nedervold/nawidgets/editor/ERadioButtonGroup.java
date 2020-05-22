package org.nedervold.nawidgets.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;

import nz.sodium.Cell;
import nz.sodium.Stream;

public class ERadioButtonGroup extends JRadioButtonGroup {

	static class Impl extends EWidgetImpl<ERadioButtonGroup, Optional<String>, ERadioButtonGroup, ActionListener> {

		public Impl(final ERadioButtonGroup component, final Stream<Optional<String>> inputStream,
				final Optional<String> initValue) {
			super(component, inputStream, initValue);
		}

		@Override
		public void addSwingListener() {
			component.addActionListener(swingListener);
		}

		@Override
		public ActionListener createSwingListener() {
			return new ActionListener() {
				private Optional<String> value = null;

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
		public Optional<String> getComponentValue() {
			return component.buttonModelToString(component.getSelection());
		}

		@Override
		public ERadioButtonGroup getModel() {
			return component;
		}

		@Override
		public void removeSwingListener() {
			component.addActionListener(swingListener);
		}

		@Override
		public void setComponentValue(final Optional<String> value) {
			component.setSelection(component.stringToButtonModel(value));
		}
	}

	private final Impl impl;

	public ERadioButtonGroup(final List<JRadioButton> buttons, final Stream<Optional<String>> inputStream,
			final Optional<String> initValue) {
		super(buttons);
		impl = new Impl(this, inputStream, initValue);
	}

	public ERadioButtonGroup(final List<String> choices, final Stream<Optional<String>> inputStream) {
		this(choices.stream().map(JRadioButton::new).collect(Collectors.toList()), inputStream, Optional.empty());
	}

	private Optional<String> buttonModelToString(final Optional<ButtonModel> selection) {
		if (selection.isPresent()) {
			final ButtonModel model = selection.get();
			final Enumeration<AbstractButton> x = buttonGroup.getElements();
			while (x.hasMoreElements()) {
				final AbstractButton button = x.nextElement();
				if (button.getModel() == model) {
					return Optional.of(button.getText());
				}
			}
			// TODO Can't happen?
			return Optional.empty();
		} else {
			return Optional.empty();
		}
	}

	@Override
	public void removeNotify() {
		impl.unlisten();
		super.removeNotify();
	}

	private Optional<ButtonModel> stringToButtonModel(final Optional<String> value) {
		if (value.isPresent()) {
			final String name = value.get();
			final Enumeration<AbstractButton> x = buttonGroup.getElements();
			while (x.hasMoreElements()) {
				final AbstractButton button = x.nextElement();
				if (button.getText().equals(name)) {
					return Optional.of(button.getModel());
				}
			}
			// TODO Can't happen?
			return Optional.empty();
		} else {
			return Optional.empty();
		}
	}
	public Cell<Optional<String>> value() {
		return impl.outputCell;
	}
}
