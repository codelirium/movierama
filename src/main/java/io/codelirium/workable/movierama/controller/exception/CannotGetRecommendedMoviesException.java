package io.codelirium.workable.movierama.controller.exception;


public class CannotGetRecommendedMoviesException extends RuntimeException {

	private static final long serialVersionUID = -6141236785008068569L;


	public CannotGetRecommendedMoviesException(final String message, final Throwable cause) {

		super(message, cause);

	}
}
