package org.nedervold.nawidgets;

import java.awt.BorderLayout;
import java.awt.Container;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.SwingConstants;

import org.nedervold.nawidgets.display.DLabel;
import org.nedervold.nawidgets.display.DProgressBar;
import org.nedervold.nawidgets.display.DTextArea;

import nz.sodium.Cell;
import nz.sodium.Stream;
import nz.sodium.Transaction;
import nz.sodium.time.MillisecondsTimerSystem;

public class App extends JFrame {

	private static final int BORDER_SIZE = 20;

	private static final DateFormat FORMATTER = makeFormatter();

	private static GregorianCalendar dateToCal(final Date millis) {
		final GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(millis);
		return cal;

	}

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

		Transaction.runVoid(() -> {
			final MillisecondsTimerSystem sys = new MillisecondsTimerSystem();
			final Stream<Long> millisStream = Timers.periodic(sys, 1000L);
			final Stream<Date> dateStream = millisStream.map(Date::new);
			final Stream<GregorianCalendar> calStream = dateStream.map(App::dateToCal);
			final Stream<String> timeStream = dateStream.map(FORMATTER::format);
			final String initValue = FORMATTER.format(new Date(sys.time.sample()));
			final Cell<String> timeCell = timeStream.hold(initValue);

			final DLabel label = new DLabel(timeCell);
			label.setBorder(BorderFactory.createEmptyBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE));
			cp.add(label, BorderLayout.NORTH);

			final Stream<Integer> secondsStream = calStream.map((cal) -> cal.get(Calendar.SECOND));
			final Cell<Integer> secondsCell = secondsStream.hold(0);
			final DProgressBar pb = new DProgressBar(SwingConstants.HORIZONTAL, 0, 59, secondsCell);
			cp.add(pb, BorderLayout.SOUTH);

			final Cell<String> sentenceTime = timeCell
					.map((str) -> "He says,\n“The time is now exactly " + str + ".”\nThat’s what he says.\n");
			final DTextArea ta = new DTextArea(4, 20, sentenceTime);
			cp.add(ta, BorderLayout.CENTER);
			pack();
		});

		setVisible(true);

	}

}
