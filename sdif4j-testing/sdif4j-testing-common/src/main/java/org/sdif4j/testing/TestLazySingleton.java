package org.sdif4j.testing;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.concurrent.atomic.AtomicInteger;

@Named
@Singleton
@org.springframework.context.annotation.Lazy // ignored by Guice
public class TestLazySingleton {
	private static final AtomicInteger instantCounter = new AtomicInteger();

	public TestLazySingleton() {
		instantCounter.incrementAndGet();
	}

	public static int getInstantCount() {
		return instantCounter.get();
	}
}
