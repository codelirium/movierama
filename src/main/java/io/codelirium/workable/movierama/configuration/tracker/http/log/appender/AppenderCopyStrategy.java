package io.codelirium.workable.movierama.configuration.tracker.http.log.appender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.OutputStreamAppender;
import ch.qos.logback.core.filter.Filter;
import java.util.List;

import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static org.springframework.core.GenericTypeResolver.resolveTypeArguments;


public abstract class AppenderCopyStrategy<A extends OutputStreamAppender<ILoggingEvent>> {

	private final Class<A> appenderClass;


	@SuppressWarnings("unchecked")
	AppenderCopyStrategy() {

		final Class<?>[] classes = resolveTypeArguments(getClass(), AppenderCopyStrategy.class);

		appenderClass = (Class<A>) classes[0];

	}


	private String copyAppenderName(final OutputStreamAppender<ILoggingEvent> consoleAppender) {

		return format("%s-Copy", consoleAppender.getName());

	}

	private void copyAppenderProperties(final OutputStreamAppender<ILoggingEvent> source, final OutputStreamAppender<ILoggingEvent> destination) {

		destination.setContext(source.getContext());
		destination.setName(copyAppenderName(source));

		final List<Filter<ILoggingEvent>> filtersList = source.getCopyOfAttachedFiltersList();

		filtersList.forEach(destination::addFilter);

	}

	public boolean canBuild(final OutputStreamAppender<ILoggingEvent> appender) {

		return nonNull(appender) && appenderClass.isInstance(appender);

	}

	@SuppressWarnings("unchecked")
	public A copy(final OutputStreamAppender<ILoggingEvent> appender) {

		final A aAppender   = (A) appender;
		final A newAppender = newAppender();

		copyAppenderProperties(aAppender, newAppender);
		copyOtherAppenderProperties(aAppender, newAppender);

		newAppender.setEncoder(aAppender.getEncoder());


		return newAppender;
	}


	abstract A newAppender();

	abstract void copyOtherAppenderProperties(final A source, final A destination);

}
