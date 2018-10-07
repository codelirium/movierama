package io.codelirium.workable.movierama.configuration.tracker.db;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;


@Aspect
@Component
@EnableAspectJAutoProxy
class SQLQueryTrackingAspect {

	private static final Logger LOGGER = getLogger(SQLQueryTrackingAspect.class);


	@Value("${tracker.sql-query.success}")
	private String logFormatSuccess;

	@Value("${tracker.sql-query.failure}")
	private String logFormatFailure;


	@Pointcut("execution(public * org.springframework.data.repository.Repository+.*(..))")
	public void monitor() { }


	@Around(value = "monitor()")
	public Object profile(final ProceedingJoinPoint joinPoint) throws Throwable {

		final String name = format("%s.%s", joinPoint.getTarget().getClass().getName(),
											joinPoint.getSignature().toShortString());


		final StopWatch stopWatch = new StopWatch(name);

		stopWatch.start(name);


		Object result;

		try {

			result = joinPoint.proceed();

		} catch (final Exception e) {

			stopWatch.stop();

			LOGGER.error(logFormatFailure,  joinPoint.getSignature().toShortString(),
											stopWatch.getTotalTimeMillis(),
											e);


			throw e;
		}


		stopWatch.stop();

		LOGGER.debug(logFormatSuccess, joinPoint.getSignature().toShortString(), stopWatch.getTotalTimeMillis());


		return result;
	}
}
