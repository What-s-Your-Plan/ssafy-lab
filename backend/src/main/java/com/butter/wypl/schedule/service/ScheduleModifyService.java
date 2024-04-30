package com.butter.wypl.schedule.service;

import org.springframework.transaction.annotation.Transactional;

import com.butter.wypl.schedule.data.request.ScheduleRequest;
import com.butter.wypl.schedule.data.response.ScheduleIdResponse;
import com.butter.wypl.schedule.data.response.ScheduleResponse;

public interface ScheduleModifyService {

	ScheduleResponse createSchedule(int memberId, ScheduleRequest scheduleRequest);

	ScheduleResponse updateSchedule(int memberId, int scheduleId, ScheduleRequest scheduleRequest);

	ScheduleIdResponse deleteSchedule(int memberId, int scheduleId);

	@Transactional
	ScheduleIdResponse deleteRepeatSchedule(int memberId, int scheduleId);
}
