package org.nedervold.nawidgets.editor;

import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import nz.sodium.Stream;

public class ETextComponentImpl<S extends JTextComponent> extends EWidgetImpl<S, String, Document, DocumentListener> {
	ETextComponentImpl(final S comp, final Stream<String> inp, final String init) {
		super(comp, inp, init);
	}

	@Override
	public void addComponentListener() {
		model.addDocumentListener(componentListener);
	}

	@Override
	public DocumentListener createComponentListener() {
		return new DocumentListener() {
			private String value = null;

			@Override
			public void changedUpdate(final DocumentEvent e) {
				update();
			}

			@Override
			public void insertUpdate(final DocumentEvent e) {
				update();
			}

			@Override
			public void removeUpdate(final DocumentEvent e) {
				update();
			}

			public void update() {
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
	public String getComponentValue() {
		return component.getText();
	}

	@Override
	public Document getModel() {
		return component.getDocument();
	}

	@Override
	public void removeComponentListener() {
		model.removeDocumentListener(componentListener);
	}

	@Override
	public void setComponentValue(final String value) {
		component.setText(value);
	}
};
