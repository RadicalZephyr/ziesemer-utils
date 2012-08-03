package com.ziesemer.utils.codec.io;

import java.io.ByteArrayOutputStream;

import org.junit.Assert;
import org.junit.Test;

import com.ziesemer.utils.codec.ICharToByteDecoder;
import com.ziesemer.utils.io.FlushCloseTestOutputStream;

/**
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com.">&lt;www.ziesemer.com&gt;</a>
 */
public class CharDecoderWriterTest extends BaseProperIOTest{
	
	protected ICharToByteDecoder decoder = testFactory.newDecoder();
	
	public CharDecoderWriterTest(TestFactory testFactory){
		super(testFactory);
	}
	
	@Override
	public void testSimple() throws Exception{
		test(encodedTest, rawTest);
	}
	
	@Override
	public void testShort() throws Exception{
		test(testFactory.getShortEncodedTest(), testFactory.getShortDecodedTest());
	}
	
	protected void test(String encodedTest, byte[] rawTest) throws Exception{
		ByteArrayOutputStream actual = new ByteArrayOutputStream();
		
		CharDecoderWriter cdw = new CharDecoderWriter(decoder, actual);
		try{
			cdw.write(encodedTest);
		}finally{
			cdw.close();
		}
		
		Assert.assertArrayEquals(rawTest, actual.toByteArray());
	}
	
	@Override
	public void testSmallerBufferThanCodec() throws Exception{
		test(1);
	}
	
	@Override
	public void testSmallerBufferThanInput() throws Exception{
		int size = (int)Math.ceil(decoder.getMinInPerOut()) + 1;
		if(size >= encodedTest.length()){
			throw new Exception("Test input too small for codec.");
		}
		test(size);
	}
	
	@Override
	protected void test(int readBufferSize) throws Exception{
		ByteArrayOutputStream actual = new ByteArrayOutputStream();
		
		CharDecoderWriter cdw = new CharDecoderWriter(decoder, actual, readBufferSize);
		try{
			cdw.write(encodedTest);
		}finally{
			cdw.close();
		}
		
		Assert.assertArrayEquals(rawTest, actual.toByteArray());
	}
	
	@Override
	public void testSingleChars() throws Exception{
		ByteArrayOutputStream actual = new ByteArrayOutputStream();
		
		CharDecoderWriter cdw = new CharDecoderWriter(decoder, actual);
		try{
			for(int i=0; i < encodedTest.length(); i++){
				cdw.write(encodedTest.charAt(i));
			}
		}finally{
			cdw.close();
		}
		
		Assert.assertArrayEquals(rawTest, actual.toByteArray());
	}
	
	@Override
	public void testLoop() throws Exception{
		ByteArrayOutputStream actual = new ByteArrayOutputStream();
		ByteArrayOutputStream expected = new ByteArrayOutputStream();
		
		CharDecoderWriter cdw = new CharDecoderWriter(decoder, actual);
		try{
			for(int i=0; i < 128; i++){
				cdw.write(encodedTest);
				expected.write(rawTest);
			}
		}finally{
			cdw.close();
		}
		
		Assert.assertArrayEquals(expected.toByteArray(), actual.toByteArray());
	}
	
	@Test
	public void testClose() throws Exception{
		FlushCloseTestOutputStream actual = new FlushCloseTestOutputStream();
		
		CharDecoderWriter cdw = new CharDecoderWriter(decoder, actual);
		try{
			cdw.write(encodedTest);
		}finally{
			cdw.close(false);
		}
		
		Assert.assertTrue(actual.isFlushed);
		Assert.assertFalse(actual.isClosed);
		
		actual.isFlushed = false;
		int count = actual.getOut().size();
		cdw.close();
		// Ensure no additional output is made.
		Assert.assertEquals(count, actual.getOut().size());
		
		Assert.assertTrue(actual.isFlushed);
		Assert.assertTrue(actual.isClosed);
	}
}
