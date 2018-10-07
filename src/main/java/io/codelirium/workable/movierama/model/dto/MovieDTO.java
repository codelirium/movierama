package io.codelirium.workable.movierama.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@Data
@ToString
@ApiModel
@EqualsAndHashCode
public class MovieDTO {

	private String title;

	private String description;

	private int year;

	private int rating;


	public MovieDTO(final String title, final String description, final int year, final int rating) {

		this.title = title;
		this.description = description;
		this.year = year;
		this.rating = rating;

	}
}
