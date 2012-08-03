package com.ziesemer.utils.io;

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;

/**
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com">&lt;www.ziesemer.com&gt;</a>
 */
public class FlushCloseTestOutputStream extends FilterOutputStream{
	
	public boolean isFlushed;
	public boolean isClosed;
	public boolean writeError;
	public boolean flushError;
	public boolean closeError;
	
	public FlushCloseTestOutputStream(){
		super(new ByteArrayOutputStream());
	}

	@Override
	public void write(int c) throws IOException{
		if(writeError){
			throw new IOException("Test write exception");
		}
		super.write(c);
	}
	
	@Override
	public void write(byte[] b, int off, int len) throws IOException{
		if(writeError){
			throw new IOException("Test write exception");
		}
		super.write(b, off, len);
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
	
	public ByteArrayOutputStream getOut(){
		return (ByteArrayOutputStream)out;
	}
}
