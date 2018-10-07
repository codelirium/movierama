package io.codelirium.workable.movierama.configuration.cache;

import io.codelirium.workable.movierama.util.cache.BaseKeyGenerator;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;


@Configuration
@EnableCaching
public class CacheConfiguration extends CachingConfigurerSupport {

	public static final String EHCACHE_XML = "ehcache.xml";


	@Bean
	@Override
	public KeyGenerator keyGenerator() {

		return new BaseKeyGenerator();

	}

	@Bean
	@Primary
	@Override
	public org.springframework.cache.CacheManager cacheManager() {

		return new EhCacheCacheManager(ehCacheManagerFactoryBean().getObject());

	}

	@Bean
	public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {

		final EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();

		ehCacheManagerFactoryBean.setConfigLocation(new ClassPathResource(EHCACHE_XML));
		ehCacheManagerFactoryBean.setShared(true);


		return ehCacheManagerFactoryBean;
	}
}
