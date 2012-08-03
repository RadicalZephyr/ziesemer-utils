package com.ziesemer.utils.codec.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.junit.Assert;

import com.ziesemer.utils.codec.IByteToCharEncoder;
import com.ziesemer.utils.io.AppendableWriter;

/**
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com.">&lt;www.ziesemer.com&gt;</a>
 */
public class CharEncoderReaderTest extends BaseProperIOTest{
	
	protected IByteToCharEncoder encoder = testFactory.newEncoder();
	
	public CharEncoderReaderTest(TestFactory testFactory){
		super(testFactory);
	}
	
	@Override
	public void testSimple() throws Exception{
		test(rawTest, encodedTest);
	}
	
	@Override
	public void testShort() throws Exception{
		test(testFactory.getShortDecodedTest(), testFactory.getShortEncodedTest());
	}
	
	protected void test(byte[] rawTest, String encodedTest) throws Exception{
		AppendableWriter actual = new AppendableWriter();
		
		ByteArrayInputStream bais = new ByteArrayInputStream(rawTest);
		CharEncoderReader cer = new CharEncoderReader(encoder, bais);
		try{
			int read;
			char[] buffer = new char[1 << 10];
			while((read = cer.read(buffer)) != -1){
				actual.write(buffer, 0, read);
			}
		}finally{
			cer.close();
		}
		
		Assert.assertEquals(encodedTest, actual.toString());
	}
	
	@Override
	public void testSmallerBufferThanCodec() throws Exception{
		test(1);
	}
	
	@Override
	public void testSmallerBufferThanInput() throws Exception{
		int size = (int)Math.ceil(encoder.getMinInPerOut()) + 1;
		if(size >= rawTest.length){
			throw new Exception("Test input too small for codec.");
		}
		test(size);
	}
	
	@Override
	protected void test(int readBufferSize) throws Exception{
		AppendableWriter actual = new AppendableWriter();
		
		ByteArrayInputStream bais = new ByteArrayInputStream(rawTest);
		CharEncoderReader cer = new CharEncoderReader(encoder, bais, readBufferSize);
		try{
			int read;
			char[] buffer = new char[1 << 10];
			while((read = cer.read(buffer)) != -1){
				actual.write(buffer, 0, read);
			}
		}finally{
			cer.close();
		}
		
		Assert.assertEquals(encodedTest, actual.toString());
	}
	
	@Override
	public void testSingleChars() throws Exception{
		AppendableWriter actual = new AppendableWriter();
		
		ByteArrayInputStream bais = new ByteArrayInputStream(rawTest);
		CharEncoderReader cer = new CharEncoderReader(encoder, bais);
		try{
			int read;
			while((read = cer.read()) != -1){
				actual.write(read);
			}
		}finally{
			cer.close();
		}
		
		Assert.assertEquals(encodedTest, actual.toString());
	}
	
	@Override
	public void testLoop() throws Exception{
		AppendableWriter actual = new AppendableWriter();
		AppendableWriter expected = new AppendableWriter();
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		for(int i=0; i < 128; i++){
			baos.write(rawTest);
			expected.write(encodedTest);
		}
		
		ByteArrayInputStream in = new ByteArrayInputStream(baos.toByteArray());
		CharEncoderReader cer = new CharEncoderReader(encoder, in);
		try{
			int read;
			char[] buffer = new char[1 << 10];
			while((read = cer.read(buffer)) != -1){
				actual.write(buffer, 0, read);
			}
		}finally{
			cer.close();
		}
		
		Assert.assertEquals(expected.toString(), actual.toString());
	}
}
