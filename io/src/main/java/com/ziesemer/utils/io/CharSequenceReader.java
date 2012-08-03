package com.ziesemer.utils.io;

import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;

/**
 * <p>
 * 	An alternative to {@link java.io.StringReader}, but supports reading from
 * 		any {@link CharSequence} in addition to {@link String}.
 * 	Optimizations included for {@link String}, {@link StringBuilder},
 * 		{@link StringBuffer}, and {@link CharBuffer}.
 * </p>
 * <p>
 * 	{@link CharBuffer#asReadOnlyBuffer()} is used on a {@link CharBuffer},
 * 		and any initial position or limits are inherited.
 * </p>
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com.">&lt;www.ziesemer.com&gt;</a>
 */
public class CharSequenceReader extends Reader{
	
	protected CharSequence cs;
	protected CSReadImpl csri;
	protected int next;
	protected int mark = 0;
	
	public CharSequenceReader(final CharSequence cs){
		this.cs = cs;
		if(cs instanceof String){
			csri = new CSReadImpl(){
				String csString = (String)cs;
				
				@Override
				public void read(int inPos, int inLen, char[] out, int outPos){
					csString.getChars(inPos, inPos + inLen, out, outPos);
				}
			};
		}else if(cs instanceof StringBuilder){
			csri = new CSReadImpl(){
				StringBuilder csSb = (StringBuilder)cs;
				
				@Override
				public void read(int inPos, int inLen, char[] out, int outPos){
					csSb.getChars(inPos, inPos + inLen, out, outPos);
				}
			};
		}else if(cs instanceof StringBuffer){
			csri = new CSReadImpl(){
				StringBuffer csSb = (StringBuffer)cs;
				
				@Override
				public void read(int inPos, int inLen, char[] out, int outPos){
					csSb.getChars(inPos, inPos + inLen, out, outPos);
				}
			};
		}else if(cs instanceof CharBuffer){
			csri = new CSReadImpl(){
				CharBuffer csCb = ((CharBuffer)cs).asReadOnlyBuffer();
				
				@Override
				public void read(int inPos, int inLen, char[] out, int outPos){
					csCb.position(inPos);
					csCb.get(out, outPos, inLen);
				}
			};
		}else{
			csri = new CSReadImpl(){
				@Override
				public void read(int inPos, int inLen, char[] out, int outPos){
					for(int i=0; i < inLen; i++){
						out[outPos + i] = cs.charAt(inPos + i);
					}
				}
			};
		}
	}
	
	private void ensureOpen() throws IOException{
		if(cs == null){
			throw new IOException("Stream closed.");
		}
	}
	
	@Override
	public int read() throws IOException{
		ensureOpen();
		if(next >= cs.length()){
			return -1;
		}
		return cs.charAt(next++);
	}

	@Override
	public int read(char[] cbuf, int off, int len) throws IOException{
		ensureOpen();
		// http://blogger.ziesemer.com/2010/01/redundant-code-in-java-io-classes.html
		if((off | len | (cbuf.length - (off + len))) < 0){
			throw new IndexOutOfBoundsException();
		}
		if(len == 0){
			return 0;
		}
		if(next >= cs.length()){
			return -1;
		}
		int n = Math.min(cs.length() - next, len);
		csri.read(next, n, cbuf, off);
		next += n;
		return n;
	}
	
	@Override
	public boolean markSupported(){
		return true;
	}
	
	@Override
	public void mark(int readAheadLimit) throws IOException{
		if(readAheadLimit < 0){
			throw new IllegalArgumentException("Read-ahead limit must be >= 0.");
		}
		ensureOpen();
		mark = next;
	}
	
	@Override
	public void reset() throws IOException{
		ensureOpen();
		next = mark;
	}
	
	/**
	 * <p>Extra overload that sets the read position to the specified position.</p>
	 */
	public void reset(int position) throws IOException{
		ensureOpen();
		next = position;
	}
	
	/**
	 * <p>This method supports a non-standard extension where negative numbers
	 * 		can be used to &quot;rewind&quot; and allow characters to be re-read.</p>
	 */
	@Override
	public long skip(long n) throws IOException{
		ensureOpen();
		n = Math.min(cs.length() - next, n);
		n = Math.max(-next, n);
		next += n;
		return n;
	}
	
	@Override
	public boolean ready() throws IOException{
		return (cs != null && next < cs.length());
	}
	
	@Override
	public void close() throws IOException{
		cs = null;
		csri = null;
	}
	
	protected abstract static class CSReadImpl{
		public abstract void read(int inPos, int inLen, char[] out, int outPos);
	}
	
}
