package io.codelirium.workable.movierama.component.recommendation;

import io.codelirium.workable.movierama.component.client.tmdb.TheMovieDBClient;
import io.codelirium.workable.movierama.model.dto.MovieDTO;
import io.codelirium.workable.movierama.model.entity.MovieEntity;
import io.codelirium.workable.movierama.model.entity.RatingEntity;
import io.codelirium.workable.movierama.repository.MovieRepository;
import io.codelirium.workable.movierama.repository.RatingRepository;
import io.codelirium.workable.movierama.util.map.MapValueComparator;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import javax.inject.Inject;
import java.util.*;

import static com.google.common.collect.Lists.newLinkedList;
import static com.google.common.collect.Maps.newHashMap;
import static io.codelirium.workable.movierama.model.entity.RatingEntity.MAX_RATING_SCORE;
import static io.codelirium.workable.movierama.util.client.MovieDescriptionFetcher.getMovieDescription;
import static java.lang.Math.abs;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.util.Assert.isTrue;


@Component
public class RecommendationEngine {

	private static final Logger LOGGER = getLogger(RecommendationEngine.class);


	private UserProfiler userProfiler;

	private TheMovieDBClient theMovieDBClient;

	private MovieRepository movieRepository;

	private RatingRepository ratingRepository;


	@Inject
	public RecommendationEngine(final UserProfiler     userProfiler,
								final TheMovieDBClient theMovieDBClient,
								final MovieRepository  movieRepository,
								final RatingRepository ratingRepository) {

		this.userProfiler = userProfiler;
		this.theMovieDBClient = theMovieDBClient;
		this.movieRepository = movieRepository;
		this.ratingRepository = ratingRepository;

	}


	/**
	 *   A User - User Collaborative filtering recommender.
	 *
	 *   r(u,i) = r(u) + sum(sim(u,v) * (r(v,i) - r(v))) / sum(abs(sim(u,v)))
	 *
	 *   sim(u,v): similarity between u and v users
	 *   r(u,i)  : rating of the movie i by the user u
	 *   r(u)    : average rating of the user u
	 *
	 **/

	public List<MovieDTO> getRecommendations(final long thisUserId, final int maxSize) {

		isTrue(thisUserId >= 0, "The user id cannot be negative.");
		isTrue(maxSize >= 0, "The maximum recommendations size cannot be negative.");


		final Map<Long, Double> otherSimilarUsers = userProfiler.getSimilarUsers(thisUserId);

		LOGGER.debug("Found [" + otherSimilarUsers.size() +"] similar users ...");


		final double thisUserAverageRating = ratingRepository.findAverageByUserId(thisUserId);

		final Map<Long, MovieEntity> movieEntities = movieRepository.findAllNotViewedByThisUserButViewedByOtherUsers(thisUserId, newLinkedList(otherSimilarUsers.keySet()))
																								.parallelStream()
																									.collect(toMap(MovieEntity::getId, identity()));

		LOGGER.debug("Found [" + movieEntities.size() +"] candidate recommended movies ...");


		final Map<Long, Double> predictedRatings = newHashMap();


		for (final Long movieId : movieEntities.keySet()) {

			double numerator   = 0D;
			double denominator = 0D;


			for (final Long otherUserId : otherSimilarUsers.keySet()) {

				double matchScore = otherSimilarUsers.get(otherUserId);


				final Optional<RatingEntity> optionalRatingEntity = ratingRepository.findByUserIdAndMovieId(otherUserId, movieId);

				if (optionalRatingEntity.isPresent()) {

					final double otherUserMovieRating = optionalRatingEntity.get().getRating();

					final double otherUserAverageRating = ratingRepository.findAverageByUserId(otherUserId);


					numerator += matchScore * (otherUserMovieRating - otherUserAverageRating);

					denominator += abs(matchScore);
				}
			}


			double predictedRating = 0D;

			if (denominator > 0D) {

				predictedRating = thisUserAverageRating + (numerator / denominator);


				if (predictedRating > MAX_RATING_SCORE) {

					predictedRating = MAX_RATING_SCORE;

				}
			}


			LOGGER.debug("MovieId: [" + movieId + "] - predicted rating: [" + predictedRating + "]");

			predictedRatings.put(movieId, predictedRating);
		}


		final Map<Long, Double> recommendationsSorted = new TreeMap<>(new MapValueComparator(predictedRatings));

		recommendationsSorted.putAll(predictedRatings);


		LOGGER.debug("Returning the top-most [" + maxSize + "] recommended movies ...");

		return recommendationsSorted
								.entrySet()
									.stream()
										.limit(maxSize)
										.map(entry -> new MovieDTO( movieEntities.get(entry.getKey()).getPlainTitle(),
																	getMovieDescription(theMovieDBClient, movieEntities.get(entry.getKey())
																																.getPlainTitle()
																																	.replaceAll(".*?\\(.*?\\).*?","")
																																	.replace(",", "")),
																	movieEntities.get(entry.getKey()).getYear(),
																	movieEntities.get(entry.getKey()).getRating()))
										.collect(toList());
	}
}
