package io.codelirium.workable.movierama.model.entity.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;


@Data
@ToString
@MappedSuperclass
@EqualsAndHashCode
public abstract class PersistableBaseEntity<ID extends Serializable> {

	public static final String FIELD_NAME_ID = "id";

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private ID id;


	public PersistableBaseEntity() { }

	public PersistableBaseEntity(final ID id) {

		this.id = id;

	}
}
