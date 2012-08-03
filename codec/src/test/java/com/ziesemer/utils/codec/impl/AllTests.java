package com.ziesemer.utils.codec.impl;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com">&lt;www.ziesemer.com&gt;</a>
 */
@SuiteClasses({
	LenientCharsTest.class,
	Base64DecoderTest.class,
	Base64EncoderTest.class,
	HexDecoderTest.class,
	HexEncoderTest.class,
	URLDecoderTest.class
})
@RunWith(value=Suite.class)
public class AllTests{
	// Nothing to do.
}
