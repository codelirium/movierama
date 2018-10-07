package io.codelirium.workable.movierama.controller;

import io.codelirium.workable.movierama.controller.exception.CannotGetMoviesViewedException;
import io.codelirium.workable.movierama.controller.exception.CannotGetRecommendedMoviesException;
import io.codelirium.workable.movierama.controller.exception.CannotSetMovieAsViewedException;
import io.codelirium.workable.movierama.model.dto.MovieDTO;
import io.codelirium.workable.movierama.model.dto.pagination.PagedSearchDTO;
import io.codelirium.workable.movierama.model.dto.response.RESTFailureResponseBody;
import io.codelirium.workable.movierama.model.dto.response.RESTResponseBody;
import io.codelirium.workable.movierama.model.dto.response.RESTSuccessResponseBody;
import io.codelirium.workable.movierama.service.MovieramaService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.inject.Inject;
import java.util.Collection;

import static io.codelirium.workable.movierama.configuration.swagger.SwaggerConfiguration.*;
import static io.codelirium.workable.movierama.controller.mapping.UrlMappings.*;
import static io.codelirium.workable.movierama.model.dto.response.builder.RESTResponseBodyBuilder.success;
import static io.codelirium.workable.movierama.model.entity.MovieEntity.COLUMN_NAME_TITLE;
import static java.util.Collections.emptyList;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;


@RestController
@RequestMapping(API_PATH_ROOT)
public class MovieramaController {

	private static final Logger LOGGER = getLogger(MovieramaController.class);


	private final MovieramaService movieramaService;


	@Inject
	public MovieramaController(final MovieramaService movieramaService) {

		this.movieramaService = movieramaService;

	}


