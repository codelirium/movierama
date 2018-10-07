package io.codelirium.workable.movierama.model.dto.pagination;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.beans.support.SortDefinition;
import org.springframework.data.domain.Sort.Order;
import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.data.domain.Sort.Order.by;


@Data
@ToString
@ApiModel
@EqualsAndHashCode
@JsonInclude(NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDTO implements SortDefinition, Serializable {

	private static final long serialVersionUID = -7724242696257458519L;


	private static final boolean DEFAULT_IGNORE_CASE = true;

	private String  property;
	private boolean ascending;
	private boolean ignoreCase;


	private OrderDTO(final String property,
					 final boolean ascending,
					 final boolean ignoreCase) {

		this.property   = property;
		this.ascending  = ascending;
		this.ignoreCase = ignoreCase;
	}

	OrderDTO(final String property, final boolean ascending) {

		this(property, ascending, DEFAULT_IGNORE_CASE);

	}


	@Override
	public String getProperty() {

		return property;

	}


	@Override
	public boolean isIgnoreCase() {

		return ignoreCase;

	}


	@Override
	public boolean isAscending() {

		return ascending;

	}


	@JsonIgnore
	public Order getOrder() {

		final Order order = by(property).with(ascending ? ASC : DESC);

		if (ignoreCase) {

			return order.ignoreCase();

		}


		return order;
	}
}
