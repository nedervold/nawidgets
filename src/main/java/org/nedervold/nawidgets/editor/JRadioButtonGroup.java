package org.nedervold.nawidgets.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Optional;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JRadioButton;

public class JRadioButtonGroup extends Box {

	private final ActionListener actionListener = new ActionListener() {
		@Override
		public void actionPerformed(final ActionEvent e) {
			fireActionPerformed(e);
		}
	};

	protected final ButtonGroup buttonGroup;

	public JRadioButtonGroup(final List<JRadioButton> buttons) {
		super(BoxLayout.Y_AXIS);
		buttonGroup = new ButtonGroup();
		for (final JRadioButton button : buttons) {
			add(button);
			buttonGroup.add(button);
			button.addActionListener(actionListener);
		}
	}

	public void addActionListener(final ActionListener l) {
		listenerList.add(ActionListener.class, l);
	}

	private void fireActionPerformed(final ActionEvent e) {
		final Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ActionListener.class) {
				// I'm just passing on the radio buttons' events.
				((ActionListener) listeners[i + 1]).actionPerformed(e);
			}
		}
	}

	public Optional<ButtonModel> getSelection() {
		final ButtonModel selection = buttonGroup.getSelection();
		if (selection == null) {
			return Optional.empty();
		} else {
			return Optional.of(selection);
		}
	}

	public void removeActionListener(final ActionListener l) {
		listenerList.remove(ActionListener.class, l);
	}

	public void setSelection(final Optional<ButtonModel> selection) {
		if (selection.isPresent()) {
			buttonGroup.setSelected(selection.get(), true);
		} else {
			buttonGroup.clearSelection();
		}
	}

}
