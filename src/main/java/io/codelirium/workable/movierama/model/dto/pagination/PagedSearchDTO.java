package io.codelirium.workable.movierama.model.dto.pagination;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.io.Serializable;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static io.codelirium.workable.movierama.model.entity.base.PersistableBaseEntity.FIELD_NAME_ID;
import static java.util.Collections.singletonList;


@Data
@ToString
@ApiModel
@EqualsAndHashCode
@JsonInclude(NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PagedSearchDTO implements Serializable {

	private static final long serialVersionUID = -241999448563833241L;


	private static final String  DEFAULT_SORT_PROPERTY = FIELD_NAME_ID;
	private static final Integer DEFAULT_PAGE_SIZE     = 10;
	private static final Integer START_PAGE            = 1;


	private Integer        page;
	private Integer        pageSize;
	private Integer        totalPages;
	private Long           totalElements;
	private List<OrderDTO> sort;


	public PagedSearchDTO() {

		this.page     = START_PAGE;
		this.pageSize = DEFAULT_PAGE_SIZE;
		this.sort     = singletonList(new OrderDTO(DEFAULT_SORT_PROPERTY, true));

	}

	public PagedSearchDTO(final int page, final String sortProperty) {

		this.page     = page;
		this.pageSize = DEFAULT_PAGE_SIZE;
		this.sort     = singletonList(new OrderDTO(sortProperty, true));

	}
}
