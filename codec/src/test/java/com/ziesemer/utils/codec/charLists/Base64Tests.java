package com.ziesemer.utils.codec.charLists;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.ziesemer.utils.junit.BaseTest;
import com.ziesemer.utils.junit.DescParameterizedTestSuite;
import com.ziesemer.utils.junit.DescParameterizedTestSuite.ParameterDescription;
import com.ziesemer.utils.junit.DescParameterizedTestSuite.Parameters;

/**
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com">&lt;www.ziesemer.com&gt;</a>
 */
@RunWith(DescParameterizedTestSuite.class)
public class Base64Tests extends BaseTest{
	
	@Parameters
	public static Collection<Object[]> getParameters(){
		return Arrays.asList(new Object[][]{
			{Base64CharList.class},
			{Base64URLCharList.class}
		});
	}
	
	@ParameterDescription
	public static Object getDescription(Class<?> c){
		return c.getName();
	}
	
	protected final Class<?> c;
	
	public Base64Tests(Class<?> c){
		this.c = c;
	}
	
	@Test
	public void test() throws Exception{
		// At this point, only checking that calls complete without exception.
		char[] chars = (char[])c.getMethod("build").invoke(null);
		Base64CharList.reverse(chars);
	}
	
}
