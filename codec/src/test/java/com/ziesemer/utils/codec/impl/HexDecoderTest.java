package com.ziesemer.utils.codec.impl;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.BitSet;

import org.junit.Assert;
import org.junit.Test;

import com.ziesemer.utils.codec.MalformedTestSupport;
import com.ziesemer.utils.junit.BaseTest;

/**
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com.">&lt;www.ziesemer.com&gt;</a>
 */
public class HexDecoderTest extends BaseTest{
	
	@Test
	public void testMalformedLengths() throws Exception{
		HexDecoder hd = new HexDecoder();
		MalformedTestSupport.testMalformed(hd, "00FFG", 4, 1);
		MalformedTestSupport.testMalformed(hd, "00FF9G", 4, 2);
		MalformedTestSupport.testMalformed(hd, "00FFGG00", 4, 1);
	}
	
	@Test
	public void testMixedCase() throws Exception{
		HexDecoder hd = new HexDecoder();
		ByteBuffer bb = hd.code(CharBuffer.wrap("00AAaaAaaA99"));
		Assert.assertEquals(0, bb.get());
		Assert.assertEquals((byte)0xAA, bb.get());
		Assert.assertEquals((byte)0xAA, bb.get());
		Assert.assertEquals((byte)0xAA, bb.get());
		Assert.assertEquals((byte)0xAA, bb.get());
		Assert.assertEquals((byte)0x99, bb.get());
		Assert.assertFalse(bb.hasRemaining());
	}
	
	@Test
	public void testStrict() throws Exception{
		HexDecoder hd = new HexDecoder();
		MalformedTestSupport.testMalformed(hd, "00 01", 2, 1);
		MalformedTestSupport.testMalformed(hd, "00_01", 2, 1);
		MalformedTestSupport.testMalformed(hd, "00g01", 2, 1);
	}
	
	@Test
	public void testLenientAll() throws Exception{
		HexDecoder hd = new HexDecoder()
			.setLenientChars(LenientChars.ALL);
		test(hd, "00 01", new byte[]{0, 1});
		test(hd, "00_01", new byte[]{0, 1});
		test(hd, "00g01", new byte[]{0, 1});
	}
	
	@Test
	public void testLenientChars() throws Exception{
		BitSet lenientChars = new BitSet(0x100);
		lenientChars.set(' ');
		lenientChars.set('_');
		
		HexDecoder hd = new HexDecoder()
			.setLenientChars(LenientChars.partial(lenientChars));
		test(hd, "00 01", new byte[]{0, 1});
		test(hd, "00_01", new byte[]{0, 1});
		MalformedTestSupport.testMalformed(hd, "00g01", 2, 1);
	}
	
	protected void test(HexDecoder hd, String encoded, byte[] expected) throws Exception{
		ByteBuffer bb = hd.code(CharBuffer.wrap(encoded));
		byte[] actual = new byte[bb.remaining()];
		bb.get(actual);
		Assert.assertArrayEquals(expected, actual);
	}
	
}
