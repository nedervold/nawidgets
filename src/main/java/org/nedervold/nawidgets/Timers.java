package org.nedervold.nawidgets;

import java.util.Optional;

import nz.sodium.CellLoop;
import nz.sodium.Stream;
import nz.sodium.Transaction;
import nz.sodium.time.TimerSystem;

public class Timers {
	public static Stream<Long> periodic(final TimerSystem<Long> timerSystem, final Long period) {
		return Transaction.run(() -> {
			final CellLoop<Optional<Long>> nextAlarmTime = new CellLoop<>();
			final Stream<Long> alarms = timerSystem.at(nextAlarmTime);
			nextAlarmTime.loop(alarms.map(t -> {
				return Optional.of(t + period);
			}).hold(Optional.<Long>of(timerSystem.time.sample() + period)));
			return alarms;
		});
	}

	private Timers() {
	}
}