package com.ziesemer.utils.io;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * <p>
 * 	Fix for <a href="https://issues.apache.org/jira/browse/XALANJ-2500"
 * 		>https://issues.apache.org/jira/browse/XALANJ-2500</a>.
 * 	Overrides {@link #flush()} to do nothing, unless called as
 * 		{@link #flush(boolean)} with <code>true</code>.
 * </p>
 * <p>
 * 	See {@link NoFlushBufferedWriter} for the
 * 		{@link java.io.Writer} equivalent.
 * </p>
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com.">&lt;www.ziesemer.com&gt;</a>
 */
public class NoFlushBufferedOutputStream extends BufferedOutputStream{
	
	public NoFlushBufferedOutputStream(OutputStream out, int sz){
		super(out, sz);
	}

	public NoFlushBufferedOutputStream(OutputStream out){
		this(out, 1 << 16);
	}
	
	@Override
	public void flush() throws IOException{
		// Do not flush.  (Performance fix.)
	}
	
	public void flush(boolean force) throws IOException{
		if(force){
			super.flush();
		}
	}
	
	@Override
	public void close() throws IOException{
		flush(true);
		super.close();
	}

}
