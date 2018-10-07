package io.codelirium.workable.movierama.component.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.io.Serializable;
import java.util.List;


@Data
@ToString
@EqualsAndHashCode
public class ResultsPage<T> implements Serializable {

	private static final long serialVersionUID = -3294029336245813645L;


	@JsonProperty("results")
	private List<T> results;

}
