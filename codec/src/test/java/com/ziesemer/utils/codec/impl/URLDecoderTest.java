package com.ziesemer.utils.codec.impl;

import org.junit.Test;

import com.ziesemer.utils.codec.MalformedTestSupport;
import com.ziesemer.utils.junit.BaseTest;

/**
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com.">&lt;www.ziesemer.com&gt;</a>
 */
public class URLDecoderTest extends BaseTest{
	
	@Test
	public void testMalformedLengths() throws Exception{
		URLDecoder ud = new URLDecoder();
		MalformedTestSupport.testMalformed(ud, "Aa%00%", 5, 1);
		MalformedTestSupport.testMalformed(ud, "Aa%00%B", 5, 2);
		MalformedTestSupport.testMalformed(ud, "Aa%00%BG", 5, 3);
		MalformedTestSupport.testMalformed(ud, "Aa%00%BGAa", 5, 3);
		MalformedTestSupport.testMalformed(ud, "Aa%00%GGBb", 5, 2);
	}
	
}
