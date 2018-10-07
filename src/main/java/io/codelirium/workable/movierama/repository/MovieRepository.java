package io.codelirium.workable.movierama.repository;

import io.codelirium.workable.movierama.model.entity.MovieEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;


@Repository
@Transactional
public interface MovieRepository extends PagingAndSortingRepository<MovieEntity, Long> {

	String SQL_QUERY_GET_ALL_MOVIES_VIEWED_BY_USER = "SELECT "                   +
															"M.*, "              +
															"R.RATING "          +
													 "FROM "                     +
															"MOVIES  M, "        +
															"RATINGS R "         +
													 "WHERE "                    +
															"R.MOVIE_ID = M.ID " +
														"AND R.USER_ID = ?1";

	@Query(nativeQuery = true, value = SQL_QUERY_GET_ALL_MOVIES_VIEWED_BY_USER)
	Page<MovieEntity> findViewedByUserId(final long userId, final Pageable pageable);

}
