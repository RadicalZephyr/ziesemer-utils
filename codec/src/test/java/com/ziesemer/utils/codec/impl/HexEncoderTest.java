package com.ziesemer.utils.codec.impl;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

import org.junit.Assert;
import org.junit.Test;

import com.ziesemer.utils.codec.charLists.HexLowerCharList;
import com.ziesemer.utils.junit.BaseTest;

/**
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com.">&lt;www.ziesemer.com&gt;</a>
 */
public class HexEncoderTest extends BaseTest{
	
	@Test
	public void testAlternateCharList() throws Exception{
		HexEncoder he = new HexEncoder();
		test(he, new byte[]{(byte)0xff}, "FF");
		
		he.setByteToCharList(HexLowerCharList.build());
		test(he, new byte[]{(byte)0xff}, "ff");
	}
	
	@Test
	public void testSectionSeparator() throws Exception{
		HexEncoder he = new HexEncoder();
		test(he, new byte[]{1, 2}, "0102");
		
		he.setSeparatorFreq(1);
		test(he, new byte[]{1, 2}, "01 02");
		
		he.setSectionSeparator("_");
		test(he, new byte[]{1, 2}, "01_02");
	}
	
	@Test
	public void testSeparatorFreq() throws Exception{
		HexEncoder he = new HexEncoder();
		test(he, new byte[]{1, 2}, "0102");
		
		he.setSeparatorFreq(1);
		test(he, new byte[]{1, 2}, "01 02");
		
		he.setSeparatorFreq(4);
		test(he, new byte[]{1, 2, 3, 4, 5, 6, 7, 8}, "01020304 05060708");
	}
	
	protected void test(HexEncoder he, byte[] input, String expected) throws Exception{
		CharBuffer cb = he.code(ByteBuffer.wrap(input));
		Assert.assertEquals(expected, cb.toString());
	}
	
}
