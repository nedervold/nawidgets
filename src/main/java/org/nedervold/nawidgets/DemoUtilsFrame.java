package org.nedervold.nawidgets;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.nedervold.nawidgets.display.DTextArea;
import org.nedervold.nawidgets.editor.ETextArea;

import nz.sodium.Cell;
import nz.sodium.Operational;
import nz.sodium.Stream;
import nz.sodium.StreamSink;
import nz.sodium.Transaction;
import nz.sodium.time.MillisecondsTimerSystem;

public class DemoUtilsFrame extends JFrame {

	public DemoUtilsFrame() {
		super("Demo Utils Frame");
		final Container cp = getContentPane();
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		Transaction.runVoid(() -> {
			final Box hbox = Box.createHorizontalBox();
			final Box vbox = Box.createVerticalBox();
			final StreamSink<String> strStream = new StreamSink<>();
			final JButton oneButton = new JButton("one");
			oneButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent e) {
					strStream.send("one");
				}
			});
			final JButton twoButton = new JButton("two");
			twoButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent e) {
					strStream.send("two");
				}
			});
			final JButton threeButton = new JButton("three");
			threeButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent e) {
					strStream.send("three");
				}
			});

			vbox.add(oneButton);
			vbox.add(twoButton);
			vbox.add(threeButton);
			hbox.add(vbox);
			final Stream<String> calmStrStream = Utils.calm(strStream);

			final Cell<String> strCell = strStream.accum("", (nxt, prev) -> prev.isEmpty() ? nxt : prev + "\n" + nxt);
			final Cell<String> calmStrCell = calmStrStream.accum("",
					(nxt, prev) -> prev.isEmpty() ? nxt : prev + "\n" + nxt);

			final JScrollPane scroll = new JScrollPane(new DTextArea(10, 10, strCell));
			scroll.setBorder(BorderFactory.createTitledBorder("raw stream"));
			hbox.add(scroll);
			final JScrollPane calmScroll = new JScrollPane(new DTextArea(10, 10, calmStrCell));
			calmScroll.setBorder(BorderFactory.createTitledBorder("calm stream"));
			hbox.add(calmScroll);
			cp.add(hbox, BorderLayout.NORTH);

			final Box hbox2 = Box.createHorizontalBox();
			final StreamSink<String> txtIn = new StreamSink<>();
			final JButton clear = new JButton("clear");
			hbox2.add(clear);
			clear.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(final ActionEvent e) {
					txtIn.send("");
				}
			});
			final ETextArea editTxt = new ETextArea(txtIn, "", 20, 20);
			editTxt.setBorder(BorderFactory.createTitledBorder("type here"));
			hbox2.add(new JScrollPane(editTxt));
			final Stream<String> editS = Operational.updates(editTxt.outputCell());
			final MillisecondsTimerSystem sys = new MillisecondsTimerSystem();
			final Stream<String> debounced = Utils.debounce(sys, 1000L, editS);
			final DTextArea showTxt = new DTextArea(20, 20, debounced.hold(""));
			showTxt.setBorder(BorderFactory.createTitledBorder("waits to show"));
			hbox2.add(new JScrollPane(showTxt));
			cp.add(hbox2);
			pack();
		});

		setVisible(true);

	}

}
