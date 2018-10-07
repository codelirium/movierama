package io.codelirium.workable.movierama.controller.exception;


public class CannotGetMoviesViewedException extends RuntimeException {

	private static final long serialVersionUID = -6141236785008068568L;


	public CannotGetMoviesViewedException(final String message, final Throwable cause) {

		super(message, cause);

	}
}
