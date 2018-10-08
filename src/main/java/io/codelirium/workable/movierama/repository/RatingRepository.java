package io.codelirium.workable.movierama.repository;

import io.codelirium.workable.movierama.model.entity.RatingEntity;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Repository
@Transactional
public interface RatingRepository extends JpaRepository<RatingEntity, Long> {

	String SQL_QUERY_GET_ALL_RATINGS_BY_MOVIES = "SELECT "       +
													"R.* "       +
												 "FROM "         +
													"RATINGS R " +
												 "WHERE "        +
													"R.MOVIE_ID IN (?1)";

	@Query(nativeQuery = true, value = SQL_QUERY_GET_ALL_RATINGS_BY_MOVIES)
	List<RatingEntity> findAllByMovieIds(final List<Long> movieIds);


	String SQL_QUERY_GET_AVERAGE_RATING_BY_USER = "SELECT "                       +
													"COALESCE(AVG(R.RATING), 0) " +
												  "FROM "                         +
													"RATINGS R "                  +
												  "WHERE "                        +
													"R.USER_ID = ?1";

	@Cacheable(value = "avg-rating-by-user", keyGenerator = "keyGenerator")
	@Query(nativeQuery = true, value = SQL_QUERY_GET_AVERAGE_RATING_BY_USER)
	Double findAverageByUserId(final long userId);


	@Cacheable(value = "rating-by-user-and-movie", keyGenerator = "keyGenerator")
	Optional<RatingEntity> findByUserIdAndMovieId(final long userId, final long movieId);

}
