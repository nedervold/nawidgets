package org.nedervold.nawidgets;

import java.util.Arrays;
import java.util.Optional;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;

import org.nedervold.nawidgets.editor.ERadioButtonGroup;

import nz.sodium.Operational;
import nz.sodium.StreamSink;

public class Tmp extends JFrame {

	public Tmp() {
		super("temp");
		final Box vbox = Box.createVerticalBox();

		final StreamSink<Optional<String>> sink = new StreamSink<>();

		final ERadioButtonGroup ebg = new ERadioButtonGroup(
				Arrays.asList(new String[] { "unu", "du", "tri", "kvar", "kvin" }), sink);

		Operational.updates(ebg.outputCell()).listen((ms) -> {
			System.out.println(ms);
		});
		final JButton clear = new JButton("clear");
		clear.addActionListener((e) -> sink.send(Optional.empty()));

		vbox.add(clear);
		vbox.add(ebg);
		getContentPane().add(vbox);
		pack();
		setVisible(true);
	}

}
