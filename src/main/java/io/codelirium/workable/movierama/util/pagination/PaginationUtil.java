package io.codelirium.workable.movierama.util.pagination;

import io.codelirium.workable.movierama.model.dto.pagination.PagedSearchDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Order;
import java.io.Serializable;
import java.util.List;

import static com.google.common.collect.Lists.newArrayListWithExpectedSize;
import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.data.domain.Sort.by;
import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notNull;


public final class PaginationUtil {


	private PaginationUtil() { }


	public static <T extends PagedSearchDTO> T feedPageInfo(final T searchDTO, final Page<? extends Serializable> page) {

		notNull(searchDTO, "The search DTO cannot be null.");
		notNull(page, "The page cannot be null.");


		searchDTO.setTotalPages(page.getTotalPages());
		searchDTO.setTotalElements(page.getTotalElements());


		return searchDTO;
	}


	public static PageRequest makePageRequest(final PagedSearchDTO searchDTO) {

		notNull(searchDTO, "The search DTO cannot be null.");


		return of(searchDTO.getPage() - 1, searchDTO.getPageSize(), by(makeOrders(searchDTO)));
	}


	public static PageRequest makePageRequest(final Integer pageSize, final String sortProperty) {

		notNull(pageSize, "The page size cannot be null.");
		isTrue(pageSize > 0, "The page size must be greater than zero.");
		notNull(sortProperty, "The sort property cannot be null.");


		final PagedSearchDTO searchDTO = new PagedSearchDTO(1, sortProperty);


		return of(searchDTO.getPage(), pageSize, by(makeOrders(searchDTO)));
	}


	public static List<Order> makeOrders(final PagedSearchDTO searchDTO) {

		notNull(searchDTO, "The search DTO cannot be null.");
		notNull(searchDTO.getSort(), "The list of sort orders cannot be null.");


		final List<Order> sortOrders = newArrayListWithExpectedSize(searchDTO.getSort().size());

		searchDTO.getSort().forEach(sort -> sortOrders.add(sort.getOrder()));


		return sortOrders;
	}
}
