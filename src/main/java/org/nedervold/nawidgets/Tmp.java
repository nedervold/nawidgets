package org.nedervold.nawidgets;

import javax.swing.*;

import org.nedervold.nawidgets.editor.ERadioButtonGroup;

import java.util.*;
import nz.sodium.*;

public class Tmp extends JFrame {

	public Tmp() {
		super("temp");
		Box vbox = Box.createVerticalBox();

		StreamSink<Optional<String>> sink = new StreamSink<>();

		ERadioButtonGroup ebg = new ERadioButtonGroup(
				Arrays.asList(new String[] { "unu", "du", "tri", "kvar", "kvin" }), sink);

		Operational.updates(ebg.value()).listen((ms) -> {
			System.out.println(ms);
		});
		JButton clear = new JButton("clear");
		clear.addActionListener((e) -> sink.send(Optional.empty()));

		vbox.add(clear);
		vbox.add(ebg);
		getContentPane().add(vbox);
		pack();
		setVisible(true);
	}

}
