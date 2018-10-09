package io.codelirium.workable.movierama.component.client;

import java.io.UnsupportedEncodingException;


public interface ExternalMovieAPIClient {

	final String MSG_UNAVAILABLE = "Unavailable.";


	String getMovieDescription(final String movieTitle) throws UnsupportedEncodingException;

}
