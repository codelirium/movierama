package io.codelirium.workable.movierama.configuration.tracker.http.component;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.OutputStreamAppender;
import io.codelirium.workable.movierama.configuration.tracker.http.log.appender.AppenderCopyStrategy;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newLinkedList;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static org.slf4j.Logger.ROOT_LOGGER_NAME;
import static org.slf4j.LoggerFactory.getLogger;


@Component
public class HttpRequestTrackingLoggerRegistrar {

	@Inject
	private HttpRequestTrackingProperties properties;

	@Inject
	private List<AppenderCopyStrategy> copyStrategies = newLinkedList();


	@PostConstruct
	public void init() {

		final Logger rootLogger = (Logger) getLogger(ROOT_LOGGER_NAME);

		final Iterator<Appender<ILoggingEvent>> appenders = rootLogger.iteratorForAppenders();

		final List<OutputStreamAppender<ILoggingEvent>> appenderList = newLinkedList();


		while (appenders.hasNext()) {

			final Appender<ILoggingEvent> appender = appenders.next();

			if (appender instanceof OutputStreamAppender) {

				final Optional<OutputStreamAppender<ILoggingEvent>> copyOfAppender = copyOfAppender((OutputStreamAppender<ILoggingEvent>) appender);

				if (copyOfAppender.isPresent()) {

					appenderList.add(copyOfAppender.get());

				} else {

					throw new UnsupportedOperationException(format("Incompatible OutputStreamAppender found: [%s] - register a AppenderCopyStrategy @Component that canBuild it", appender.getClass().getName()));

				}
			}
		}


		final Logger logger = (Logger) getLogger(properties.getLoggingContext());

		logger.setLevel(properties.getLoggingLevel());
		logger.setAdditive(false);

		removeAppenders(logger);

		addAppenders(logger, appenderList);

	}

	private void addAppenders(final Logger logger, final List<OutputStreamAppender<ILoggingEvent>> appenderList) {

		final LoggerContext loggerContext = logger.getLoggerContext();

		for (final OutputStreamAppender<ILoggingEvent> newAppender : appenderList) {

			final PatternLayoutEncoder encoder = new PatternLayoutEncoder();

			encoder.setContext(loggerContext);
			encoder.setPattern(properties.getEncoderPattern());
			encoder.start();

			newAppender.setContext(loggerContext);
			newAppender.setEncoder(encoder);
			newAppender.start();

			logger.addAppender(newAppender);

		}
	}

	private void removeAppenders(final Logger logger) {

		final Iterator<Appender<ILoggingEvent>> appenderIterator = logger.iteratorForAppenders();

		while (appenderIterator.hasNext()) {

			logger.detachAppender(appenderIterator.next());

		}
	}

	@SuppressWarnings("unchecked")
	private <A extends OutputStreamAppender<ILoggingEvent>> Optional<A> copyOfAppender(final A appender) {

		A newAppender = null;

		for (final AppenderCopyStrategy copyStrategy : copyStrategies) {

			if (copyStrategy.canBuild(appender)) {

				newAppender = (A) copyStrategy.copy(appender);

			}

		}


		return ofNullable(newAppender);
	}
}
