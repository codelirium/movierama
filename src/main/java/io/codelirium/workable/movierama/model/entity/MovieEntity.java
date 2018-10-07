package io.codelirium.workable.movierama.model.entity;

import io.codelirium.workable.movierama.model.entity.base.PersistableBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.regex.Matcher;

import static io.codelirium.workable.movierama.model.entity.RatingEntity.COLUMN_NAME_MOVIE_ID;
import static io.codelirium.workable.movierama.model.entity.RatingEntity.TABLE_NAME;
import static io.codelirium.workable.movierama.model.entity.base.PersistableBaseEntity.FIELD_NAME_ID;
import static java.lang.Integer.valueOf;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static java.util.Objects.isNull;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Stream.of;


@Data
@Entity
@ToString
@EqualsAndHashCode
@Table(name = MovieEntity.TABLE_NAME)
@AttributeOverride(name = FIELD_NAME_ID, column = @Column(name = MovieEntity.COLUMN_NAME_ID))
@SecondaryTables({ @SecondaryTable(name = TABLE_NAME, pkJoinColumns = { @PrimaryKeyJoinColumn(name = COLUMN_NAME_MOVIE_ID, referencedColumnName = MovieEntity.COLUMN_NAME_ID) }) })
public class MovieEntity extends PersistableBaseEntity<Long> implements Serializable {

	private static final long serialVersionUID = -2589170530443760064L;


			static final String TABLE_NAME        = "MOVIES";
			static final String COLUMN_NAME_ID    = "ID";
	public  static final String COLUMN_NAME_TITLE = "TITLE";


	@Column(name = COLUMN_NAME_TITLE, nullable = false)
	private String title;

	@Column(name = "GENRES", nullable = false)
	private String genres;

	@Column(table = TABLE_NAME, nullable = false)
	private Integer rating;


	public String getPlainTitle() {

		if (!isNull(title)) {

			return title.substring(0, title.length() - 7);

		}


		return "";
	}


	public int getYear() {

		if (!isNull(title)) {

			final Matcher matcher = compile("\\(([0-9]*?)\\)").matcher(title);


			if (matcher.find()) {

				return valueOf(matcher.group(1));

			}
		}


		return 0;
	}


	public Collection<String> getGenresCollection() {

		if (!isNull(genres)) {

			return of(genres.split("\\|")).collect(toCollection(LinkedList::new));

		}


		return unmodifiableList(emptyList());
	}
}
