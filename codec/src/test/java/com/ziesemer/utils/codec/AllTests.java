package com.ziesemer.utils.codec;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com">&lt;www.ziesemer.com&gt;</a>
 */
@SuiteClasses({
	ConfigTest.class,
	DemoTest.class,
	InsaneTest.class,
	RoundTripTest.class,
	com.ziesemer.utils.codec.charLists.AllTests.class,
	com.ziesemer.utils.codec.impl.AllTests.class,
	com.ziesemer.utils.codec.io.AllTests.class
})
@RunWith(value=Suite.class)
public class AllTests{
	// Nothing to do.
}
