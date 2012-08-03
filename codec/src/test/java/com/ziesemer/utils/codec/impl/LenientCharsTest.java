package com.ziesemer.utils.codec.impl;

import java.util.BitSet;

import org.junit.Assert;
import org.junit.Test;

import com.ziesemer.utils.junit.BaseTest;

/**
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com.">&lt;www.ziesemer.com&gt;</a>
 */
public class LenientCharsTest extends BaseTest{
	
	@Test
	public void testALL(){
		LenientChars lc = LenientChars.ALL;
		Assert.assertTrue(lc.isLenient(0));
		Assert.assertTrue(lc.isLenient(' '));
		Assert.assertTrue(lc.isLenient('_'));
		Assert.assertTrue(lc.isLenient('x'));
		Assert.assertTrue(lc.isLenient(255));
	}
	
	@Test
	public void testNONE(){
		LenientChars lc = LenientChars.NONE;
		Assert.assertFalse(lc.isLenient(0));
		Assert.assertFalse(lc.isLenient(' '));
		Assert.assertFalse(lc.isLenient('_'));
		Assert.assertFalse(lc.isLenient('x'));
		Assert.assertFalse(lc.isLenient(255));
	}
	
	@Test
	public void testPartial(){
		BitSet bs = new BitSet(0x100);
		LenientChars lc = LenientChars.partial(bs);
		Assert.assertFalse(lc.isLenient(0));
		Assert.assertFalse(lc.isLenient(' '));
		Assert.assertFalse(lc.isLenient('_'));
		Assert.assertFalse(lc.isLenient('x'));
		Assert.assertFalse(lc.isLenient(255));
		
		bs.set(0);
		bs.set(' ');
		bs.set('_');
		bs.set('x');
		bs.set(255);
		
		Assert.assertTrue(lc.isLenient(0));
		Assert.assertTrue(lc.isLenient(' '));
		Assert.assertTrue(lc.isLenient('_'));
		Assert.assertTrue(lc.isLenient('x'));
		Assert.assertTrue(lc.isLenient(255));
	}
}
