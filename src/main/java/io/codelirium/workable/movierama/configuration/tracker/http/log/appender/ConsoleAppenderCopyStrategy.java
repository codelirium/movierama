package io.codelirium.workable.movierama.configuration.tracker.http.log.appender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import org.springframework.stereotype.Component;


@Component
public class ConsoleAppenderCopyStrategy extends AppenderCopyStrategy<ConsoleAppender<ILoggingEvent>> {

	@Override
	ConsoleAppender<ILoggingEvent> newAppender() {

		return new ConsoleAppender<ILoggingEvent>();

	}

	@Override
	void copyOtherAppenderProperties(final ConsoleAppender<ILoggingEvent> source, final ConsoleAppender<ILoggingEvent> destination) {

		destination.setTarget(source.getTarget());

		destination.setWithJansi(source.isWithJansi());

	}
}
