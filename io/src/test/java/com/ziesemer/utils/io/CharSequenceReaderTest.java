package com.ziesemer.utils.io;

import java.nio.CharBuffer;

import org.junit.Assert;
import org.junit.Test;

import com.ziesemer.utils.junit.BaseTest;

/**
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com">&lt;www.ziesemer.com&gt;</a>
 */
public class CharSequenceReaderTest extends BaseTest{
	
	protected static final String TEST_DATA = "abcdefghijklmno";
	
	protected void test(CharSequence cs) throws Exception{
		test(new CharSequenceReader(cs));
	}
	
	protected void test(CharSequenceReader csr) throws Exception{
		try{
			Assert.assertTrue(csr.ready());
			char[] chars = new char[TEST_DATA.length()];
			
			// No reason why these read calls shouldn't return all available data,
			// 	but good practice.
			for(int read = 0, pos = 0;
					(read = csr.read(chars, pos, chars.length - pos)) > 0;){
				pos += read;
			}
			// Check end-of-stream.
			Assert.assertEquals(-1, csr.read());
			
			Assert.assertArrayEquals(TEST_DATA.toCharArray(), chars);
			Assert.assertFalse(csr.ready());
			csr.reset();
			
			Assert.assertTrue(csr.ready());
			for(int i=0; i < TEST_DATA.length(); i++){
				Assert.assertEquals(TEST_DATA.charAt(i), csr.read());
			}
			Assert.assertFalse(csr.ready());
			csr.reset();
			
			Assert.assertTrue(csr.ready());
			// Below array is intentionally created too big, by 3.
			chars = new char[TEST_DATA.length() + 3];
			
			for(int read = 0, pos = 3;
					(read = csr.read(chars, pos, TEST_DATA.length() - pos)) > 0;){
				pos += read;
			}
			
			for(int i=0; i < TEST_DATA.length() - 3; i++){
				Assert.assertEquals(TEST_DATA.charAt(i), chars[i + 3]);
			}
			Assert.assertTrue(csr.ready());
			csr.reset();
			
			// Test skip()
			csr.skip(5);
			Assert.assertEquals('f', csr.read());
			
			// Test mark / reset()
			Assert.assertTrue(csr.markSupported());
			csr.mark(0); // Value is not really used.
			Assert.assertEquals('g', csr.read());
			Assert.assertEquals('h', csr.read());
			csr.reset();
			Assert.assertEquals('g', csr.read());
			
			// Test extended skip()
			csr.skip(Integer.MIN_VALUE);
			Assert.assertEquals('a', csr.read());
			
			// Test extra, overloaded reset
			csr.reset(5);
			Assert.assertEquals('f', csr.read());
			
		}finally{
			csr.close();
		}
	}
	
	@Test
	public void testString() throws Exception{
		test(TEST_DATA);
	}
	
	@Test
	public void testStringBuilder() throws Exception{
		test(new StringBuilder(TEST_DATA));
	}
	
	@Test
	public void testStringBuffer() throws Exception{
		test(new StringBuffer(TEST_DATA));
	}
	
	@Test
	public void testCharBuffer() throws Exception{
		test(CharBuffer.wrap(TEST_DATA));
	}
	
	@Test
	public void testGeneric() throws Exception{
		test(new CharSequence(){
			
			public int length(){
				return TEST_DATA.length();
			}
			
			public char charAt(int index){
				return TEST_DATA.charAt(index);
			}
			
			public CharSequence subSequence(int start, int end){
				return TEST_DATA.subSequence(start, end);
			}
		});
	}
	
}
