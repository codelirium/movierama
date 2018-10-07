package io.codelirium.workable.movierama.util.cache;

import org.springframework.cache.interceptor.SimpleKeyGenerator;
import java.lang.reflect.Method;

import static com.google.common.collect.ObjectArrays.concat;


public class BaseKeyGenerator extends SimpleKeyGenerator {

	@Override
	public Object generate(final Object target, final Method method, final Object... params) {

		return super.generate(target, method, concat(method.getName(), params));

	}
}
