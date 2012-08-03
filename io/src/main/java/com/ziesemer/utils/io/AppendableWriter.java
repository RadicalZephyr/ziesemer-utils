package com.ziesemer.utils.io;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.Writer;
import java.nio.CharBuffer;

/**
 * <p>
 * 	A faster alternative to {@link java.io.StringWriter}.
 * 	Uses a non-synchronized {@link StringBuilder} by default instead of the
 * 		synchronized {@link StringBuffer}, but also accepts any {@link Appendable}
 * 		as the underlying destination.
 * </p>
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com.">&lt;www.ziesemer.com&gt;</a>
 */
public class AppendableWriter extends Writer{
	
	protected Appendable appendable;
	
	public AppendableWriter(){
		this(new StringBuilder());
	}
	
	public AppendableWriter(Appendable appendable){
		this.appendable = appendable;
	}
	
	@Override
	public void write(int c) throws IOException{
		appendable.append((char)c);
	}
	
	@Override
	public void write(char[] cbuf, int off, int len) throws IOException{
		appendable.append(CharBuffer.wrap(cbuf, off, len));
	}
	
	@Override
	public void flush() throws IOException{
		if(appendable instanceof Flushable){
			((Flushable)appendable).flush();
		}
	}

	@Override
	public void close() throws IOException{
		if(appendable instanceof Closeable){
			((Closeable)appendable).close();
		}
	}
	
	public Appendable getAppendable(){
		return appendable;
	}
	
	@Override
	public String toString(){
		return appendable.toString();
	}
	
}
