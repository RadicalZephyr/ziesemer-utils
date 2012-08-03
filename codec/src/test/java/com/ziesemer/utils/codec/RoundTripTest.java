package com.ziesemer.utils.codec;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

import org.junit.Assert;
import org.junit.Test;

import com.ziesemer.utils.codec.base.ByteToCharEncoder;
import com.ziesemer.utils.codec.base.CharToByteDecoder;

/**
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com.">&lt;www.ziesemer.com&gt;</a>
 */
public class RoundTripTest extends BaseCharCoderTest{
	
	public RoundTripTest(TestFactory testFactory){
		super(testFactory);
	}
	
	@Test
	public void testRoundTrip() throws Exception{
		testRoundTrip(rawTest, encodedTest);
	}
	
	@Test
	public void testRoundTripEmpty() throws Exception{
		testRoundTrip(new byte[0], testFactory.getEmptyEncodedTest());
	}
	
	protected void testRoundTrip(byte[] rawTest, String encodedTest) throws Exception{
		CharBuffer cbResult = testFactory.newEncoder().code(ByteBuffer.wrap(rawTest));
		
		cbResult.mark();
		
		Assert.assertEquals(encodedTest, cbResult.toString());
		
		cbResult.reset();
		
		ByteBuffer bbResult = testFactory.newDecoder().code(cbResult);
		byte[] result = new byte[bbResult.remaining()];
		bbResult.get(result);
		
		Assert.assertArrayEquals(rawTest, result);
	}
	
	@Test
	public void testRoundTripConvenience() throws Exception{
		testRoundTripConvenience(rawTest, encodedTest);
	}
	
	@Test
	public void testRoundTripConvenienceEmpty() throws Exception{
		testRoundTripConvenience(new byte[0], testFactory.getEmptyEncodedTest());
	}
	
	protected void testRoundTripConvenience(byte[] rawTest, String encodedTest) throws Exception{
		CharToByteDecoder<?> decoder = (CharToByteDecoder<?>)testFactory.newDecoder();
		byte[] bytes = decoder.decodeToBytes(encodedTest);
		
		Assert.assertArrayEquals(rawTest, bytes);
		
		ByteToCharEncoder<?> encoder = (ByteToCharEncoder<?>)testFactory.newEncoder();
		String chars = encoder.encodeToString(bytes);
		
		Assert.assertEquals(encodedTest, chars);
	}

}
