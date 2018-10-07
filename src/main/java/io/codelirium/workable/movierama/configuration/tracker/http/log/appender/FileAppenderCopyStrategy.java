package io.codelirium.workable.movierama.configuration.tracker.http.log.appender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;
import org.springframework.stereotype.Component;


@Component
public class FileAppenderCopyStrategy extends AppenderCopyStrategy<FileAppender<ILoggingEvent>> {

	@Override
	FileAppender<ILoggingEvent> newAppender() {

		return new FileAppender<>();

	}

	@Override
	void copyOtherAppenderProperties(final FileAppender source, final FileAppender destination) {

		destination.setFile(source.getFile());
		destination.setPrudent(source.isPrudent());
		destination.setAppend(source.isAppend());

	}
}
