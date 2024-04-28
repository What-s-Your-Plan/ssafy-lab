package com.butter.wypl.member.utils;

import com.butter.wypl.global.exception.CallConstructorException;
import com.butter.wypl.member.domain.Member;
import com.butter.wypl.member.exception.MemberErrorCode;
import com.butter.wypl.member.exception.MemberException;
import com.butter.wypl.member.repository.MemberRepository;

public class MemberServiceUtils {

	private MemberServiceUtils() {
		throw new CallConstructorException();
	}

	public static Member findByEmail(
			final MemberRepository memberRepository,
			final String email
	) {
		return memberRepository.findByEmail(email)
				.orElseThrow(() -> new MemberException(MemberErrorCode.NOT_EXIST_MEMBER));
	}
}
