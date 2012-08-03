package com.ziesemer.utils.junit;

import java.io.File;

import org.junit.After;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.internal.runners.model.MultipleFailureException;
import org.junit.rules.TestWatchman;
import org.junit.runners.model.FrameworkMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com.">&lt;www.ziesemer.com&gt;</a>
 */
public class BaseTest{
	
	protected static final File TEST_OUTPUT = new File("target/JUnit");
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	protected final TestThreadData testThreadData = TestThreadData.instance();
	
	@Rule
	public final BaseTestWatchman watchman = new BaseTestWatchman();
	
	@After
	public void afterLogCheck(){
		Assert.assertEquals("Warnings or Errors were generated to log.",
			0, testThreadData.getLogWarnings() - watchman.getStartLogWarningCount());
	}
	
	protected File getTestOutputRoot(){
		return TEST_OUTPUT;
	}
	
	protected File getTestOutput(){
		return getTestOutput(getClass());
	}
	
	protected static File getTestOutput(Class<?> c){
		String pkgName = c.getName();
		pkgName = pkgName.substring(0, pkgName.lastIndexOf('.'));
		return new File(TEST_OUTPUT, pkgName.replace('.', '/'));
	}
	
	protected class BaseTestWatchman extends TestWatchman{
		protected FrameworkMethod frameworkMethod;
		protected long timeMs;
		protected int startWarningCount = testThreadData.getLogWarnings();
		
		@Override
		public void starting(final FrameworkMethod method){
			logger.info("Starting test: {}", method.getMethod().getName());
			this.frameworkMethod = method;
			this.timeMs = System.currentTimeMillis();
		}
		
		@Override
		public void failed(final Throwable e, final FrameworkMethod method){
			logger.error("Failed test: {}, {}",
				new Object[]{method.getMethod().getName(), e.toString(), e});
			if(e instanceof MultipleFailureException){
				MultipleFailureException mfe = (MultipleFailureException)e;
				int failureIdx = 0;
				for(final Throwable t : mfe.getFailures()){
					logger.error("Failure {}/{}: {}",
						new Object[]{(++failureIdx), mfe.getFailures().size(), t, t});
				}
			}
		}
		
		@Override
		public void finished(final FrameworkMethod method){
			final long time = System.currentTimeMillis() - timeMs;
			logger.info("Finished test: {}, {} ms", method.getMethod().getName(), time);
		}
		
		public FrameworkMethod getFrameworkMethod(){
			return frameworkMethod;
		}
		
		public long getTestStartTimeMs(){
			return timeMs;
		}
		
		public int getStartLogWarningCount(){
			return startWarningCount;
		}
	}
}
