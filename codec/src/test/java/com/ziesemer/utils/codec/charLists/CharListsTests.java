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
public class CharListsTests extends BaseTest{
	
	@Parameters
	public static Collection<Object[]> getParameters(){
		return Arrays.asList(new Object[][]{
			{Base64CharList.class},
			{Base64URLCharList.class},
			{HexLowerCharList.class},
			{HexUpperCharList.class}
		});
	}
	
	@ParameterDescription
	public static Object getDescription(Class<?> c){
		return c.getName();
	}
	
	protected final Class<?> c;
	
	public CharListsTests(Class<?> c){
		this.c = c;
	}
	
	@Test
	public void test() throws Exception{
		// At this point, only checking that instance is constructed without exception.
		c.newInstance();
	}
	
}
