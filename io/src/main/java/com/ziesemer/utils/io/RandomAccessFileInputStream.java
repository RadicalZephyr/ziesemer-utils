package com.ziesemer.utils.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

/**
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com">&lt;www.ziesemer.com&gt;</a>
 */
public class RandomAccessFileInputStream extends InputStream{
	
	protected RandomAccessFile raf;
	protected long mark = 0;
	
	public RandomAccessFileInputStream(RandomAccessFile raf){
		this.raf = raf;
	}
	
	public RandomAccessFile getRandomAccessFile(){
		return raf;
	}
	
	@Override
	public int available() throws IOException{
		return (int)(raf.length() - raf.getFilePointer());
	}
	
	@Override
	public int read() throws IOException{
		return raf.read();
	}
	
	@Override
	public int read(byte[] b) throws IOException{
		return raf.read(b);
	}
	
	@Override
	public int read(byte[] b, int off, int len) throws IOException{
		return raf.read(b, off, len);
	}
	
	@Override
	public boolean markSupported(){
		return true;
	}
	
	@Override
	public synchronized void mark(int readlimit){
		if(readlimit < 0){
			throw new IllegalArgumentException("Read-ahead limit must be >= 0.");
		}
		try{
			mark = raf.getFilePointer();
		}catch(IOException ioe){
			throw new RuntimeException(ioe);
		}
	}
	
	@Override
	public synchronized void reset() throws IOException{
		raf.seek(mark);
	}
	
	/**
	 * <p>Extra overload that sets the read position to the specified position.</p>
	 */
	public void reset(int position) throws IOException{
		raf.seek(position);
	}
	
	/**
	 * <p>This method supports a non-standard extension where negative numbers
	 * 		can be used to &quot;rewind&quot; and allow characters to be re-read.</p>
	 */
	@Override
	public long skip(long n) throws IOException{
		n = Math.min(raf.length() - raf.getFilePointer(), n);
		n = Math.max(-raf.getFilePointer(), n);
		raf.seek(raf.getFilePointer() + n);
		return n;
	}
	
	@Override
	public void close() throws IOException{
		raf.close();
	}
	
}
