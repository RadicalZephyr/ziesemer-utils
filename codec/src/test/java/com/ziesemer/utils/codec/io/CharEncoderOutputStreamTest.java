package com.ziesemer.utils.codec.io;

import org.junit.Assert;
import org.junit.Test;

import com.ziesemer.utils.codec.IByteToCharEncoder;
import com.ziesemer.utils.io.AppendableWriter;
import com.ziesemer.utils.io.FlushCloseTestWriter;

/**
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com.">&lt;www.ziesemer.com&gt;</a>
 */
public class CharEncoderOutputStreamTest extends BaseProperIOTest{
	
	protected IByteToCharEncoder encoder = testFactory.newEncoder();
	
	public CharEncoderOutputStreamTest(TestFactory testFactory){
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
		
		CharEncoderOutputStream ceos = new CharEncoderOutputStream(encoder, actual);
		try{
			ceos.write(rawTest);
		}finally{
			ceos.close();
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
		
		CharEncoderOutputStream ceos = new CharEncoderOutputStream(encoder, actual, readBufferSize);
		try{
			ceos.write(rawTest);
		}finally{
			ceos.close();
		}
		
		Assert.assertEquals(encodedTest, actual.toString());
	}
	
	@Override
	public void testSingleChars() throws Exception{
		AppendableWriter actual = new AppendableWriter();
		
		CharEncoderOutputStream ceos = new CharEncoderOutputStream(encoder, actual);
		try{
			for(int i=0; i < rawTest.length; i++){
				ceos.write(rawTest[i]);
			}
		}finally{
			ceos.close();
		}
		
		Assert.assertEquals(encodedTest, actual.toString());
	}
	
	@Override
	public void testLoop() throws Exception{
		AppendableWriter actual = new AppendableWriter();
		AppendableWriter expected = new AppendableWriter();
		
		CharEncoderOutputStream ceos = new CharEncoderOutputStream(encoder, actual);
		try{
			for(int i=0; i < 128; i++){
				ceos.write(rawTest);
				expected.write(encodedTest);
			}
		}finally{
			ceos.close();
		}
		
		Assert.assertEquals(expected.toString(), actual.toString());
	}
	
	@Test
	public void testClose() throws Exception{
		FlushCloseTestWriter actual = new FlushCloseTestWriter();
		
		CharEncoderOutputStream ceos = new CharEncoderOutputStream(encoder, actual);
		try{
			ceos.write(rawTest);
		}finally{
			ceos.close(false);
		}
		
		Assert.assertTrue(actual.isFlushed);
		Assert.assertFalse(actual.isClosed);
		
		// Compared to CharDecoderWriterTest, actual.isFlushed will not be set on a call to .close() alone here,
		//   due to differences between the FilterWriter and FilterOutputStream used by the test writer / stream.
		
		StringBuilder sb = (StringBuilder)actual.getOut().getAppendable();
		int count = sb.length();
		ceos.close();
		// Ensure no additional output is made.
		Assert.assertEquals(count, sb.length());
		
		Assert.assertTrue(actual.isClosed);
	}
}
