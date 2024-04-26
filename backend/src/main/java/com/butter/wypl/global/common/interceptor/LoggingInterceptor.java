package com.butter.wypl.global.common.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class LoggingInterceptor implements HandlerInterceptor {

	private static final ThreadLocal<Long> startTime = new ThreadLocal<>();

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
		Exception {
		log.info("요청 URL => {}", request.getRequestURL());
		startTime.set(System.currentTimeMillis());

		return HandlerInterceptor.super.preHandle(request, response, handler);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
		Exception ex) throws Exception {
		long duration = System.currentTimeMillis() - startTime.get();
		startTime.remove();

		log.info("요청 URL => {}, status => {}, 처리 시간 => {}ms", request.getRequestURL(), response.getStatus(),
			duration);

		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
}
