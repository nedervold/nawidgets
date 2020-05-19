package org.nedervold.nawidgets;

import java.awt.BorderLayout;
import java.awt.Container;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.swing.BorderFactory;
import javax.swing.JFrame;

import org.nedervold.nawidgets.display.DStringLabel;

import nz.sodium.Stream;
import nz.sodium.time.MillisecondsTimerSystem;

public class App extends JFrame {

	private static final int BORDER_SIZE = 20;

	private static final DateFormat FORMATTER = makeFormatter();

	public static void main(final String[] args) {
		new App();
	}

	private static DateFormat makeFormatter() {
		final DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("GMT-7"));
		return formatter;
	}

	public App() {
		super("NaWidgets App");
		final Container cp = getContentPane();
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		final MillisecondsTimerSystem sys = new MillisecondsTimerSystem();
		final Stream<Long> millisStream = Timers.periodic(sys, 1000L);
		final Stream<Date> dateStream = millisStream.map(Date::new);
		final Stream<String> timeStream = dateStream.map(FORMATTER::format);

		final String initValue = FORMATTER.format(new Date(sys.time.sample()));
		final DStringLabel label = new DStringLabel(timeStream.hold(initValue));
		label.setBorder(BorderFactory.createEmptyBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE));
		cp.add(label, BorderLayout.NORTH);

		pack();
		setVisible(true);
	}

}
