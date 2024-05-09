package com.butter.wypl.group.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.butter.wypl.group.domain.MemberGroup;
import com.butter.wypl.group.repository.MemberGroupRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberGroupService {

	private final MemberGroupRepository memberGroupRepository;

	@Transactional
	public void deleteMemberGroup(int memberId, int groupId) {
		MemberGroup memberGroup = memberGroupRepository.findMemberGroupByMemberIdAndGroupId(memberId, groupId);
		memberGroup.delete();
	}
}
