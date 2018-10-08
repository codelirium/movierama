package io.codelirium.workable.movierama.repository;

import io.codelirium.workable.movierama.model.entity.MovieEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;


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
	Page<MovieEntity> findPageViewedByUserId(final long userId, final Pageable pageable);


	@Query(nativeQuery = true, value = SQL_QUERY_GET_ALL_MOVIES_VIEWED_BY_USER)
	List<MovieEntity> findAllViewedByUserId(final long userId);


	String SQL_QUERY_GET_ALL_MOVIES_NOT_VIEWED_BY_USER_BUT_VIEWED_BY_OTHER_USERS = "SELECT "                     +
																							"DISTINCT ON (1)  "  +
																							"M.ID, "             +
																							"M.*, "              +
																							"R.RATING "          +
																					"FROM "                      +
																							"MOVIES  M, "        +
																							"RATINGS R "         +
																					"WHERE "                     +
																							"R.MOVIE_ID = M.ID " +
																						"AND R.USER_ID <> ?1 "   +
																						"AND R.USER_ID IN ?2";

	@Query(nativeQuery = true, value = SQL_QUERY_GET_ALL_MOVIES_NOT_VIEWED_BY_USER_BUT_VIEWED_BY_OTHER_USERS)
	List<MovieEntity> findAllNotViewedByThisUserButViewedByOtherUsers(final long userId, final List<Long> otherUserIds);

}
