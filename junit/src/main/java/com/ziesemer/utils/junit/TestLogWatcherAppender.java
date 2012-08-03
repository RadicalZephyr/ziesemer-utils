package com.ziesemer.utils.junit;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

/**
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com.">&lt;www.ziesemer.com&gt;</a>
 */
public class TestLogWatcherAppender extends AppenderBase<ILoggingEvent>{
	
	@Override
	protected void append(ILoggingEvent eventObject){
		if(eventObject.getLevel().isGreaterOrEqual(Level.WARN)){
			TestThreadData.instance().addLogWarnings();
		}
	}
	
}
