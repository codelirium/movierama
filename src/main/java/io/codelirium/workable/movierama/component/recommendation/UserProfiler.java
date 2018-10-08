package io.codelirium.workable.movierama.component.recommendation;

import io.codelirium.workable.movierama.model.entity.MovieEntity;
import io.codelirium.workable.movierama.model.entity.RatingEntity;
import io.codelirium.workable.movierama.repository.MovieRepository;
import io.codelirium.workable.movierama.repository.RatingRepository;
import io.codelirium.workable.movierama.util.map.MapValueComparator;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.inject.Inject;
import java.util.*;

import static com.google.common.collect.Lists.newLinkedList;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Maps.newTreeMap;
import static java.lang.Math.sqrt;
import static java.util.Collections.shuffle;
import static java.util.stream.Collectors.toSet;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.util.Assert.isTrue;


@Component
public class UserProfiler {

	private static final Logger LOGGER = getLogger(UserProfiler.class);


	@Value("${recommender.user-profiler.neighbour-size}")
	private Integer neighbourSize;


	private MovieRepository movieRepository;

	private RatingRepository ratingRepository;


	@Inject
	public UserProfiler(final MovieRepository movieRepository, final RatingRepository ratingRepository) {

		this.movieRepository  = movieRepository;
		this.ratingRepository = ratingRepository;

	}


	/**
	 *
	 * Get the k-nearest neighbours using Pearson:
	 *
	 * sim(i,j)             = numerator / (sqrt(userDenominator^2) * sqrt(otherUserDenominator^2))
	 *
	 * numerator            = sum((r(u,i) - r(u)) * (r(v,i) - r(v)))
	 * userDenominator      = sum(r(u,i) - r(i))
	 * otherUserDenominator = sum(r(v,i) - r(v))
	 *
	 * r(u,i): rating of the movie i by the user u
	 * r(u)  : average rating of the user u
	 *
	 **/

	public Map<Long, Double> getSimilarUsers(final long thisUserId) {

		isTrue(thisUserId > 0L, "The user id cannot be negative.");


		LOGGER.debug("Looking for users similar to: [" + thisUserId + "] ...");


		final Set<Long> movieIdsRatedByThisUser = movieRepository.findAllViewedByUserId(thisUserId)
																							.parallelStream()
																								.map(MovieEntity::getId)
																								.collect(toSet());

		LOGGER.debug("The user with id: [" + thisUserId + "] has rated [" + movieIdsRatedByThisUser.size() + "] movies.");


		final Set<Long> otherUserIdsRatedAnyOfMovies = ratingRepository.findAllByMovieIds(newLinkedList(movieIdsRatedByThisUser))
																													.parallelStream()
																														.filter(ratingEntity -> ratingEntity.getUserId() != thisUserId)
																														.map(RatingEntity::getUserId)
																														.collect(toSet());

		LOGGER.debug("Found [" + otherUserIdsRatedAnyOfMovies.size() +"] candidate similar users ...");


		final double thisUserAverageRating = ratingRepository.findAverageByUserId(thisUserId);


		final Map<Long, Double> similarUsers = newHashMap();


		final List<Long> otherUserIds = newLinkedList(otherUserIdsRatedAnyOfMovies);

		shuffle(otherUserIds);


		for (final Long otherUserId : otherUserIds) {

			double matchingScore;
			double numerator            = 0D;
			double thisUserDenominator  = 0D;
			double otherUserDenominator = 0D;


			final double otherUserAverageRating = ratingRepository.findAverageByUserId(otherUserId);


			for (final Long movieIdRatedByThisUser : movieIdsRatedByThisUser) {

				final double thisUserRating = ratingRepository.findByUserIdAndMovieId(thisUserId, movieIdRatedByThisUser).get().getRating();

				double u = thisUserRating - thisUserAverageRating;


				final Optional<RatingEntity> optionalRatingEntity = ratingRepository.findByUserIdAndMovieId(otherUserId, movieIdRatedByThisUser);

				if (optionalRatingEntity.isPresent()) {

					final double otherUserRating = optionalRatingEntity.get().getRating();

					double v = otherUserRating - otherUserAverageRating;


					numerator            += u * v;

					thisUserDenominator  += u * u;

					otherUserDenominator += v * v;
				}
			}


			if (thisUserDenominator == 0D || otherUserDenominator == 0D) {

				matchingScore = 0D;

			} else {

				matchingScore = numerator / (sqrt(thisUserDenominator) * sqrt(otherUserDenominator));

			}


			LOGGER.debug("UserId: [" + otherUserId + "] - matching score: [" + matchingScore + "]");

			similarUsers.put(otherUserId, matchingScore);


			if (similarUsers.size() == 2 * neighbourSize) {

				break;

			}
		}


		final TreeMap<Long, Double> similarUsersSorted = newTreeMap(new MapValueComparator(similarUsers));

		similarUsersSorted.putAll(similarUsers);


		LOGGER.debug("Returning the top-most [" + neighbourSize + "] similar users ...");

		return similarUsersSorted
							.entrySet()
								.stream()
									.limit(neighbourSize)
									.collect(TreeMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), Map::putAll);
	}
}
