package com.ziesemer.utils.junit;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com.">&lt;www.ziesemer.com&gt;</a>
 */
public class BaseTestTest extends BaseTest{

	@Test
	public void baseTest() throws Exception{
		// Nothing to do - simply ensure that test completes without exception.
	}
	
	@Test
	public void testGetTestOutput() throws Exception{
		File f = getTestOutput();
		Assert.assertEquals(new File("target/JUnit/com/ziesemer/utils/junit"), f);
	}
	
}
