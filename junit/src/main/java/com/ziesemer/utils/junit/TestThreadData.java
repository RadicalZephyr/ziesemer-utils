package com.ziesemer.utils.junit;

/**
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com.">&lt;www.ziesemer.com&gt;</a>
 */
public class TestThreadData{
	
	protected static final ThreadLocal<TestThreadData> THREAD_TEST_DATA
			= new InheritableThreadLocal<TestThreadData>(){
		@Override
		protected TestThreadData initialValue(){
			return new TestThreadData();
		}
	};
	
	public static TestThreadData instance(){
		return THREAD_TEST_DATA.get();
	}
	
	protected TestThreadData(){
		// Protected.
	}
	
	protected int logWarnings = 0;
	public int getLogWarnings(){
		return logWarnings;
	}
	/**
	 * Should only be called by {@link TestLogWatcherAppender}.
	 */
	public int addLogWarnings(){
		return ++logWarnings;
	}
	
}
