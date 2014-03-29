package org.sdif4j.testing.spring;

import org.sdif4j.InjectContext;
import org.sdif4j.spring.SpringInjectContext;
import org.sdif4j.testing.ITestSingleton;
import org.sdif4j.testing.TestLazySingleton;
import org.sdif4j.testing.TestPrototype;
import org.sdif4j.testing.TestSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.inject.Inject;
import javax.inject.Named;

import static org.testng.Assert.*;

public class SpringXmlTest {
	private InjectContext injectContext;

	@BeforeClass
	public void beforeClass() {
		final ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-test-context.xml");
		injectContext = ctx.getBean(InjectContext.class);
	}

	@Test
	public void testInjectContextInstance() {
		final InjectContext injectContext = this.injectContext;
		assertTrue(injectContext instanceof SpringInjectContext);
		assertTrue(this.injectContext.getInstance(InjectContext.class) == injectContext);
	}

	@Test
	public void testNamed() {
		assertEquals(injectContext.getInstance(String.class, "key"), "value");
	}

	@Test
	public void testSingleton() {
		assertEquals(TestSingleton.getInstantCount(), 1);
		final ITestSingleton iTestSingleton1 = injectContext.getInstance(ITestSingleton.class);
		final ITestSingleton iTestSingleton2 = injectContext.getInstance(ITestSingleton.class);
		final TestSingleton testSingleton1 = injectContext.getInstance(TestSingleton.class);
		final TestSingleton testSingleton2 = injectContext.getInstance(TestSingleton.class);
		assertEquals(TestSingleton.getInstantCount(), 1);

		assertNotNull(iTestSingleton1);
		assertTrue(iTestSingleton1 == iTestSingleton2);
		assertTrue(iTestSingleton2 == testSingleton1);
		assertTrue(testSingleton1 == testSingleton2);
	}

	@Test
	public void testLazySingleton() {
		assertEquals(TestLazySingleton.getInstantCount(), 0);
		final TestLazySingleton s1 = injectContext.getInstance(TestLazySingleton.class);
		final TestLazySingleton s2 = injectContext.getInstance(TestLazySingleton.class);
		assertEquals(TestLazySingleton.getInstantCount(), 1);
		assertNotNull(s1);
		assertTrue(s1 == s2);
	}

	@Test
	public void testPrototype() {
		final TestPrototype testPrototype1 = injectContext.getInstance(TestPrototype.class);
		final TestPrototype testPrototype2 = injectContext.getInstance(TestPrototype.class);
		assertNotNull(testPrototype1);
		assertNotNull(testPrototype2);
		assertTrue(testPrototype1 != testPrototype2);
	}

	@Test
	public void testInjectMembers() {
		class TestInjectable {
			@Inject
			@Named("key")
			String testKey;
		}
		final TestInjectable testInjectable = new TestInjectable();
		injectContext.injectMembers(testInjectable);
		assertEquals(testInjectable.testKey, "value");
	}
}
