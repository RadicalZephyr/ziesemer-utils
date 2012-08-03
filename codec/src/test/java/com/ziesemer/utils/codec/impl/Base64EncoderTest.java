package com.ziesemer.utils.codec.impl;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

import org.junit.Assert;
import org.junit.Test;

import com.ziesemer.utils.codec.charLists.Base64URLCharList;
import com.ziesemer.utils.junit.BaseTest;

/**
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com.">&lt;www.ziesemer.com&gt;</a>
 */
public class Base64EncoderTest extends BaseTest{
	
	@Test
	public void test() throws Exception{
		Base64Encoder b64e = new Base64Encoder();
		test(b64e, new byte[]{}, "");
		
		// http://tools.ietf.org/html/rfc4648#section-9
		test(b64e, new byte[]{0x14, (byte)0xfb, (byte)0x9c, 0x03, (byte)0xd9, 0x7e},
			"FPucA9l+");
		test(b64e, new byte[]{0x14, (byte)0xfb, (byte)0x9c, 0x03, (byte)0xd9},
			"FPucA9k=");
		test(b64e, new byte[]{0x14, (byte)0xfb, (byte)0x9c, 0x03},
			"FPucAw==");
		
		test(b64e, new byte[]{-5, -16}, "+/A=");
	}
	
	@Test
	public void testAlternateCharList() throws Exception{
		Base64Encoder b64e = new Base64Encoder();
		b64e.setByteToCharList(Base64URLCharList.build());
		test(b64e, new byte[]{}, "");
		
		// http://tools.ietf.org/html/rfc4648#section-9, with required edit for the alternate character list.
		test(b64e, new byte[]{0x14, (byte)0xfb, (byte)0x9c, 0x03, (byte)0xd9, 0x7e},
			"FPucA9l-");
		test(b64e, new byte[]{0x14, (byte)0xfb, (byte)0x9c, 0x03, (byte)0xd9},
			"FPucA9k=");
		test(b64e, new byte[]{0x14, (byte)0xfb, (byte)0x9c, 0x03},
			"FPucAw==");
		
		test(b64e, new byte[]{-5, -16}, "-_A=");
	}
	
	@Test
	public void testSectionSeparator() throws Exception{
		Base64Encoder b64e = new Base64Encoder();
		test(b64e, new byte[]{0, 0, 0, 0}, "AAAAAA==");
		
		b64e.setSeparatorFreq(4);
		test(b64e, new byte[]{0, 0, 0, 0}, "AAAA\r\nAA==");
		
		b64e.setSectionSeparator(" ");
		test(b64e, new byte[]{0, 0, 0, 0}, "AAAA AA==");
	}
	
	@Test
	public void testSeparatorFreq() throws Exception{
		Base64Encoder b64e = new Base64Encoder();
		test(b64e, new byte[]{0, 0, 0, 0}, "AAAAAA==");
		
		b64e.setSeparatorFreq(4);
		test(b64e, new byte[]{0, 0, 0, 0}, "AAAA\r\nAA==");
		
		b64e.setSeparatorFreq(8);
		test(b64e, new byte[]{0, 0, 0, 0, 0, 0, 0, 0}, "AAAAAAAA\r\nAAA=");
	}
	
	protected void test(Base64Encoder b64e, byte[] input, String expected) throws Exception{
		CharBuffer cb = b64e.code(ByteBuffer.wrap(input));
		Assert.assertEquals(expected, cb.toString());
	}
	
}
