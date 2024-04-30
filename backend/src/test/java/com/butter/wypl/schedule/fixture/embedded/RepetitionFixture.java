package com.butter.wypl.schedule.fixture.embedded;

import java.time.LocalDate;

import com.butter.wypl.schedule.data.request.RepetitionRequest;
import com.butter.wypl.schedule.domain.embedded.RepetitionCycle;

public enum RepetitionFixture {
	MONDAY_REPETITION(
		RepetitionCycle.WEEK,
		LocalDate.of(2024, 4, 25),
		LocalDate.of(2025, 4, 25),
		1,
		(byte)0b000011
	),
	TUESDAY_THRUSDAY_REPETITION(
		RepetitionCycle.WEEK,
		LocalDate.of(2024, 4, 25),
		LocalDate.of(2025, 4, 25),
		2,
		(byte)0b0010100
	),
	MONTHLY_REPETITION(
		RepetitionCycle.MONTH,
		LocalDate.of(2024, 4, 25),
		LocalDate.of(2025, 4, 25),
		0,
		(byte)0b0000000
	),
	LAST_DAY_REPETITION(
		RepetitionCycle.MONTH,
		LocalDate.of(2024, 4, 25),
		LocalDate.of(2025, 4, 25),
		0,
		(byte)0b0000000
	),
	YEARLY_REPETITION(
		RepetitionCycle.YEAR,
		LocalDate.of(2024, 4, 25),
		null,
		0,
		(byte)0b0000000
	),
	;

	private final RepetitionCycle repetitionCycle;

	private final LocalDate repetitionStartDate;

	private final LocalDate repetitionEndDate;

	private final int week;

	private final byte dayOfWeek;

	RepetitionFixture(RepetitionCycle repetitionCycle, LocalDate repetitionStartDate,
		LocalDate repetitionEndDate, int week, byte dayOfWeek) {
		this.repetitionCycle = repetitionCycle;
		this.repetitionStartDate = repetitionStartDate;
		this.repetitionEndDate = repetitionEndDate;
		this.week = week;
		this.dayOfWeek = dayOfWeek;
	}

	public RepetitionRequest toRepetitionRequest() {
		return new RepetitionRequest(
			repetitionCycle,
			repetitionStartDate,
			repetitionEndDate,
			dayOfWeek,
			week
		);
	}

}
