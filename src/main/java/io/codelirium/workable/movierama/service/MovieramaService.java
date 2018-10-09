package io.codelirium.workable.movierama.service;

import io.codelirium.workable.movierama.component.client.ExternalMovieAPIClient;
import io.codelirium.workable.movierama.component.client.tmdb.TheMovieDBClient;
import io.codelirium.workable.movierama.component.recommendation.RecommendationEngine;
import io.codelirium.workable.movierama.model.dto.MovieDTO;
import io.codelirium.workable.movierama.model.dto.pagination.PagedSearchDTO;
import io.codelirium.workable.movierama.model.entity.MovieEntity;
import io.codelirium.workable.movierama.model.entity.RatingEntity;
import io.codelirium.workable.movierama.repository.MovieRepository;
import io.codelirium.workable.movierama.repository.RatingRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import javax.inject.Inject;
import java.util.Collection;
import java.util.LinkedList;

import static io.codelirium.workable.movierama.model.entity.RatingEntity.MAX_RATING_SCORE;
import static io.codelirium.workable.movierama.util.client.MovieDescriptionFetcher.getMovieDescription;
import static io.codelirium.workable.movierama.util.pagination.PaginationUtil.feedPageInfo;
import static io.codelirium.workable.movierama.util.pagination.PaginationUtil.makePageRequest;
import static java.util.stream.Collectors.toCollection;
import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notNull;


@Service
public class MovieramaService {

	private MovieRepository  movieRepository;

	private RatingRepository ratingRepository;

	private ExternalMovieAPIClient theMovieDBClient;

	private RecommendationEngine recommendationEngine;


	@Inject
	public MovieramaService(final MovieRepository        movieRepository,
							final RatingRepository       ratingRepository,
							final ExternalMovieAPIClient theMovieDBClient,
							final RecommendationEngine   recommendationEngine) {

		this.movieRepository      = movieRepository;
		this.ratingRepository     = ratingRepository;
		this.theMovieDBClient     = theMovieDBClient;
		this.recommendationEngine = recommendationEngine;
	}


	public void rateMovieViewed(final long userId, final long movieId, final int rating) {

		isTrue(userId  >= 0, "The user id cannot be negative.");
		isTrue(movieId >= 0, "The movie id cannot be negative.");
		isTrue(rating >= 0 && rating <= MAX_RATING_SCORE, "The rating must be within [0, " + MAX_RATING_SCORE + "].");


		final RatingEntity ratingEntity = new RatingEntity();

		ratingEntity.setUserId(userId);
		ratingEntity.setMovieId(movieId);
		ratingEntity.setRating(rating);

		ratingRepository.saveAndFlush(ratingEntity);
	}


	public Collection<MovieDTO> getMoviesViewed(final PagedSearchDTO searchDTO, final long userId) {

		notNull(searchDTO, "The searchDTO cannot be null.");
		isTrue(userId  >= 0, "The user id cannot be negative.");


		final Page<MovieEntity> movieEntities = movieRepository.findPageViewedByUserId(userId, makePageRequest(searchDTO));

		feedPageInfo(searchDTO, movieEntities);


		return movieEntities
					.getContent()
						.parallelStream()
							.map(movieEntity -> new MovieDTO(movieEntity.getPlainTitle(),
															 getMovieDescription(theMovieDBClient, movieEntity
																										.getPlainTitle()
																											.replaceAll(".*?\\(.*?\\).*?",""))
																											.replace(",", ""),
															 movieEntity.getYear(),
															 movieEntity.getRating()))
							.collect(toCollection(LinkedList::new));
	}


	public Collection<MovieDTO> getRecommendedMovies(final int userId, final int maxSize) {

		isTrue(userId >= 0, "The user id cannot be negative.");
		isTrue(maxSize >= 0, "The maximum recommendations size cannot be negative.");


		return recommendationEngine.getRecommendations(userId, maxSize);
	}
}
