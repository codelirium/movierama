package io.codelirium.workable.movierama.configuration.tracker.http.component;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static java.lang.String.valueOf;
import static java.lang.System.nanoTime;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static org.slf4j.LoggerFactory.getLogger;
import static org.slf4j.MDC.*;


@Component
public class HttpRequestTrackingInterceptor extends HandlerInterceptorAdapter {

	private Logger LOGGER;


	private static final String X_FORWARDED_FOR = "X-FORWARDED-FOR";


	@Inject
	private HttpRequestTrackingProperties properties;


	@PostConstruct
	public void init() {

		LOGGER = getLogger(properties.getLoggingContext());

	}


	@Override
	public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {

		final long startTime = nanoTime();

		final String threadId  = valueOf(Thread.currentThread().getId());
		final String reqName   = request.getServletPath();
		final String userName  = getUserName(request);
		final String ipAddress = getIpAddress(request);

		put(properties.getMdcUid(), threadId);

		LOGGER.info(properties.getBeginMessageFormat(), userName, ipAddress, reqName);

		clearMDC();

		request.setAttribute(properties.getStartTimeAttribute(), startTime);


		return super.preHandle(request, response, handler);
	}

	private String getUserName(final HttpServletRequest request) {

		return nonNull(request.getRemoteUser()) ? request.getRemoteUser() : properties.getNullUser();

	}

	private String getIpAddress(final HttpServletRequest request) {

		String ipAddress = request.getHeader(X_FORWARDED_FOR);

		if (isNull(ipAddress)) {

			ipAddress = request.getRemoteAddr();

		}


		return ipAddress;
	}

	@Override
	public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final Exception e) throws Exception {

		final long endTime     = nanoTime();
		final long startTime   = (Long) request.getAttribute(properties.getStartTimeAttribute());
		final long elapsedTime = endTime - startTime;
		final long reqTime     = NANOSECONDS.toMillis(elapsedTime);

		final String threadId  = valueOf(Thread.currentThread().getId());
		final String reqName   = request.getServletPath();
		final String userName  = getUserName(request);
		final String ipAddress = getIpAddress(request);

		put(properties.getMdcUid(), threadId);

		LOGGER.info(properties.getEndMessageFormat(), userName, ipAddress, reqName, reqTime);

		clearMDC();

		super.afterCompletion(request, response, handler, e);

	}

	private void clearMDC() {

		remove(properties.getMdcUid());

		clear();

	}
}
