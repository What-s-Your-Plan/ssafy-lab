package com.butter.wypl.group.data.response;

import java.util.List;

import com.butter.wypl.member.domain.Member;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public record MemberResponse(

	@JsonProperty("member_id")
	int memberId,

	String email,

	String nickname,

	@JsonProperty("profile_image")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	String profileImage

) {

	public static MemberResponse from(Member member) {
		return new MemberResponse(member.getId(), member.getEmail(), member.getNickname(), member.getProfileImage());
	}

	public static List<MemberResponse> from(List<Member> members) {
		return members.stream().map(MemberResponse::from)
			.toList();
	}
}
