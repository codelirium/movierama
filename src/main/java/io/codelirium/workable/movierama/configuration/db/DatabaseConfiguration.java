package io.codelirium.workable.movierama.configuration.db;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Properties;

import static io.codelirium.workable.movierama.configuration.cache.CacheConfiguration.EHCACHE_XML;
import static java.io.File.separator;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.slf4j.LoggerFactory.getLogger;


@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = { DatabaseConfiguration.CORE_PACKAGE_JPA_REPOSITORIES })
public class DatabaseConfiguration {

	private static final Logger LOGGER = getLogger(DatabaseConfiguration.class);


	public static final String CORE_PACKAGE = "io.codelirium.workable.movierama";

	private static final String REPOSITORY_PACKAGE = "repository";
	private static final String ENTITY_PACKAGE = "model.entity";

	static final String CORE_PACKAGE_JPA_REPOSITORIES                        = CORE_PACKAGE + "." + REPOSITORY_PACKAGE;
	private static final String CORE_PACKAGE_ENTITY_MANAGER_PACKAGES_TO_SCAN = CORE_PACKAGE + "." + ENTITY_PACKAGE;


	private static final String PROPERTY_NAME_DATABASE_JPA_VENDOR = "db.database";
	private static final String PROPERTY_NAME_DATABASE_DRIVER     = "db.driver";
	private static final String PROPERTY_NAME_DATABASE_URL        = "db.url";
	private static final String PROPERTY_NAME_DATABASE_USERNAME   = "db.username";
	private static final String PROPERTY_NAME_DATABASE_PASSWORD   = "db.password";

	private static final String PROPERTY_NAME_DB_HIKARI_POOL_NAME                = "db.hikari.poolName";
	private static final String PROPERTY_NAME_DB_HIKARI_MAX_POOL_SIZE            = "db.hikari.maxPoolSize";
	private static final String PROPERTY_NAME_DB_HIKARI_IDLE_TIMEOUT             = "db.hikari.idleTimeout";
	private static final String PROPERTY_NAME_DB_HIKARI_CONNECTION_TIMEOUT       = "db.hikari.connectionTimeout";
	private static final String PROPERTY_NAME_DB_HIKARI_PREFERRED_TEST_QUERY     = "db.hikari.preferredTestQuery";
	private static final String PROPERTY_NAME_DB_HIKARI_AUTO_COMMIT              = "db.hikari.autoCommit";
	private static final String PROPERTY_NAME_DB_HIKARY_LEAK_DETECTION_THRESHOLD = "db.hikari.leakDetectionThreshold";

	private static final String PROPERTY_NAME_HIBERNATE_SHOW_SQL                      = "hibernate.show_sql";
	private static final String PROPERTY_NAME_HIBERNATE_FORMAT_SQL                    = "hibernate.format_sql";
	private static final String PROPERTY_NAME_HIBERNATE_GENERATE_DDL                  = "hibernate.generate_ddl";
	private static final String PROPERTY_NAME_HIBERNATE_DIALECT                       = "hibernate.dialect";
	private static final String PROPERTY_NAME_HIBERNATE_HBM2DDL_AUTO                  = "hibernate.hbm2ddl.auto";
	private static final String PROPERTY_NAME_HIBERNATE_AUTO_CLOSE_SESSION            = "hibernate.auto_close_session";
	private static final String PROPERTY_NAME_HIBERNATE_CONNECTION_USE_UNICODE        = "hibernate.connection.useUnicode";
	private static final String PROPERTY_NAME_HIBERNATE_CONNECTION_CHARACTER_ENCODING = "hibernate.connection.characterEncoding";
	private static final String PROPERTY_NAME_HIBERNATE_CONNECTION_CHAR_SET           = "hibernate.connection.charSet";
	private static final String PROPERTY_NAME_HIBERNATE_USE_SQL_COMMENTS              = "hibernate.use_sql_comments";
	private static final String PROPERTY_NAME_HIBERNATE_GENERATE_STATISTICS           = "hibernate.generate_statistics";
	private static final String PROPERTY_NAME_HIBERNATE_ID_NEW_GEN_MAPPINGS           = "hibernate.id.new_generator_mappings";
	private static final String PROPERTY_NAME_HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE  = "hibernate.cache.use_second_level_cache";
	private static final String PROPERTY_NAME_HIBERNATE_CACHE_USE_QUERY_CACHE         = "hibernate.cache.use_query_cache";
	private static final String PROPERTY_NAME_HIBERNATE_CACHE_USE_STRUCTURED_ENTRIES  = "hibernate.cache.use_structured_entries";
	private static final String PROPERTY_NAME_HIBERNATE_CACHE_REGION_FACTORY_CLASS    = "hibernate.cache.region.factory_class";
	private static final String PROPERTY_NAME_HIBERNATE_CACHE_EHACACHE_RESOURCE_NAME  = "org.hibernate.cache.ehcache.configurationResourceName";


