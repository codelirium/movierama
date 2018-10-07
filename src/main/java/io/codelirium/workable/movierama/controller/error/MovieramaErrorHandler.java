package io.codelirium.workable.movierama.controller.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.codelirium.workable.movierama.model.dto.response.RESTFailureResponseBody;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static io.codelirium.workable.movierama.controller.mapping.UrlMappings.API_ERROR;
import static io.codelirium.workable.movierama.model.dto.response.builder.RESTResponseBodyBuilder.failure;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_HTML_VALUE;


@ControllerAdvice
public class MovieramaErrorHandler extends BasicErrorController {

	private static final Logger LOGGER = getLogger(MovieramaErrorHandler.class);


	private static final String ERROR_BEGIN_PREFIX_MESSAGE = "ERROR::HANDLER::BEGIN";
	private static final String ERROR_END_PREFIX_MESSAGE   = "ERROR::HANDLER::END";

	private static final String RESPONSE_MESSAGE_NOT_FOUND = "Not Found";


	private final ObjectMapper objectMapper;


	@Inject
	public MovieramaErrorHandler(final ObjectMapper objectMapper) {

		super(new DefaultErrorAttributes(), new ErrorProperties());

		this.objectMapper = objectMapper;

	}


	@Override
	@ResponseStatus(NOT_FOUND)
	@RequestMapping(value = API_ERROR, produces = APPLICATION_JSON_VALUE)
	@SuppressWarnings("unchecked")
	public @ResponseBody ResponseEntity<Map<String, Object>> error(final HttpServletRequest request) {

		LOGGER.info("{} - error", ERROR_BEGIN_PREFIX_MESSAGE);


		try {

			LOGGER.warn("The Error Handler caught a request to an invalid URL Path.");


			final HttpStatus httpStatus = NOT_FOUND;


			LOGGER.debug("Building custom {} response body ...", httpStatus.toString());

			final RESTFailureResponseBody<String> body = failure(httpStatus.toString(), RESPONSE_MESSAGE_NOT_FOUND);

			LOGGER.debug("Custom {} response body was built successfully.", httpStatus.toString());


			return new ResponseEntity<>(objectMapper.convertValue(body, Map.class), httpStatus);

		} finally {

			LOGGER.info("{} - error", ERROR_END_PREFIX_MESSAGE);

		}
	}


	@Override
	@ResponseStatus(NOT_FOUND)
	@RequestMapping(value = API_ERROR, produces = TEXT_HTML_VALUE)
	@SuppressWarnings("unchecked")
	public @ResponseBody ModelAndView errorHtml(final HttpServletRequest request, final HttpServletResponse response) {

		LOGGER.info("{} - errorHtml", ERROR_BEGIN_PREFIX_MESSAGE);


		try {

			LOGGER.warn("The Error Handler caught a browser request to an invalid URL Path.");


			final HttpStatus httpStatus = NOT_FOUND;


			LOGGER.debug("Building custom browser {} response body ...", httpStatus.toString());

			final RESTFailureResponseBody<String> body = failure(httpStatus.toString(), RESPONSE_MESSAGE_NOT_FOUND);

			LOGGER.debug("Custom browser {} response body was built successfully.", httpStatus.toString());


			final MappingJackson2JsonView jsonView = new MappingJackson2JsonView();

			jsonView.setPrettyPrint(true);


			return new ModelAndView(jsonView, objectMapper.convertValue(body, Map.class));

		} finally {

			LOGGER.info("{} - errorHtml", ERROR_END_PREFIX_MESSAGE);

		}
	}


	@Override
	public String getErrorPath() {

		return API_ERROR;

	}


	@ExceptionHandler(Exception.class)
	public @ResponseBody ResponseEntity<RESTFailureResponseBody<String>> handleException(final Exception e) {

		LOGGER.info("{} - {}", ERROR_BEGIN_PREFIX_MESSAGE, e.getClass().getSimpleName());


		try {

			LOGGER.warn("The Error Handler caught an exception:");


			LOGGER.error(e.getMessage(), e);


			RESTFailureResponseBody<String> body = failure(e.getClass().getSimpleName(), e.getMessage());

			HttpStatus status = INTERNAL_SERVER_ERROR;


			if (e instanceof MethodArgumentNotValidException) {

				body   = failure(e.getClass().getSimpleName(), ((MethodArgumentNotValidException) e).getBindingResult().getAllErrors().toString());

				status = BAD_REQUEST;

			} else if (e instanceof HttpMessageNotReadableException) {

				body   = failure(e.getClass().getSimpleName(), "There is a syntactic error in the request body.");

				status = BAD_REQUEST;

			} else if (e instanceof HttpMediaTypeNotSupportedException) {

				body   = failure(e.getClass().getSimpleName(), "The http media type of the request is not supported.");

				status = UNSUPPORTED_MEDIA_TYPE;

			} else if (e instanceof HttpMediaTypeNotAcceptableException) {

				body   = failure(e.getClass().getSimpleName(), "The http media type of the request is not acceptable.");

				status = UNSUPPORTED_MEDIA_TYPE;

			} else if (e instanceof HttpRequestMethodNotSupportedException) {

				body   = failure(e.getClass().getSimpleName(), "The http request method is not supported.");

				status = BAD_REQUEST;

			}


			return new ResponseEntity<>(body, status);

		} finally {

			LOGGER.info("{} - {}", ERROR_END_PREFIX_MESSAGE,  e.getClass().getSimpleName());

		}
	}
}
