package com.butter.wypl.member.exception;

import org.springframework.http.HttpStatus;

import com.butter.wypl.global.exception.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MemberErrorCode implements ErrorCode {
	TOO_LONG_CONTENT(HttpStatus.BAD_REQUEST, "MEMBER_001", "내용이 너무 깁니다."),
	NOT_EXIST_MEMBER(HttpStatus.BAD_REQUEST, "MEMBER_002", "존재하지 않는 사용자입니다.");

	private final HttpStatus httpStatus;
	private final String errorCode;
	private final String message;

	public int getStatusCode() {
		return httpStatus.value();
	}
}
