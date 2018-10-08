package io.codelirium.workable.movierama.util.client;

import io.codelirium.workable.movierama.component.client.ExternalMovieAPIClient;

import static io.codelirium.workable.movierama.component.client.tmdb.TheMovieDBClient.MSG_UNAVAILABLE;
import static org.springframework.util.Assert.notNull;


public class MovieDescriptionFetcher {

	private MovieDescriptionFetcher() { }


	public static String getMovieDescription(final ExternalMovieAPIClient client, final String movieTitle) {

		notNull(client, "The client cannot be null.");
		notNull(movieTitle, "The movie title cannot be null.");


		try {

			return client.getMovieDescription(movieTitle);

		} catch (final Exception e) {

			return MSG_UNAVAILABLE;

		}
	}
}
