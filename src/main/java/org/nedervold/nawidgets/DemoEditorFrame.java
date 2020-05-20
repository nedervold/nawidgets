package org.nedervold.nawidgets;

import java.awt.BorderLayout;
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

import org.nedervold.nawidgets.display.DBox;
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
			cp.add(vbox, BorderLayout.EAST);

			// TODO Something is not updating properly here. Am I running in the UI thread?
			// Do I need to invlidate something in DBox on changes?

			final ETextArea ta = new ETextArea(new StreamSink<>(), "", 8, 60);
			final Cell<List<JLabel>> comps = ta.value().map(DemoEditorFrame::f);
			final DBox<JLabel> db = new DBox<>(BoxLayout.Y_AXIS, comps);
			cp.add(ta, BorderLayout.NORTH);
			final JScrollPane sp = new JScrollPane(db, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			cp.add(sp, BorderLayout.WEST);
			pack();
		});
		setVisible(true);
	}

}
