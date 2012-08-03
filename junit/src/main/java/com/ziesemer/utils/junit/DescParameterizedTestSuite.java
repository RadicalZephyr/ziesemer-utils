package com.ziesemer.utils.junit;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.Suite;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

/**
 * <p>Modified copy of org.junit.runners.Parameterized from JUnit 4.7 - 4.8.2.</p>
 * @author JUnit <a href="http://www.junit.org">&lt;www.junit.org&gt;</a>
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com.">&lt;www.ziesemer.com&gt;</a>
 */
public class DescParameterizedTestSuite extends Suite{
	/**
	 * Annotation for a method which provides parameters to be injected into the test class
	 * constructor by <code>Parameterized</code>
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public static @interface Parameters{
		// Marker only.
	}
	
	/**
	 * Annotation for a method which returns a description for a given set of parameters.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public static @interface ParameterDescription{
		// Marker only.
	}
	
	protected class TestClassRunnerForParameters extends BlockJUnit4ClassRunner{
		protected final List<Object[]> fParameterList;
		protected final int fParameterSetNumber;
		
		protected TestClassRunnerForParameters(Class<?> type, List<Object[]> parameterList, int i)
				throws InitializationError{
			super(type);
			this.fParameterList = parameterList;
			this.fParameterSetNumber = i;
		}
		
		@Override
		public Object createTest() throws Exception{
			return getTestClass().getOnlyConstructor().newInstance(computeParams());
		}
		
		protected Object[] computeParams() throws Exception{
			try{
				return fParameterList.get(fParameterSetNumber);
			}catch(ClassCastException e){
				throw new Exception(String.format("%s.%s() must return a Collection of arrays.",
					getTestClass().getName(),
					getAnnotationMethod(getTestClass(), Parameters.class).getName()));
			}
		}
		
		@Override
		protected String getName(){
			return String.format("[%s]", getParameterDescription());
		}
		
		@Override
		protected String testName(final FrameworkMethod method){
			return String.format("%s[%s]", method.getName(), getParameterDescription());
		}
		
		protected Object getParameterDescription(){
			try{
				return DescParameterizedTestSuite.this.getParameterDescription(
					getTestClass(), fParameterList.get(fParameterSetNumber));
			}catch(Throwable t){
				return fParameterSetNumber;
			}
		}
		
		@Override
		protected void validateConstructor(List<Throwable> errors){
			validateOnlyOneConstructor(errors);
		}
		
		@Override
		protected Statement classBlock(RunNotifier notifier){
			return childrenInvoker(notifier);
		}
	}
	
	protected final List<Runner> runners;
	
	/**
	 * Only called reflectively. Do not use programmatically.
	 */
	public DescParameterizedTestSuite(Class<?> klass) throws Throwable{
		super(klass, Collections.<Runner>emptyList());
		List<Object[]> parametersList = getParametersList(getTestClass());
		runners = new ArrayList<Runner>(parametersList.size());
		for(int i = 0; i < parametersList.size(); i++){
			runners.add(new TestClassRunnerForParameters(
				getTestClass().getJavaClass(), parametersList, i));
		}
	}
	
	@Override
	protected List<Runner> getChildren(){
		return runners;
	}
	
	@SuppressWarnings("unchecked")
	protected List<Object[]> getParametersList(TestClass klass) throws Throwable{
		return (List<Object[]>)getAnnotationMethod(klass, Parameters.class)
			.invokeExplosively(null);
	}
	
	protected Object getParameterDescription(TestClass klass, Object[] params) throws Throwable{
		return getAnnotationMethod(klass, ParameterDescription.class)
			.invokeExplosively(null, params);
	}
	
	protected FrameworkMethod getAnnotationMethod(TestClass testClass,
			Class<? extends Annotation> annotationClass) throws Exception{
		List<FrameworkMethod> methods = testClass.getAnnotatedMethods(annotationClass);
		for(FrameworkMethod each : methods){
			int modifiers = each.getMethod().getModifiers();
			if(Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers)){
				return each;
			}
		}
		
		throw new Exception("No public static parameters method on class " + testClass.getName());
	}
	
}