	@Resource
	private Environment env;


	@Bean
	public DataSource dataSource() {

		final HikariDataSource dataSource = new HikariDataSource();

		try {

			dataSource.setDriverClassName(env.getProperty(PROPERTY_NAME_DATABASE_DRIVER));

		} catch (final RuntimeException e) {

			LOGGER.error("Failed to create the dataSource: ", e);

		}


		dataSource.setJdbcUrl(env.getProperty(PROPERTY_NAME_DATABASE_URL));
		dataSource.setUsername(env.getProperty(PROPERTY_NAME_DATABASE_USERNAME));
		dataSource.setPassword(env.getProperty(PROPERTY_NAME_DATABASE_PASSWORD));
		dataSource.setPoolName(env.getProperty(PROPERTY_NAME_DB_HIKARI_POOL_NAME));


		try {

			dataSource.setMaximumPoolSize(parseInt(env.getProperty(PROPERTY_NAME_DB_HIKARI_MAX_POOL_SIZE)));
			dataSource.setIdleTimeout(SECONDS.toMillis(parseLong(env.getProperty(PROPERTY_NAME_DB_HIKARI_IDLE_TIMEOUT))));
			dataSource.setConnectionTimeout(SECONDS.toMillis(parseLong(env.getProperty(PROPERTY_NAME_DB_HIKARI_CONNECTION_TIMEOUT))));
			dataSource.setLeakDetectionThreshold(parseLong(env.getProperty(PROPERTY_NAME_DB_HIKARY_LEAK_DETECTION_THRESHOLD)));

		} catch (final NumberFormatException e) {

			LOGGER.error("Failed to create the dataSource: ", e);

		}


		dataSource.setConnectionTestQuery(env.getProperty(PROPERTY_NAME_DB_HIKARI_PREFERRED_TEST_QUERY));
		dataSource.setAutoCommit(Boolean.valueOf(PROPERTY_NAME_DB_HIKARI_AUTO_COMMIT));


		return dataSource;
	}


	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

		final LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();

