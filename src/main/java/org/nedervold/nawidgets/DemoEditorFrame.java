package org.nedervold.nawidgets;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;

import org.nedervold.nawidgets.display.DBox;
import org.nedervold.nawidgets.display.DFlow;
import org.nedervold.nawidgets.display.DLabel;
import org.nedervold.nawidgets.editor.ECheckBox;
import org.nedervold.nawidgets.editor.ETextArea;

import nz.sodium.Cell;
import nz.sodium.StreamSink;
import nz.sodium.Transaction;

public class DemoEditorFrame extends JFrame {

	private static final int BORDER_SIZE = 8;

	private static List<JLabel> f(final String str) {
		final String[] strs = str.toLowerCase().split("\\W+");
		final TreeSet<String> ts = new TreeSet<>(Arrays.asList(strs));
		final List<JLabel> res = new ArrayList<>();
		for (final String s : ts) {
			res.add(new JLabel(s));
		}
		return res;
	}

	private static List<JLabel> f2(final String str) {
		final int SMALL_BORDER_SIZE = 4;
		final String[] strs = str.toLowerCase().split("\\W+");
		final TreeSet<String> ts = new TreeSet<>(Arrays.asList(strs));
		final List<JLabel> res = new ArrayList<>();
		for (final String s : ts) {
			final JLabel label = new JLabel(s);
			final Border empty = BorderFactory.createEmptyBorder(SMALL_BORDER_SIZE, SMALL_BORDER_SIZE,
					SMALL_BORDER_SIZE, SMALL_BORDER_SIZE);
			final Border line = BorderFactory.createLineBorder(Color.BLACK);
			label.setBorder(BorderFactory.createCompoundBorder(line, empty));
			res.add(label);
		}
		return res;
	}

	public DemoEditorFrame() {
		super("Demo Editor Frame");
		final Container cp = getContentPane();
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		Transaction.runVoid(() -> {
			final Box vbox = Box.createVerticalBox();
			final ECheckBox bowdlerizeCheckBox = new ECheckBox("Bowdlerize", new StreamSink<Boolean>(), false);
			vbox.add(bowdlerizeCheckBox);
			final Cell<String> x = bowdlerizeCheckBox.value().map(
					(b) -> b ? "Frankly, my dear, I don't give a darn." : "Frankly, my dear, I don't give a damn.");
			final DLabel lbl = new DLabel(x);
			vbox.add(lbl);
			vbox.setBorder(BorderFactory.createEmptyBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE));
			cp.add(vbox, BorderLayout.SOUTH);

			final ETextArea ta = new ETextArea(new StreamSink<>(), "", 8, 60);
			ta.setLineWrap(true);
			final Cell<List<JLabel>> comps = ta.value().map(DemoEditorFrame::f);
			final DBox<JLabel> db = new DBox<>(BoxLayout.Y_AXIS, comps);
			final JScrollPane sp0 = new JScrollPane(ta, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

			cp.add(sp0, BorderLayout.NORTH);
			final JScrollPane sp = new JScrollPane(db, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			cp.add(sp, BorderLayout.WEST);

			final Cell<List<JLabel>> comps2 = ta.value().map(DemoEditorFrame::f2);

			// TODO Does flow do what you think it does?
			final DFlow<JLabel> df = new DFlow<>(comps2);
			df.setBorder(BorderFactory.createEtchedBorder());
			cp.add(df, BorderLayout.CENTER);
			pack();
		});
		setVisible(true);
	}

}
