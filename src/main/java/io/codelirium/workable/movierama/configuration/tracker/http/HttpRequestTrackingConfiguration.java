package io.codelirium.workable.movierama.configuration.tracker.http;

import io.codelirium.workable.movierama.configuration.tracker.http.component.HttpRequestTrackingInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import javax.inject.Inject;


@Configuration
class HttpRequestTrackingConfiguration extends WebMvcConfigurationSupport {

	@Inject
	private HttpRequestTrackingInterceptor interceptor;


	@Override
	public void addInterceptors(final InterceptorRegistry registry) {

		registry.addInterceptor(interceptor);

		super.addInterceptors(registry);

	}
}
