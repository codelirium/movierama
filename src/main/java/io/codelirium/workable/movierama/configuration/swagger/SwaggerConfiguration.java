package io.codelirium.workable.movierama.configuration.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import javax.annotation.Resource;

import static io.codelirium.workable.movierama.controller.mapping.UrlMappings.API_PATH_ROOT;
import static io.codelirium.workable.movierama.controller.mapping.UrlMappings.MORE_ON_PATH;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static springfox.documentation.builders.PathSelectors.regex;
import static springfox.documentation.builders.RequestHandlerSelectors.any;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;


@Configuration
@EnableSwagger2
public class SwaggerConfiguration extends WebMvcConfigurationSupport {

	public static final String  SWAGGER_API_RESPONSE_GENERIC_200_MESSAGE = "The operation was successful.";
	public static final String  SWAGGER_API_RESPONSE_GENERIC_400_MESSAGE = "The request body is not syntactically correct.";
	public static final String  SWAGGER_API_RESPONSE_GENERIC_415_MESSAGE = "The http media type of the request is not supported.";
	public static final String  SWAGGER_API_RESPONSE_GENERIC_500_MESSAGE = "Internal server failure.";

	public static final String  SWAGGER_API_OPERATION_RATE_MOVIE_VIEWED       = "rateViewed";
	public static final String  SWAGGER_API_OPERATION_RATE_MOVIE_VIEWED_NOTES = "This operation is used to rate a movie which was viewed by the user.";

	public static final String  SWAGGER_API_OPERATION_GET_VIEWED       = "getViewed";
	public static final String  SWAGGER_API_OPERATION_GET_VIEWED_NOTES = "This operation is used to retrieved the movies viewed by the user.";

	public static final String SWAGGER_API_OPERATION_GET_RECOMMENDED       = "getRecommended";
	public static final String SWAGGER_API_OPERATION_GET_RECOMMENDED_NOTES = "This operation is used to retrieve movies which are recommended to the user.";


	private static final String PROPERTY_APP_API_NAME                 = "application.api.name";
	private static final String PROPERTY_APP_API_DESCRIPTION          = "application.api.description";
	private static final String PROPERTY_APP_API_VERSION              = "application.api.version";
	private static final String PROPERTY_APP_API_TERMS_OF_SERVICE_URL = "application.api.termsOfServiceUrl";
	private static final String PROPERTY_APP_API_CONTACT_NAME         = "application.api.contact.name";
	private static final String PROPERTY_APP_API_CONTACT_URL          = "application.api.contact.url";
	private static final String PROPERTY_APP_API_CONTACT_EMAIL        = "application.api.contact.email";
	private static final String PROPERTY_APP_API_LICENCE_TYPE         = "application.api.licenceType";
	private static final String PROPERTY_APP_API_LICENCE_URL          = "application.api.licenceURL";


	@Resource
	private Environment env;


	@Bean
	public Docket movieramaApiDocket() {

		return new Docket(SWAGGER_2)
							.apiInfo(movieramaApiInfo())
							.forCodeGeneration(TRUE)
							.useDefaultResponseMessages(FALSE)
							.select()
							.apis(any())
							.paths(regex(format("%s%s", API_PATH_ROOT, MORE_ON_PATH)))
							.build();

	}

	@Bean
	public ApiInfo movieramaApiInfo() {

		return new ApiInfo( env.getProperty(PROPERTY_APP_API_NAME),
							env.getProperty(PROPERTY_APP_API_DESCRIPTION),
							env.getProperty(PROPERTY_APP_API_VERSION),
							env.getProperty(PROPERTY_APP_API_TERMS_OF_SERVICE_URL),
							new Contact(env.getProperty(PROPERTY_APP_API_CONTACT_NAME),
										env.getProperty(PROPERTY_APP_API_CONTACT_URL),
										env.getProperty(PROPERTY_APP_API_CONTACT_EMAIL)),
							env.getProperty(PROPERTY_APP_API_LICENCE_TYPE),
							env.getProperty(PROPERTY_APP_API_LICENCE_URL),
							emptyList());

	}

	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {

		registry.addResourceHandler("swagger-ui.html")
				.addResourceLocations("classpath:/META-INF/resources/");

		registry.addResourceHandler("/webjars/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/");

	}
}
