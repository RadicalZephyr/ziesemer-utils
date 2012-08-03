package com.ziesemer.utils.codec.impl;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

import org.junit.Assert;
import org.junit.Test;

import com.ziesemer.utils.codec.MalformedTestSupport;
import com.ziesemer.utils.junit.BaseTest;

/**
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com.">&lt;www.ziesemer.com&gt;</a>
 */
public class Base64DecoderTest extends BaseTest{
	
	@Test
	public void test() throws Exception{
		Base64Decoder b64d = new Base64Decoder();
		test(b64d, "", new byte[]{});
		
		// http://tools.ietf.org/html/rfc4648#section-9
		test(b64d, "FPucA9l+",
			new byte[]{0x14, (byte)0xfb, (byte)0x9c, 0x03, (byte)0xd9, 0x7e});
		test(b64d, "FPucA9k=",
			new byte[]{0x14, (byte)0xfb, (byte)0x9c, 0x03, (byte)0xd9});
		test(b64d, "FPucAw==",
			new byte[]{0x14, (byte)0xfb, (byte)0x9c, 0x03});
	}
	
	@Test
	public void testStrict() throws Exception{
		Base64Decoder b64d = new Base64Decoder();
		MalformedTestSupport.testMalformed(b64d, "\nF\nP\nu\nc\nA\nw\n=\n=\n", 0, 4);
	}
	
	@Test
	public void testLenient() throws Exception{
		Base64Decoder b64d = new Base64Decoder()
			.setLenientChars(LenientChars.ALL);
		test(b64d, "\nF\nP\nu\nc\nA\nw\n=\n=\n",
			new byte[]{0x14, (byte)0xfb, (byte)0x9c, 0x03});
	}
	
	protected void test(Base64Decoder b64d, String input, byte[] expected) throws Exception{
		ByteBuffer bb = b64d.code(CharBuffer.wrap(input));
		byte[] result = new byte[bb.remaining()];
		bb.get(result);
		Assert.assertArrayEquals(expected, result);
	}
	
	@Test
	public void testMalformedLengths() throws Exception{
		Base64Decoder b64d = new Base64Decoder();
		MalformedTestSupport.testMalformed(b64d, "AAAAAA=-", 4, 4);
		MalformedTestSupport.testMalformed(b64d, "AAAAA=", 4, 2);
		MalformedTestSupport.testMalformed(b64d, "AAAA=", 4, 1);
	}
	
}
