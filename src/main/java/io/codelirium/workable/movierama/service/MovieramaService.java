package io.codelirium.workable.movierama.service;

import io.codelirium.workable.movierama.component.client.TheMovieDBClient;
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

import static io.codelirium.workable.movierama.component.client.TheMovieDBClient.MSG_UNAVAILABLE;
import static io.codelirium.workable.movierama.util.pagination.PaginationUtil.feedPageInfo;
import static io.codelirium.workable.movierama.util.pagination.PaginationUtil.makePageRequest;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toCollection;
import static org.springframework.util.Assert.notNull;


@Service
public class MovieramaService {

	private MovieRepository  movieRepository;

	private RatingRepository ratingRepository;

	private TheMovieDBClient theMovieDBClient;


	@Inject
	public MovieramaService(final MovieRepository  movieRepository,
							final RatingRepository ratingRepository,
							final TheMovieDBClient theMovieDBClient) {

		this.movieRepository  = movieRepository;
		this.ratingRepository = ratingRepository;
		this.theMovieDBClient = theMovieDBClient;
	}


	public void rateMovieViewed(final long userId, final long movieId, final int rating) {

		final RatingEntity ratingEntity = new RatingEntity();

		ratingEntity.setUserId(userId);
		ratingEntity.setMovieId(movieId);
		ratingEntity.setRating(rating);

		ratingRepository.saveAndFlush(ratingEntity);
	}


	public Collection<MovieDTO> getMoviesViewed(final PagedSearchDTO searchDTO, final long userId) {

		notNull(searchDTO, "The searchDTO cannot be null.");


		final Page<MovieEntity> movieEntities = movieRepository.findViewedByUserId(userId, makePageRequest(searchDTO));

		feedPageInfo(searchDTO, movieEntities);


		return movieEntities
					.getContent()
						.parallelStream()
							.map(movieEntity -> new MovieDTO(movieEntity.getPlainTitle(),
								 getMovieDescription(movieEntity.getPlainTitle()),
								 movieEntity.getYear(),
								 movieEntity.getRating()))
							.collect(toCollection(LinkedList::new));
	}


	public Collection<MovieDTO> getRecommendedMovies(final int userId) {

		return emptyList();

	}


	private String getMovieDescription(final String title) {

		notNull(title, "The movie title cannot be null.");


		try {

			return theMovieDBClient.getMovieDescription(title);

		} catch (final Exception e) {

			return MSG_UNAVAILABLE;

		}
	}
}
