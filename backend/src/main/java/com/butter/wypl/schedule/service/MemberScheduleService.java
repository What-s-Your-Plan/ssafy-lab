package com.butter.wypl.schedule.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

import com.butter.wypl.global.annotation.FacadeService;
import com.butter.wypl.member.domain.Member;
import com.butter.wypl.member.repository.MemberRepository;
import com.butter.wypl.member.utils.MemberServiceUtils;
import com.butter.wypl.schedule.data.response.MemberIdResponse;
import com.butter.wypl.schedule.domain.MemberSchedule;
import com.butter.wypl.schedule.domain.Schedule;
import com.butter.wypl.schedule.exception.ScheduleErrorCode;
import com.butter.wypl.schedule.exception.ScheduleException;
import com.butter.wypl.schedule.respository.MemberScheduleRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@FacadeService
public class MemberScheduleService {

	private final MemberScheduleRepository memberScheduleRepository;
	private final MemberRepository memberRepository;

	@Transactional
	public List<Member> createMemberSchedule(Schedule schedule, List<MemberIdResponse> memberIdResponses) {
		List<MemberSchedule> memberSchedules = new ArrayList<>();

		for (MemberIdResponse memberIdResponse : memberIdResponses) {
			memberSchedules.add(
				MemberSchedule.builder()
					.member(MemberServiceUtils.findById(memberRepository, memberIdResponse.memberId()))
					.schedule(schedule)
					.build()
			);
		}

		List<MemberSchedule> savedMemberSchedules = memberScheduleRepository.saveAll(memberSchedules);

		return savedMemberSchedules.stream()
			.map(memberSchedule -> memberSchedule.getMember())
			.collect(Collectors.toList());
	}

	public void validateMemberSchedule(Schedule schedule, int memberId) {
		Member member = MemberServiceUtils.findById(memberRepository, memberId);

		memberScheduleRepository.findByScheduleAndMember(schedule, member)
			.orElseThrow(() -> new ScheduleException(ScheduleErrorCode.NOT_PERMISSION_UPDATE_SCHEDUEL));
	}

	@Transactional
	public void updateMemberSchedule(Schedule schedule, List<MemberIdResponse> memberIdResponses) {
		List<Member> newMembers = new ArrayList<>();
		for (MemberIdResponse memberIdResponse : memberIdResponses) {
			newMembers.add(MemberServiceUtils.findById(memberRepository, memberIdResponse.memberId()));
		}

		// 기존 MemberSchedule들을 조회
		List<MemberSchedule> existingMemberSchedules = memberScheduleRepository.findAllBySchedule(schedule);

		// 바뀌는 멤버에 속하지 않는 기존 MemberSchedule 삭제
		for (MemberSchedule memberSchedule : existingMemberSchedules) {
			if (!newMembers.contains(memberSchedule.getMember())) {
				memberSchedule.delete();
			}
		}

		// 새로운 MemberSchedule 추가
		for (Member newMember : newMembers) {
			if (existingMemberSchedules.stream()
				.noneMatch(memberSchedule -> memberSchedule.getMember().getId() == newMember.getId())) {
				memberScheduleRepository.save(
					MemberSchedule
						.builder()
						.schedule(schedule)
						.member(newMember)
						.build()
				);
			}
		}
	}

	public List<Member> getMembersBySchedule(Schedule schedule) {
		List<Member> members = new ArrayList<>();
		List<MemberSchedule> memberSchedules = memberScheduleRepository.findAllBySchedule(schedule);

		for (MemberSchedule memberSchedule : memberSchedules) {
			members.add(memberSchedule.getMember());
		}

		return members;
	}
}
