package io.codelirium.workable.movierama.configuration.tracker.http.log.appender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;
import org.springframework.stereotype.Component;


@Component
public class RollingFileAppenderCopyStrategy extends AppenderCopyStrategy<RollingFileAppender<ILoggingEvent>> {

	@Override
	RollingFileAppender<ILoggingEvent> newAppender() {

		return new RollingFileAppender<>();

	}

	@Override
	void copyOtherAppenderProperties(final RollingFileAppender<ILoggingEvent> source, final RollingFileAppender<ILoggingEvent> destination) {

		destination.setFile(source.getFile());
		destination.setPrudent(source.isPrudent());
		destination.setAppend(source.isAppend());
		destination.setRollingPolicy(source.getRollingPolicy());
		destination.setTriggeringPolicy(source.getTriggeringPolicy());

	}
}
