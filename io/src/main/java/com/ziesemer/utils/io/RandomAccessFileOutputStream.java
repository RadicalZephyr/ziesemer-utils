package com.ziesemer.utils.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

/**
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com">&lt;www.ziesemer.com&gt;</a>
 */
public class RandomAccessFileOutputStream extends OutputStream{
	
	protected RandomAccessFile raf;

	public RandomAccessFileOutputStream(RandomAccessFile raf) throws IOException{
		this(raf, false);
	}
	
	public RandomAccessFileOutputStream(RandomAccessFile raf, boolean append) throws IOException{
		this.raf = raf;
		if(!append){
			raf.seek(0);
			raf.setLength(0);
		}
	}
	
	public RandomAccessFile getRandomAccessFile(){
		return raf;
	}
	
	@Override
	public void write(int b) throws IOException{
		raf.write(b);
	}
	
	@Override
	public void write(byte[] b) throws IOException{
		raf.write(b);
	}
	
	@Override
	public void write(byte[] b, int off, int len) throws IOException{
		raf.write(b, off, len);
	}
	
	@Override
	public void flush() throws IOException{
		raf.getFD().sync();
		super.flush();
	}
	
	@Override
	public void close() throws IOException{
		raf.close();
		super.close();
	}
	
}
