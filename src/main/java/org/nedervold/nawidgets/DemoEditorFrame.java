package org.nedervold.nawidgets;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import org.nedervold.nawidgets.display.DBox;
import org.nedervold.nawidgets.display.DFlow;
import org.nedervold.nawidgets.display.DLabel;
import org.nedervold.nawidgets.editor.ECheckBox;
import org.nedervold.nawidgets.editor.EComboBox;
import org.nedervold.nawidgets.editor.EDateSpinner;
import org.nedervold.nawidgets.editor.EIntegerSpinner;
import org.nedervold.nawidgets.editor.ESlider;
import org.nedervold.nawidgets.editor.ETextArea;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import nz.sodium.Cell;
import nz.sodium.Operational;
import nz.sodium.Stream;
import nz.sodium.StreamLoop;
import nz.sodium.StreamSink;
import nz.sodium.Transaction;

public class DemoEditorFrame extends JFrame {

	public static enum WhichChanged {
		SLIDER_CHANGED, SPINNER_CHANGED
	};

	private static final int BORDER_SIZE = 8;

	private static List<JLabel> f(final String str) {
		final List<JLabel> res = new ArrayList<>();
		if (str.isEmpty()) {
			return res;
		}
		final String[] strs = str.toLowerCase().split("(\\W|\\n)+");
		final TreeSet<String> ts = new TreeSet<>(Arrays.asList(strs));
		for (final String s : ts) {
			res.add(new JLabel(s));
		}
		return res;
	}

	private static List<JLabel> f2(final String str) {
		final List<JLabel> res = new ArrayList<>();
		if (str.isEmpty()) {
			return res;
		}
		final int SMALL_BORDER_SIZE = 4;
		final String[] strs = str.toLowerCase().split("\\W+");
		final TreeSet<String> ts = new TreeSet<>(Arrays.asList(strs));
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
			final Cell<String> x = bowdlerizeCheckBox.outputCell().map(
					(b) -> b ? "Frankly, my dear, I don't give a darn." : "Frankly, my dear, I don't give a damn.");
			final DLabel lbl = new DLabel(x);
			vbox.add(lbl);
			vbox.setBorder(BorderFactory.createEmptyBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE));
			final Box hbox = Box.createHorizontalBox();

			final Box vbox2 = Box.createVerticalBox();

			// TODO I wanted to put these two in sync, but can't figure how to do it.
			final Stream<Integer> origSliderInput = new StreamSink<>();
			final Stream<Integer> origSpinnerInput = new StreamSink<>();
			final StreamLoop<Integer> sliderDeferredInput = new StreamLoop<>();
			final StreamLoop<Integer> spinnerDeferredInput = new StreamLoop<>();
			final Stream<Integer> sliderInput = origSliderInput.orElse(sliderDeferredInput);
			final Stream<Integer> spinnerInput = origSpinnerInput.orElse(spinnerDeferredInput);

			final EIntegerSpinner spinner = new EIntegerSpinner(0, 100, 1, spinnerInput, 0);
			final ESlider slider = new ESlider(SwingConstants.HORIZONTAL, 0, 100, sliderInput, 0);
			slider.setMajorTickSpacing(10);
			slider.setPaintTicks(true);
			slider.createStandardLabels(10);
			slider.setPaintLabels(true);

			final Stream<Integer> sliderUpdates = Operational.updates(slider.outputCell());
			final Stream<Integer> spinnerUpdates = Operational.updates(spinner.outputCell());

			final Stream<Tuple2<Integer, Integer>> sliderUpdatesWithSpinnerValue = sliderUpdates
					.snapshot(spinner.outputCell(), (sl, sp) -> Tuple.of(sl, sp));
			final Stream<Tuple2<Integer, Integer>> spinnerUpdatesWithSliderValue = spinnerUpdates
					.snapshot(slider.outputCell(), (sl, sp) -> Tuple.of(sl, sp));

			final Stream<Integer> filteredNewSpinnerValue = sliderUpdatesWithSpinnerValue.filter((t) -> t._1 != t._2)
					.map(Tuple2::_1);
			final Stream<Integer> filteredNewSliderValue = spinnerUpdatesWithSliderValue.filter((t) -> t._1 != t._2)
					.map(Tuple2::_1);
			sliderDeferredInput.loop(Operational.defer(filteredNewSliderValue));
			spinnerDeferredInput.loop(Operational.defer(filteredNewSpinnerValue));

			final Cell<String> diff = spinner.outputCell().lift(slider.outputCell(),
					(sp, sl) -> "spinner=" + sp + "; slider=" + sl);

			vbox2.add(spinner);
			vbox2.add(slider);
			vbox2.add(new DLabel(diff));
			hbox.add(vbox);
			hbox.add(vbox2);
			cp.add(hbox, BorderLayout.SOUTH);

			final ETextArea ta = new ETextArea(new StreamSink<>(), "", 8, 60);
			ta.setLineWrap(true);
			final Cell<List<JLabel>> comps = ta.outputCell().map(DemoEditorFrame::f);
			final DBox<JLabel> db = new DBox<>(BoxLayout.Y_AXIS, comps);
			final JScrollPane sp0 = new JScrollPane(ta, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

			cp.add(sp0, BorderLayout.NORTH);
			final JScrollPane sp = new JScrollPane(db, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			cp.add(sp, BorderLayout.WEST);

			final Cell<List<JLabel>> comps2 = ta.outputCell().map(DemoEditorFrame::f2);

			// TODO Does flow do what you think it does?
			final DFlow<JLabel> df = new DFlow<>(comps2);
			df.setBorder(BorderFactory.createEtchedBorder());
			cp.add(df, BorderLayout.CENTER);

			final StreamSink<Date> sink = new StreamSink<>();
			final StreamSink<String> sink2 = new StreamSink<>();
			final JButton setNow = new JButton("setNow");
			setNow.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(final ActionEvent e) {
					sink.send(new Date());
					sink2.send("start");
				}
			});
			final EComboBox<String> cb = new EComboBox<>(sink2, new String[] { "start", "middle", "end" });
			final Cell<String> msgCell = cb.outputCell().map((s) -> "We're now in the " + s + " of the demo.");
			final DLabel dl = new DLabel(msgCell);
			final EDateSpinner ds = new EDateSpinner(null, null, Calendar.DAY_OF_MONTH, sink, new Date());
			final Box lilBox = Box.createVerticalBox();
			lilBox.add(cb);
			lilBox.add(dl);
			lilBox.add(setNow);
			lilBox.add(ds);
			cp.add(lilBox, BorderLayout.EAST);
			pack();
		});
		setVisible(true);
	}

}
