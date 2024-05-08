package com.butter.wypl.calendar.data.response;

import java.util.List;

import com.butter.wypl.member.domain.Member;
import com.butter.wypl.schedule.domain.Category;
import com.butter.wypl.schedule.domain.Schedule;
import com.fasterxml.jackson.annotation.JsonProperty;

public record GroupCalendarResponse(
	@JsonProperty("schedule_id")
	int scheduleId,

	String title,

	Category category,

	@JsonProperty("member_count")
	int memberCount,

	List<MemberResponse> members

) {

	public static GroupCalendarResponse of(Schedule schedule, List<Member> members) {
		return new GroupCalendarResponse(
			schedule.getScheduleId(),
			schedule.getTitle(),
			schedule.getCategory(),
			members.size(),
			members.stream().map(MemberResponse::from).toList()
		);
	}
}
