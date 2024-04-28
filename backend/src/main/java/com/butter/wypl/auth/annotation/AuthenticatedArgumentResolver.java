package com.butter.wypl.auth.annotation;

import java.util.Objects;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.butter.wypl.auth.domain.AuthMember;
import com.butter.wypl.auth.exception.AuthErrorCode;
import com.butter.wypl.auth.exception.AuthException;
import com.butter.wypl.auth.utils.JwtProvider;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class AuthenticatedArgumentResolver implements HandlerMethodArgumentResolver {

	private static final String HEADER_AUTHORIZATION = "Authorization";
	private static final String BEARER_TOKEN = "Bearer ";

	private final JwtProvider jwtProvider;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		boolean hasParameterAnnotation = parameter.hasParameterAnnotation(Authenticated.class);
		boolean assignableFrom = AuthMember.class.isAssignableFrom(parameter.getParameterType());
		return hasParameterAnnotation && assignableFrom;
	}

	@Override
	public AuthMember resolveArgument(
			MethodParameter parameter,
			ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory
	) {
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		String authorization = Objects.requireNonNull(request).getHeader(HEADER_AUTHORIZATION);
		if (authorization.startsWith(BEARER_TOKEN)) {
			String token = authorization.substring(BEARER_TOKEN.length()).trim();
			int memberId = jwtProvider.getPayload(token);
			return AuthMember.from(memberId);
		}
		throw new AuthException(AuthErrorCode.NOT_AUTHORIZATION_MEMBER);
	}
}
