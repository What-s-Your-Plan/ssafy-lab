package com.butter.wypl.group.service;

import static com.butter.wypl.group.exception.GroupErrorCode.*;
import static com.butter.wypl.group.utils.GroupServiceUtils.findById;
import static com.butter.wypl.group.utils.GroupServiceUtils.*;
import static com.butter.wypl.group.utils.MemberGroupServiceUtils.*;
import static com.butter.wypl.member.utils.MemberServiceUtils.findById;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.butter.wypl.global.common.BaseEntity;
import com.butter.wypl.global.exception.CustomErrorCode;
import com.butter.wypl.global.exception.CustomException;
import com.butter.wypl.group.data.request.GroupCreateRequest;
import com.butter.wypl.group.data.request.GroupMemberInviteRequest;
import com.butter.wypl.group.data.request.GroupUpdateRequest;
import com.butter.wypl.group.data.request.MemberIdRequest;
import com.butter.wypl.group.data.response.GroupIdResponse;
import com.butter.wypl.group.data.response.MemberIdResponse;
import com.butter.wypl.group.domain.Group;
import com.butter.wypl.group.domain.MemberGroup;
import com.butter.wypl.group.exception.GroupErrorCode;
import com.butter.wypl.group.exception.GroupException;
import com.butter.wypl.group.repository.GroupRepository;
import com.butter.wypl.group.repository.MemberGroupRepository;
import com.butter.wypl.member.domain.Member;
import com.butter.wypl.member.repository.MemberRepository;
import com.butter.wypl.notification.service.GroupNotificationService;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class GroupModifyServiceImpl implements GroupModifyService {

	private final GroupRepository groupRepository;
	private final MemberRepository memberRepository;
	private final MemberGroupRepository memberGroupRepository;
	private final GroupNotificationService groupNotificationService;

	@Transactional
	@Override
	public GroupIdResponse createGroup(int ownerId, GroupCreateRequest createRequest) {

		Set<Integer> memberIds = new HashSet<>(createRequest.memberIdList());
		memberIds.add(ownerId);
		validateMaxMemberCount(memberIds);

		List<Member> members = memberRepository.findAllById(memberIds);
		validateAllMembersExist(members, memberIds);

		Group savedGroup = groupRepository.save(
			Group.of(createRequest.name(), createRequest.description(), getMember(ownerId)));
		saveAllMemberGroup(members, savedGroup);

		return new GroupIdResponse(savedGroup.getId());
	}

	@Transactional
	@Override
	public GroupIdResponse updateGroup(int memberId, int groupId, GroupUpdateRequest updateRequest) {

		Member foundMember = getMember(memberId);
		Group foundGroup = getGroup(groupId);
		isGroupMember(foundMember.getId(), getMembersByGroupId(memberGroupRepository, foundGroup.getId()));

		foundGroup.updateGroupInfo(updateRequest.name(), updateRequest.description());
		return new GroupIdResponse(foundGroup.getId());
	}

	@Transactional
	@Override
	public void deleteGroup(int memberId, int groupId) {

		Group foundGroup = getGroup(groupId);
		Member foundMember = getMember(memberId);
		validateOwnerPermission(foundMember, foundGroup, HAS_NOT_DELETE_PERMISSION);

		List<MemberGroup> findMemberGroups = getMemberGroupsByGroupId(memberGroupRepository, groupId);
		findMemberGroups.forEach(BaseEntity::delete);
		foundGroup.delete();
	}

	@Override
	public GroupIdResponse inviteGroupMember(int ownerId, int groupId, GroupMemberInviteRequest inviteRequest) {

		Member owner = getMember(ownerId);
		Group group = getGroup(groupId);
		Set<Integer> memberIds = inviteRequest.memberIdList();

		validateOwnerPermission(owner, group, HAS_NOT_INVITE_PERMISSION);
		validateMaxMemberCount(memberIds);

		List<Member> members = memberRepository.findAllById(memberIds);
		validateAllMembersExist(members, memberIds);

		saveAllMemberGroup(members, group);
		members.forEach(member -> {
			/* 그룹 초대 알림 전송 */
			groupNotificationService.createGroupNotification(
				member.getId(), owner.getNickname(), group.getName(), group.getId()
			);
		});

		return new GroupIdResponse(group.getId());
	}

	@Override
	public MemberIdResponse forceOutGroupMember(int ownerId, int groupId, MemberIdRequest userIdRequest) {
		Member owner = getMember(ownerId);
		Group group = getGroup(groupId);
		validateOwnerPermission(owner, group, HAS_NOT_FORCE_OUT_PERMISSION);

		Member member = getMember(userIdRequest.memberId());
		memberGroupRepository.deleteByMemberIdAndGroupId(member.getId(), group.getId());
		return new MemberIdResponse(member.getId());
	}

	@Transactional
	@Override
	public void acceptGroupInvitation(int memberId, int groupId) {

		Member foundMember = getMember(memberId);
		Group foundGroup = getGroup(groupId);

		MemberGroup memberGroup = memberGroupRepository.findFirstPendingMemberGroupsByGroupId(foundMember.getId(),
				foundGroup.getId())
			.orElseThrow(() -> new GroupException(NOT_EXIST_PENDING_MEMBER_GROUP));

		memberGroup.setGroupInviteStateAccepted();
	}

	@Transactional
	@Override
	public void rejectGroupInvitation(int memberId, int groupId) {

		Member foundMember = getMember(memberId);
		Group foundGroup = getGroup(groupId);

		MemberGroup memberGroup = memberGroupRepository.findFirstPendingMemberGroupsByGroupId(foundMember.getId(),
				foundGroup.getId())
			.orElseThrow(() -> new GroupException(NOT_EXIST_PENDING_MEMBER_GROUP));

		memberGroupRepository.delete(memberGroup);
	}

	@Override
	public void leaveGroup(int memberId, int groupId) {

		Member foundMember = getMember(memberId);
		Group foundGroup = getGroup(groupId);

		if (isGroupOwner(foundMember, foundGroup)
			&& getMemberGroupsByGroupId(memberGroupRepository, groupId).size() > 1) {
			throw new GroupException(NOT_ACCEPTED_LEAVE_GROUP);
		}

		MemberGroup memberGroup = memberGroupRepository.findMemberGroupByMemberIdAndGroupId(
				foundMember.getId(), foundGroup.getId())
			.orElseThrow(() -> new GroupException(NOT_EXIST_MEMBER_GROUP));

		memberGroupRepository.delete(memberGroup);
	}

	private void saveAllMemberGroup(List<Member> members, Group group) {
		List<MemberGroup> memberGroups = new ArrayList<>();
		members.forEach(member -> {
				if (member.getMemberGroups().size() >= 50) {
					throw new CustomException(new CustomErrorCode(HttpStatus.BAD_REQUEST, "GROUP_CUSTOM",
						member.getEmail() + "해당 맴버는 인당 최대 50개의 그룹 생성을 초과했습니다."));
				}
				memberGroups.add(MemberGroup.of(member, group));
			}
		);
		memberGroupRepository.saveAll(memberGroups);
	}

	private void validateAllMembersExist(Collection<Member> members, Collection<Integer> memberIdList) {
		if (members.size() != memberIdList.size()) {
			throw new GroupException(EXISTS_INVALID_MEMBER);
		}
	}

	private void validateMaxMemberCount(Collection<?> members) {
		if (members.size() > 50) {
			throw new GroupException(EXCEED_MAX_MEMBER_COUNT);
		}
	}

	private static void validateOwnerPermission(Member owner, Group group, GroupErrorCode errorCode) {
		if (!isGroupOwner(owner, group)) {
			throw new GroupException(errorCode);
		}
	}

	private Member getMember(int ownerId) {
		return findById(memberRepository, ownerId);
	}

	private Group getGroup(int groupId) {
		return findById(groupRepository, groupId);
	}
}
