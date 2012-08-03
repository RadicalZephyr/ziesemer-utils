package com.ziesemer.utils.codec.io;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;

import org.junit.Assert;

import com.ziesemer.utils.codec.ICharToByteDecoder;

/**
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com.">&lt;www.ziesemer.com&gt;</a>
 */
public class CharDecoderInputStreamTest extends BaseProperIOTest{
	
	protected ICharToByteDecoder decoder = testFactory.newDecoder();
	
	public CharDecoderInputStreamTest(TestFactory testFactory){
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
	
	protected void test(String encodedTest, byte[] actualTest) throws Exception{
		ByteArrayOutputStream actual = new ByteArrayOutputStream();
		
		StringReader sr = new StringReader(encodedTest);
		CharDecoderInputStream cdis = new CharDecoderInputStream(decoder, sr);
		try{
			int read;
			byte[] buffer = new byte[1 << 10];
			while((read = cdis.read(buffer)) != -1){
				actual.write(buffer, 0, read);
			}
		}finally{
			cdis.close();
		}
		
		Assert.assertArrayEquals(actualTest, actual.toByteArray());
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
		
		StringReader sr = new StringReader(encodedTest);
		CharDecoderInputStream cdis = new CharDecoderInputStream(decoder, sr, readBufferSize);
		try{
			int read;
			byte[] buffer = new byte[1 << 10];
			while((read = cdis.read(buffer)) != -1){
				actual.write(buffer, 0, read);
			}
		}finally{
			cdis.close();
		}
		
		Assert.assertArrayEquals(rawTest, actual.toByteArray());
	}
	
	@Override
	public void testSingleChars() throws Exception{
		ByteArrayOutputStream actual = new ByteArrayOutputStream();
		
		StringReader sr = new StringReader(encodedTest);
		CharDecoderInputStream cdis = new CharDecoderInputStream(decoder, sr);
		try{
			int read;
			while((read = cdis.read()) != -1){
				actual.write(read);
			}
		}finally{
			cdis.close();
		}
		
		Assert.assertArrayEquals(rawTest, actual.toByteArray());
	}
	
	@Override
	public void testLoop() throws Exception{
		ByteArrayOutputStream actual = new ByteArrayOutputStream();
		ByteArrayOutputStream expected = new ByteArrayOutputStream();
		
		StringBuilder in = new StringBuilder();
		for(int i=0; i < 128; i++){
			in.append(encodedTest);
			expected.write(rawTest);
		}
		
		StringReader sr = new StringReader(in.toString());
		CharDecoderInputStream cdis = new CharDecoderInputStream(decoder, sr);
		try{
			int read;
			byte[] buffer = new byte[1 << 10];
			while((read = cdis.read(buffer)) != -1){
				actual.write(buffer, 0, read);
			}
		}finally{
			cdis.close();
		}
		
		Assert.assertArrayEquals(expected.toByteArray(), actual.toByteArray());
	}
}