	@ApiOperation(response = RESTResponseBody.class,
				  produces = APPLICATION_JSON_VALUE,
				  value    = SWAGGER_API_OPERATION_RATE_MOVIE_VIEWED,
				  notes    = SWAGGER_API_OPERATION_RATE_MOVIE_VIEWED_NOTES
	)
	@ApiResponses(value = {
			@ApiResponse(code     = 200,
						 response = RESTSuccessResponseBody.class,
						 message  = SWAGGER_API_RESPONSE_GENERIC_200_MESSAGE),
			@ApiResponse(code     = 400,
						 response = RESTFailureResponseBody.class,
						 message  = SWAGGER_API_RESPONSE_GENERIC_400_MESSAGE),
			@ApiResponse(code     = 415,
						 response = RESTFailureResponseBody.class,
						 message  = SWAGGER_API_RESPONSE_GENERIC_415_MESSAGE),
			@ApiResponse(code     = 500,
						 response = RESTFailureResponseBody.class,
						 message  = SWAGGER_API_RESPONSE_GENERIC_500_MESSAGE)
	})
	@ResponseStatus(OK)
	@RequestMapping(value = API_ENDPOINT_RATE_MOVIE_VIEWED, method = POST, produces = APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<RESTSuccessResponseBody<Void>> rateViewed(@PathVariable(PATH_PARAM_MOVIE_ID) final int movieId,
																				  @PathVariable(PATH_PARAM_USER_ID)  final int userId,
																				  @PathVariable(PATH_PARAM_RATING)   final int rating) {

		try {

			movieramaService.rateMovieViewed(movieId, userId, rating);

		} catch (final Exception e) {

			throw new CannotSetMovieAsViewedException(e.getMessage(), e.getCause());

		}


		LOGGER.debug("Building response for marking movie as viewed ...");

		final RESTSuccessResponseBody<Void> body = success(RESTSuccessResponseBody.class.getSimpleName(), emptyList());

		LOGGER.debug("Response body for marking movie as viewed was built successfully.");


		return new ResponseEntity<>(body, OK);
	}


	@ApiOperation(response = RESTResponseBody.class,
				  produces = APPLICATION_JSON_VALUE,
				  value    = SWAGGER_API_OPERATION_GET_VIEWED,
				  notes    = SWAGGER_API_OPERATION_GET_VIEWED_NOTES
	)
	@ApiResponses(value = {
			@ApiResponse(code     = 200,
						 response = RESTSuccessResponseBody.class,
						 message  = SWAGGER_API_RESPONSE_GENERIC_200_MESSAGE),
			@ApiResponse(code     = 400,
						 response = RESTFailureResponseBody.class,
						 message  = SWAGGER_API_RESPONSE_GENERIC_400_MESSAGE),
			@ApiResponse(code     = 415,
						 response = RESTFailureResponseBody.class,
						 message  = SWAGGER_API_RESPONSE_GENERIC_415_MESSAGE),
			@ApiResponse(code     = 500,
						 response = RESTFailureResponseBody.class,
						 message  = SWAGGER_API_RESPONSE_GENERIC_500_MESSAGE)
	})
	@ResponseStatus(OK)
	@RequestMapping(value = API_ENDPOINT_GET_MOVIES_VIEWED, method = GET, produces = APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<RESTSuccessResponseBody<MovieDTO>> getViewed(@PathVariable(PATH_PARAM_USER_ID) final int userId,
																					 @PathVariable(PATH_PARAM_PAGE) final int page) {

		final PagedSearchDTO searchDTO = new PagedSearchDTO(page, COLUMN_NAME_TITLE);


		Collection<MovieDTO> moviesViewed;

		try {

			moviesViewed = movieramaService.getMoviesViewed(searchDTO, userId);

		} catch (final Exception e) {

			throw new CannotGetMoviesViewedException(e.getMessage(), e.getCause());

		}


		LOGGER.debug("Building response for movies viewed ...");

		final RESTSuccessResponseBody<MovieDTO> body = success(MovieDTO.class.getSimpleName(), moviesViewed, searchDTO);

		LOGGER.debug("Response body for movies viewed was built successfully.");


		return new ResponseEntity<>(body, OK);
	}


	@ApiOperation(response = RESTResponseBody.class,
				  produces = APPLICATION_JSON_VALUE,
				  value    = SWAGGER_API_OPERATION_GET_RECOMMENDED,
				  notes    = SWAGGER_API_OPERATION_GET_RECOMMENDED_NOTES
	)
	@ApiResponses(value = {
			@ApiResponse(code     = 200,
						 response = RESTSuccessResponseBody.class,
						 message  = SWAGGER_API_RESPONSE_GENERIC_200_MESSAGE),
			@ApiResponse(code     = 400,
						 response = RESTFailureResponseBody.class,
						 message  = SWAGGER_API_RESPONSE_GENERIC_400_MESSAGE),
			@ApiResponse(code     = 415,
						 response = RESTFailureResponseBody.class,
						 message  = SWAGGER_API_RESPONSE_GENERIC_415_MESSAGE),
			@ApiResponse(code     = 500,
						 response = RESTFailureResponseBody.class,
						 message  = SWAGGER_API_RESPONSE_GENERIC_500_MESSAGE)
	})
	@ResponseStatus(OK)
	@RequestMapping(value = API_ENDPOINT_GET_RECOMMENDED_MOVIES, method = GET, produces = APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<RESTSuccessResponseBody<MovieDTO>> getRecommended(@PathVariable(PATH_PARAM_USER_ID) final int userId) {

		Collection<MovieDTO> recommendations;

		try {

			recommendations = movieramaService.getRecommendedMovies(userId);

		} catch (final Exception e) {

			throw new CannotGetRecommendedMoviesException(e.getMessage(), e.getCause());

		}


		LOGGER.debug("Building response for recommended movies ...");

		final RESTSuccessResponseBody<MovieDTO> body = success(MovieDTO.class.getSimpleName(), recommendations);

		LOGGER.debug("Response body for recommended movies was built successfully.");


		return new ResponseEntity<>(body, OK);
	}
}
