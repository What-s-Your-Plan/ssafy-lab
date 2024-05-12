package com.butter.wypl.group.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.butter.wypl.auth.annotation.Authenticated;
import com.butter.wypl.auth.domain.AuthMember;
import com.butter.wypl.global.common.Message;
import com.butter.wypl.group.data.request.GroupCreateRequest;
import com.butter.wypl.group.data.request.GroupUpdateRequest;
import com.butter.wypl.group.data.response.GroupDetailResponse;
import com.butter.wypl.group.data.response.GroupIdResponse;
import com.butter.wypl.group.data.response.GroupListByMemberIdResponse;
import com.butter.wypl.group.service.GroupLoadService;
import com.butter.wypl.group.service.GroupModifyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/group")
@RequiredArgsConstructor
public class GroupController {

	private final GroupModifyService groupModifyService;
	private final GroupLoadService groupLoadService;

	@PostMapping("/v1/groups")
	public ResponseEntity<Message<GroupIdResponse>> createGroup(@Authenticated AuthMember authMember,
		@RequestBody GroupCreateRequest createRequest) {
		return ResponseEntity.status(HttpStatus.CREATED).body(
			Message.withBody("그룹 등록에 성공했습니다.", groupModifyService.createGroup(authMember.getId(), createRequest)));
	}

	@GetMapping("/v1/groups/{groupId}")
	public ResponseEntity<Message<GroupDetailResponse>> getGroupDetail(@Authenticated AuthMember authMember,
		@PathVariable int groupId) {
		return ResponseEntity.ok(
			Message.withBody("그룹 조회에 성공했습니다.", groupLoadService.getDetailById(authMember.getId(), groupId)));
	}

	@PatchMapping("/v1/groups/{groupId}")
	public ResponseEntity<Message<GroupIdResponse>> updateGroup(@Authenticated AuthMember authMember,
		@PathVariable int groupId,
		@RequestBody GroupUpdateRequest updateRequest) {
		return ResponseEntity.ok(Message.withBody("그룹 수정에 성공했습니다.",
			groupModifyService.updateGroup(authMember.getId(), groupId, updateRequest)));
	}

	@DeleteMapping("/v1/groups/{groupId}")
	public ResponseEntity<Message<Void>> deleteGroup(@Authenticated AuthMember authMember, @PathVariable int groupId) {
		groupModifyService.deleteGroup(authMember.getId(), groupId);
		return ResponseEntity.ok(Message.onlyMessage("그룹 삭제에 성공했습니다."));
	}

	@GetMapping("/v1/groups/members")
	public ResponseEntity<Message<GroupListByMemberIdResponse>> getGroupListByMemberId(
		@Authenticated AuthMember authMember) {
		return ResponseEntity.ok(
			Message.withBody("회원의 그룹 전체 조회에 성공했습니다.", groupLoadService.getGroupListByMemberId(authMember.getId())));
	}

	@PutMapping("/v1/groups/{groupId}/members/invitation")
	public ResponseEntity<Message<Void>> acceptGroupInvitation(@Authenticated AuthMember authMember,
		@PathVariable int groupId) {
		groupModifyService.acceptGroupInvitation(authMember.getId(), groupId);
		return ResponseEntity.ok(Message.onlyMessage("그룹 멤버로 등록됐습니다."));
	}

	@DeleteMapping("/v1/groups/{groupId}/members/invitation")
	public ResponseEntity<Message<Void>> rejectGroupInvitation(@Authenticated AuthMember authMember,
		@PathVariable int groupId) {
		groupModifyService.rejectGroupInvitation(authMember.getId(), groupId);
		return ResponseEntity.ok(Message.onlyMessage("그룹 멤버 초대를 거절했습니다."));
	}

	@DeleteMapping("/v1/groups/{groupId}/members")
	public ResponseEntity<Message<Void>> leaveGroup(@Authenticated AuthMember authMember,
		@PathVariable int groupId) {
		groupModifyService.leaveGroup(authMember.getId(), groupId);
		return ResponseEntity.ok(Message.onlyMessage("그룹을 탈퇴했습니다."));
	}

}