		entityManagerFactory.setDataSource(dataSource());
		entityManagerFactory.setPackagesToScan(CORE_PACKAGE_ENTITY_MANAGER_PACKAGES_TO_SCAN);
		entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter());
		entityManagerFactory.setJpaProperties(loadHibernateProperties());


		return entityManagerFactory;
	}


	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {

		final HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();

		hibernateJpaVendorAdapter.setShowSql(Boolean.valueOf(env.getProperty(PROPERTY_NAME_HIBERNATE_SHOW_SQL)));
		hibernateJpaVendorAdapter.setGenerateDdl(Boolean.valueOf(env.getProperty(PROPERTY_NAME_HIBERNATE_GENERATE_DDL)));
		hibernateJpaVendorAdapter.setDatabase(Database.valueOf(env.getProperty(PROPERTY_NAME_DATABASE_JPA_VENDOR)));


		return hibernateJpaVendorAdapter;
	}


	@Bean
	public JpaTransactionManager transactionManager() {

		final JpaTransactionManager transactionManager = new JpaTransactionManager();

		transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());


		return transactionManager;
	}


	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {

		return new PersistenceExceptionTranslationPostProcessor();

	}


	private Properties loadHibernateProperties() {

		final Properties hibernateProperties = new Properties();

		hibernateProperties.put(PROPERTY_NAME_HIBERNATE_DIALECT,                       env.getProperty(PROPERTY_NAME_HIBERNATE_DIALECT));
		hibernateProperties.put(PROPERTY_NAME_HIBERNATE_SHOW_SQL,                      env.getProperty(PROPERTY_NAME_HIBERNATE_SHOW_SQL));
		hibernateProperties.put(PROPERTY_NAME_HIBERNATE_FORMAT_SQL,                    env.getProperty(PROPERTY_NAME_HIBERNATE_FORMAT_SQL));
		hibernateProperties.put(PROPERTY_NAME_HIBERNATE_HBM2DDL_AUTO,                  env.getProperty(PROPERTY_NAME_HIBERNATE_HBM2DDL_AUTO));
		hibernateProperties.put(PROPERTY_NAME_HIBERNATE_GENERATE_DDL,                  env.getProperty(PROPERTY_NAME_HIBERNATE_GENERATE_DDL));
		hibernateProperties.put(PROPERTY_NAME_HIBERNATE_AUTO_CLOSE_SESSION,            env.getProperty(PROPERTY_NAME_HIBERNATE_AUTO_CLOSE_SESSION));
		hibernateProperties.put(PROPERTY_NAME_HIBERNATE_CONNECTION_USE_UNICODE,        env.getProperty(PROPERTY_NAME_HIBERNATE_CONNECTION_USE_UNICODE));
		hibernateProperties.put(PROPERTY_NAME_HIBERNATE_CONNECTION_CHARACTER_ENCODING, env.getProperty(PROPERTY_NAME_HIBERNATE_CONNECTION_CHARACTER_ENCODING));
		hibernateProperties.put(PROPERTY_NAME_HIBERNATE_CONNECTION_CHAR_SET,           env.getProperty(PROPERTY_NAME_HIBERNATE_CONNECTION_CHAR_SET));
		hibernateProperties.put(PROPERTY_NAME_HIBERNATE_USE_SQL_COMMENTS,              env.getProperty(PROPERTY_NAME_HIBERNATE_USE_SQL_COMMENTS));
		hibernateProperties.put(PROPERTY_NAME_HIBERNATE_GENERATE_STATISTICS,           env.getProperty(PROPERTY_NAME_HIBERNATE_GENERATE_STATISTICS));
		hibernateProperties.put(PROPERTY_NAME_HIBERNATE_ID_NEW_GEN_MAPPINGS,           env.getProperty(PROPERTY_NAME_HIBERNATE_ID_NEW_GEN_MAPPINGS));
		hibernateProperties.put(PROPERTY_NAME_HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE,  env.getProperty(PROPERTY_NAME_HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE));
		hibernateProperties.put(PROPERTY_NAME_HIBERNATE_CACHE_USE_QUERY_CACHE,         env.getProperty(PROPERTY_NAME_HIBERNATE_CACHE_USE_QUERY_CACHE));
		hibernateProperties.put(PROPERTY_NAME_HIBERNATE_CACHE_USE_STRUCTURED_ENTRIES,  env.getProperty(PROPERTY_NAME_HIBERNATE_CACHE_USE_STRUCTURED_ENTRIES));
		hibernateProperties.put(PROPERTY_NAME_HIBERNATE_CACHE_REGION_FACTORY_CLASS,    env.getProperty(PROPERTY_NAME_HIBERNATE_CACHE_REGION_FACTORY_CLASS));
		hibernateProperties.put(PROPERTY_NAME_HIBERNATE_CACHE_EHACACHE_RESOURCE_NAME,  separator + EHCACHE_XML);


		return hibernateProperties;
	}
}
