package com.ziesemer.utils.io;

import java.io.FilterWriter;
import java.io.IOException;

/**
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com">&lt;www.ziesemer.com&gt;</a>
 */
public class FlushCloseTestWriter extends FilterWriter{
	
	public boolean isFlushed;
	public boolean isClosed;
	public boolean writeError;
	public boolean flushError;
	public boolean closeError;
	
	public FlushCloseTestWriter(){
		super(new AppendableWriter());
	}
	
	@Override
	public void write(int c) throws IOException{
		if(writeError){
			throw new IOException("Test write exception");
		}
		super.write(c);
	}
	
	@Override
	public void write(char[] cbuf, int off, int len) throws IOException{
		if(writeError){
			throw new IOException("Test write exception");
		}
		super.write(cbuf, off, len);
	}
			
	@Override
	public void flush() throws IOException{
		super.flush();
		isFlushed = true;
		if(flushError){
			throw new IOException("Test flush exception.");
		}
	}
	
	@Override
	public void close() throws IOException{
		super.close();
		isClosed = true;
		if(closeError){
			throw new IOException("Test close exception.");
		}
	}
	
	public AppendableWriter getOut(){
		return (AppendableWriter)out;
	}
	
	@Override
	public String toString(){
		return getOut().getAppendable().toString();
	}
}
