package io.codelirium.workable.movierama.configuration.tracker.http.component;

import ch.qos.logback.classic.Level;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Getter
@Component
class HttpRequestTrackingProperties {

	@Value("${tracker.http-request.level}")
	private Level loggingLevel;

	@Value("${tracker.http-request.pattern}")
	private String encoderPattern;

	@Value("${tracker.http-request.context}")
	private String loggingContext;

	@Value("${tracker.http-request.begin}")
	private String beginMessageFormat;

	@Value("${tracker.http-request.end}")
	private String endMessageFormat;

	@Value("${tracker.http-request.attribute.startTime}")
	private String startTimeAttribute;

	@Value("${tracker.http-request.pattern.uid}")
	private String mdcUid;

	@Value("${tracker.http-request.user.null}")
	private String nullUser;

}
