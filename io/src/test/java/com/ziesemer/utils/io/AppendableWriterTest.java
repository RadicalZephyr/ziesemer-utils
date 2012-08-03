package com.ziesemer.utils.io;

import java.io.CharArrayWriter;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.BufferOverflowException;
import java.nio.CharBuffer;

import org.junit.Assert;
import org.junit.Test;

import com.ziesemer.utils.junit.BaseTest;

/**
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com">&lt;www.ziesemer.com&gt;</a>
 */
public class AppendableWriterTest extends BaseTest{
	
	protected static final int TEST_LENGTH = 15;
	
	protected void test(Appendable a) throws Exception{
		test(new AppendableWriter(a));
	}
	
	protected void test(AppendableWriter aw) throws Exception{
		try{
			for(char c = 'a'; c <= 'e'; c++){
				aw.append(c);
			}
			
			aw.append("fghij");
			
			aw.append("ijklmnopq", 2, 7);
		}finally{
			aw.flush();
			aw.close();
		}
		
		// Not ideal, but this is only test code.
		if(aw.getAppendable() instanceof CharBuffer){
			((CharBuffer)aw.getAppendable()).flip();
		}
		
		String s = aw.toString();
		
		Assert.assertEquals(aw.getAppendable().toString(), s);
		
		Assert.assertEquals(TEST_LENGTH, s.length());
		for(int i=0; i < s.length(); i++){
			Assert.assertEquals('a' + i, s.charAt(i));
		}
	}
	
	@Test
	public void testDefault() throws Exception{
		test(new AppendableWriter());
	}
	
	@Test
	public void testStringBuilder() throws Exception{
		test(new StringBuilder());
	}
	
	@Test
	public void testStringBuffer() throws Exception{
		test(new StringBuffer());
	}
	
	@Test
	public void testStringWriter() throws Exception{
		test(new StringWriter());
	}
	
	@Test
	public void testCharArrayWriter() throws Exception{
		test(new CharArrayWriter());
	}
	
	/**
	 * <p>A {@link CharBuffer} works, but requires the buffer to be flipped
	 * 		before being read.
	 * 	(This is included in {@link #test(AppendableWriter)}.)</p>
	 * @throws Exception
	 */
	@Test
	public void testCharBuffer() throws Exception{
		test(CharBuffer.allocate(TEST_LENGTH));
	}
	
	@Test(expected=BufferOverflowException.class)
	public void testCharBufferOverflow() throws Exception{
		test(CharBuffer.allocate(TEST_LENGTH - 1));
	}
	
	/**
	 * <p>Ensures that {@link Appendable#append(CharSequence, int, int)} which uses start/end positions,
	 * 	and {@link java.io.Writer#write(String, int, int)} which uses off/len positions
	 * 	are translated correctly.</p>
	 * @throws Exception
	 */
	@Test
	public void testSubstring() throws Exception{
		AppendableWriter aw = new AppendableWriter();
		aw.write("0123456789".toCharArray(), 4, 2);
	}
	
	@Test
	public void testFlush() throws Exception{
		FlushCloseTest fct = new FlushCloseTest();
		AppendableWriter aw = new AppendableWriter(fct);
		Assert.assertFalse(fct.isFlushed);
		Assert.assertFalse(fct.isClosed);
		aw.flush();
		Assert.assertTrue(fct.isFlushed);
		Assert.assertFalse(fct.isClosed);
	}
	
	@Test
	public void testClose() throws Exception{
		FlushCloseTest fct = new FlushCloseTest();
		AppendableWriter aw = new AppendableWriter(fct);
		Assert.assertFalse(fct.isClosed);
		aw.close();
		Assert.assertTrue(fct.isClosed);
	}
	
	protected static class FlushCloseTest implements Appendable, Flushable, Closeable{
		
		public boolean isFlushed;
		public boolean isClosed;
		
		public Appendable append(char c) throws IOException{
			return this;
		}
		
		public Appendable append(CharSequence csq) throws IOException{
			return this;
		}
		
		public Appendable append(CharSequence csq, int start, int end) throws IOException{
			return this;
		}
		
		public void flush() throws IOException{
			isFlushed = true;
		}
		
		public void close() throws IOException{
			isClosed = true;
		}
		
	}
	
}
