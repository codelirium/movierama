package io.codelirium.workable.movierama.controller.mapping;


public class UrlMappings {


	private UrlMappings() { }


	public static final String API_PATH_ROOT = "/api/movierama";


	public static final String PATH_PARAM_MOVIE_ID = "movieId";
	public static final String PATH_PARAM_USER_ID  = "userId";
	public static final String PATH_PARAM_RATING   = "rating";
	public static final String PATH_PARAM_PAGE     = "pageId";

	public static final String API_ENDPOINT_RATE_MOVIE_VIEWED      = "/users/{" + PATH_PARAM_USER_ID + "}/movies/{" + PATH_PARAM_MOVIE_ID + "}/ratings/{" + PATH_PARAM_RATING + "}";
	public static final String API_ENDPOINT_GET_MOVIES_VIEWED      = "/users/{" + PATH_PARAM_USER_ID + "}/movies/pages/{" + PATH_PARAM_PAGE + "}";
	public static final String API_ENDPOINT_GET_RECOMMENDED_MOVIES = "/users/{" + PATH_PARAM_USER_ID + "}/movies/recommendations";


	public static final String API_ERROR = "/error";

	public static final String MORE_ON_PATH = ".*";

}
