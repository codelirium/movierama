package io.codelirium.workable.movierama.component.client;

import java.io.UnsupportedEncodingException;


public interface ExternalMovieAPIClient {

	String getMovieDescription(final String movieTitle) throws UnsupportedEncodingException;

}
