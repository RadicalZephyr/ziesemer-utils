package com.ziesemer.utils.codec;

import java.nio.CharBuffer;
import java.nio.charset.MalformedInputException;

import com.ziesemer.utils.junit.BaseTest;

import junit.framework.Assert;

/**
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com.">&lt;www.ziesemer.com&gt;</a>
 */
public class MalformedTestSupport extends BaseTest{
	
	public static void testMalformed(ICharToByteDecoder decoder, String testStr,
			int expectedPosition, int expectedLength) throws Exception{
		CharBuffer cb = CharBuffer.wrap(testStr);
		try{
			decoder.code(cb);
			Assert.fail("No MalformedInputException caught.");
		}catch(MalformedInputException mie){
			Assert.assertEquals("position", expectedPosition, cb.position());
			Assert.assertEquals("length", expectedLength, mie.getInputLength());
		}
	}
	
	public static void testMalformed(ICharToByteDecoder decoder, String testStr)
			throws Exception{
		CharBuffer cb = CharBuffer.wrap(testStr);
		try{
			decoder.code(cb);
			Assert.fail("No MalformedInputException caught.");
		}catch(MalformedInputException mie){
			// Expected.
		}
	}
	
}
