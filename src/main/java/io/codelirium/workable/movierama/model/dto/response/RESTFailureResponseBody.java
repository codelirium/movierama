package io.codelirium.workable.movierama.model.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;


@Data
@ToString
@ApiModel
@EqualsAndHashCode
@JsonInclude(NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RESTFailureResponseBody<T> extends RESTResponseBody {

	private static final long serialVersionUID = -8372376590183501108L;


	private T message;


	public RESTFailureResponseBody() {

		super();

	}
}
