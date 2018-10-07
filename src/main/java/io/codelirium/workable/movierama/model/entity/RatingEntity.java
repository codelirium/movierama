package io.codelirium.workable.movierama.model.entity;

import io.codelirium.workable.movierama.model.entity.base.PersistableBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

import static io.codelirium.workable.movierama.model.entity.base.PersistableBaseEntity.FIELD_NAME_ID;


@Data
@Entity
@ToString
@EqualsAndHashCode
@Table(name = RatingEntity.TABLE_NAME)
@AttributeOverride(name = FIELD_NAME_ID, column = @Column(name = RatingEntity.COLUMN_NAME_ID))
public class RatingEntity extends PersistableBaseEntity<Long> implements Serializable {

	private static final long serialVersionUID = -2589170530443760063L;


	static final String TABLE_NAME           = "RATINGS";
	static final String COLUMN_NAME_ID       = "ID";
	static final String COLUMN_NAME_MOVIE_ID = "MOVIE_ID";


	@Column(name = "USER_ID", nullable = false)
	private Long userId;

	@Column(name = "MOVIE_ID", nullable = false)
	private Long movieId;

	@Column(name = "RATING", nullable = false)
	private Integer rating;

}
