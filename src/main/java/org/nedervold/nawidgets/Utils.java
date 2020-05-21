package org.nedervold.nawidgets;

import java.util.Optional;

import nz.sodium.Cell;
import nz.sodium.Lazy;
import nz.sodium.Operational;
import nz.sodium.Stream;
import nz.sodium.Tuple2;
import nz.sodium.time.MillisecondsTimerSystem;

// calm() implementations lifted directly from
// https://github.com/SodiumFRP/sodium/blob/89f40da2f62aed75201a86868e489f1564d667f6/book/patterns/java/calm.java
// License here:
// https://github.com/SodiumFRP/sodium/blob/89f40da2f62aed75201a86868e489f1564d667f6/COPYING
// TODO Integrate the licence into this repository.
public class Utils {

	public static <A> Cell<A> calm(final Cell<A> a) {
		final Lazy<A> initA = a.sampleLazy();
		final Lazy<Optional<A>> oInitA = initA.map(a_ -> Optional.of(a_));
		return calm(Operational.updates(a), oInitA).holdLazy(initA);
	}

	public static <A> Stream<A> calm(final Stream<A> sA) {
		return calm(sA, new Lazy<Optional<A>>(Optional.empty()));
	}

	public static <A> Stream<A> calm(final Stream<A> sA, final Lazy<Optional<A>> oInit) {
		return Stream.filterOptional(
				sA.<Optional<A>, Optional<A>>collectLazy(oInit, (final A a, final Optional<A> oLastA) -> {
					final Optional<A> oa = Optional.of(a);
					return oa.equals(oLastA) ? new Tuple2<>(Optional.empty(), oLastA) : new Tuple2<>(oa, oa);
				}));
	}

	/**
	 * Returns a Stream where values are emitted only if a delay period has passed
	 * without another value being emitted. ̰
	 *
	 * @param <A>
	 *            type of the Streams
	 * @param sys
	 *            a TimerSystem
	 * @param delayMillisecs
	 *            the time to wait for values to stabilize
	 * @param input
	 *            the input Stream
	 * @return the debounced Stream
	 */
	public static <A> Stream<A> debounce(final MillisecondsTimerSystem sys, final Long delayMillisecs,
			final Stream<A> input) {
		// "delayedTimes" is a stream of times, one for each element of
		// the "input" stream, but delayed from the time of that element's arrival
		// by "delayMillisecs".
		final Stream<Long> delayedTimes = input.snapshot(sys.time,
				(final A nextValue, final Long time) -> time + delayMillisecs);

		// "nextAlarmTime" wraps "delayedTimes" in Optional and holds the latest delayed
		// time in a Cell.
		final Cell<Optional<Long>> nextAlarmTime = holdOptional(delayedTimes);

		// "alarm" goes off "delayMillisecs" after the arrival of last element of
		// "input".
		final Stream<Long> alarm = sys.at(nextAlarmTime);

		// "inputValue" holds the values from "input".
		final Cell<Optional<A>> inputValue = holdOptional(input);

		// "delayedValues" contains the (wrapped) values from "input", but sampled at
		// the last arrival time delayed by "delayMillisecs".
		final Stream<Optional<A>> delayedValues = alarm.snapshot(inputValue,
				(final Long time, final Optional<A> inputVal) -> inputVal);
		return Stream.filterOptional(delayedValues);
	}

	/**
	 * Returns a Cell containing the values from the Stream wrapped in Optional.
	 * Contains nothing if the Stream has emitted nothing.
	 *
	 * @param <A>
	 *            type of the Streams
	 * @param input
	 *            the input Stream
	 * @return the output Cell
	 */
	public static <A> Cell<Optional<A>> holdOptional(final Stream<A> input) {
		return input.map(Optional::of).hold(Optional.empty());
	}

	private Utils() {
	}

}
