package com.butter.wypl.schedule.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ScheduleIdResponse(
	@JsonProperty("schedule_id")
	int scheduleId
) {
}
