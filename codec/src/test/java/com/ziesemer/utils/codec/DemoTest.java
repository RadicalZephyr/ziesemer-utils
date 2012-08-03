package com.ziesemer.utils.codec;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import com.ziesemer.utils.codec.impl.Base64Decoder;
import com.ziesemer.utils.codec.impl.Base64Encoder;
import com.ziesemer.utils.codec.impl.URLDecoder;
import com.ziesemer.utils.codec.impl.URLEncoder;
import com.ziesemer.utils.junit.BaseTest;

/**
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com.">&lt;www.ziesemer.com&gt;</a>
 */
public class DemoTest extends BaseTest{
	
	/**
	 * Simple usage, taking no advantage of streaming capabilities.
	 */
	@Test
	public void testDirectSimple() throws Exception{
		IByteToCharEncoder encoder = new URLEncoder();
		ICharToByteDecoder decoder = new URLDecoder();
		
		// Random test data.
		byte[] rawData = new byte[1 << 10];
		new Random().nextBytes(rawData);
		
		// Encode.
		CharBuffer cbOut = encoder.code(ByteBuffer.wrap(rawData));
		
		// Decode (round-trip).
		ByteBuffer bbOut = decoder.code(cbOut);
		
		// Verify.
		byte[] result = new byte[bbOut.remaining()];
		bbOut.get(result);
		Assert.assertArrayEquals(rawData, result);
	}
	
	/**
	 * Simplest usage, taking no advantage of streaming capabilities, and using convenience methods.
	 */
	@Test
	public void testDirectSimpleConvenience() throws Exception{
		byte[] sampleBytes = new byte[]{0, 1, 2, 3};
		String enc = new Base64Encoder().encodeToString(sampleBytes);
		byte[] dec = new Base64Decoder().decodeToBytes(enc);
		Arrays.equals(sampleBytes, dec);
	}
	
}
