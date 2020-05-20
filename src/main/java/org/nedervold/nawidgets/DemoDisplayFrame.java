package org.nedervold.nawidgets;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import org.nedervold.nawidgets.display.DBox;
import org.nedervold.nawidgets.display.DLabel;
import org.nedervold.nawidgets.display.DProgressBar;
import org.nedervold.nawidgets.display.DTextArea;
import org.nedervold.nawidgets.display.DWidgetImpl;

import nz.sodium.Cell;
import nz.sodium.Stream;
import nz.sodium.Transaction;
import nz.sodium.time.MillisecondsTimerSystem;

public class DemoDisplayFrame extends JFrame {

	static class ColorImpl extends DWidgetImpl<DColorLabel, Color> {
		ColorImpl(final DColorLabel comp, final Cell<Color> inputCell) {
			super(comp, inputCell);
		}

		@Override
		public void setComponentValue(final Color value) {
			component.setForeground(value);
		}
	}

	static class DColorLabel extends DLabel {

		private final ColorImpl colorImpl;

		public DColorLabel(final Cell<String> inputCell, final Cell<Color> colorInputCell) {
			super(inputCell);
			colorImpl = new ColorImpl(this, colorInputCell);
		}

		@Override
		public void removeNotify() {
			colorImpl.unlisten();
			super.removeNotify();
		}

	}

	private static final int BORDER_SIZE = 20;

	private static final DateFormat FORMATTER = makeFormatter();

	private static GregorianCalendar dateToCal(final Date millis) {
		final GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(millis);
		return cal;

	}

	public static void main(final String[] args) {
		new DemoDisplayFrame();
	}

	private static DateFormat makeFormatter() {
		final DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("GMT-7"));
		return formatter;
	}

	public DemoDisplayFrame() {
		super("Demo Display Frame");
		final Container cp = getContentPane();
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		Transaction.runVoid(() -> {
			final MillisecondsTimerSystem sys = new MillisecondsTimerSystem();
			final Stream<Long> millisStream = Timers.periodic(sys, 1000L);
			final Stream<Date> dateStream = millisStream.map(Date::new);
			final Stream<GregorianCalendar> calStream = dateStream.map(DemoDisplayFrame::dateToCal);
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

			final Cell<Color> colorCell = secondsCell.map((s) -> Color.getHSBColor(s / 60.0f, 1, 0.5f));
			final DColorLabel cl = new DColorLabel(timeCell, colorCell);
			cp.add(cl, BorderLayout.EAST);

			final Cell<List<JLabel>> compsCell = secondsCell.map((s) -> {
				final List<JLabel> res = new ArrayList<>();
				for (int n = s; n >= 0; n--) {
					final String str = Integer.toString(n);
					final JLabel numLabel = new JLabel(str);
					res.add(numLabel);
				}
				return res;
			});
			final DBox<JLabel> box = new DBox<>(BoxLayout.Y_AXIS, compsCell);
			final JScrollPane sp = new JScrollPane(box, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			sp.setMinimumSize(new Dimension(60, 200));
			cp.add(sp, BorderLayout.WEST);

			pack();
		});

		setVisible(true);

	}

}
