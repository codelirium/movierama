package io.codelirium.workable.movierama.model.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.util.Collection;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;


@Data
@ToString
@ApiModel
@EqualsAndHashCode
@JsonInclude(NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "status", "type", "search", "count", "data" })
public class RESTSuccessResponseBody<T> extends RESTSearchResponseBody {

	private static final long serialVersionUID = -829911228038138150L;


	private Integer       count;

	private Collection<T> data;


	public RESTSuccessResponseBody() {

		super();

	}
}
