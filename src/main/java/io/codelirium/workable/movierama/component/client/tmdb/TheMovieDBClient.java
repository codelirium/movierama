package io.codelirium.workable.movierama.component.client.tmdb;

import io.codelirium.workable.movierama.component.client.ExternalMovieAPIClient;
import io.codelirium.workable.movierama.component.client.tmdb.model.ResultsPage;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import static java.net.URLEncoder.encode;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.util.Assert.notNull;


@Component
@EnableRetry
public class TheMovieDBClient implements ExternalMovieAPIClient {

	private static final Logger LOGGER = getLogger(TheMovieDBClient.class);


	public  static final String MSG_UNAVAILABLE = "Unavailable.";
	private static final String FIELD_OVERVIEW = "overview";


	@Value("${external.api.themoviedb.key}")
	private String apiKey;

	@Value("${external.api.themoviedb.endpoint.search}")
	private String endpointSearchWithPlaceholders;

	@Value("${external.api.themoviedb.placeholder.key}")
	private String apiKeyPlaceholder;

	@Value("${external.api.themoviedb.placeholder.movie-title}")
	private String movieTitlePlaceholder;

	@Value("${external.api.themoviedb.client.timeout}")
	private Integer timeout;

	private RestTemplate restTemplate;


	@PostConstruct
	private void init() {

		restTemplate = new RestTemplateBuilder()
									.setConnectTimeout(timeout)
									.setReadTimeout(timeout)
									.build();

	}


	@Override
	@SuppressWarnings("unchecked")
	@Retryable(value = { RestClientResponseException.class }, maxAttempts = 15, backoff = @Backoff(delay = 2000))
	public String getMovieDescription(final String movieTitle) throws UnsupportedEncodingException {

		notNull(movieTitle, "The movie title cannot be null.");


		final String endpointSearch = endpointSearchWithPlaceholders
														.replace(apiKeyPlaceholder, apiKey)
														.replace(movieTitlePlaceholder, encode(movieTitle, UTF_8.toString()));


		LOGGER.debug("Fetching title for movie: [" + movieTitle + "] - via: " + endpointSearch);


		final ResultsPage<Map> page = restTemplate.getForObject(endpointSearch, ResultsPage.class);

		if (!page.getResults().isEmpty()) {

			return page.getResults().get(0).get(FIELD_OVERVIEW).toString();

		}


		return MSG_UNAVAILABLE;
	}
}
