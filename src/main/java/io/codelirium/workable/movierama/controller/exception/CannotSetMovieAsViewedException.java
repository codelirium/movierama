package io.codelirium.workable.movierama.controller.exception;


public class CannotSetMovieAsViewedException extends RuntimeException {

	private static final long serialVersionUID = -6141236785008068567L;


	public CannotSetMovieAsViewedException(final String message, final Throwable cause) {

		super(message, cause);

	}
}
