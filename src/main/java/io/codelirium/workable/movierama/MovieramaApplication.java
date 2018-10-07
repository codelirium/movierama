package io.codelirium.workable.movierama;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

import static io.codelirium.workable.movierama.configuration.db.DatabaseConfiguration.CORE_PACKAGE;
import static java.lang.Boolean.FALSE;


@SpringBootApplication
@ComponentScan({CORE_PACKAGE})
public class MovieramaApplication {

	public static void main(String[] args) {

		new SpringApplicationBuilder(MovieramaApplication.class)
				.logStartupInfo(FALSE)
				.run(args);

	}
}
