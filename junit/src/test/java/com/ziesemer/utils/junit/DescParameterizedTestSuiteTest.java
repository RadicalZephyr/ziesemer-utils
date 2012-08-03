package com.ziesemer.utils.junit;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;

import com.ziesemer.utils.junit.DescParameterizedTestSuite.ParameterDescription;
import com.ziesemer.utils.junit.DescParameterizedTestSuite.Parameters;

/**
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com.">&lt;www.ziesemer.com&gt;</a>
 */
public class DescParameterizedTestSuiteTest extends BaseTest{
	
	@Test
	public void test() throws Throwable{
		DescParameterizedTestSuite dpts = new DescParameterizedTestSuite(TestSuite.class);
		Assert.assertEquals(2, dpts.testCount());
		Iterator<Runner> iter = dpts.getChildren().iterator();
		Description d = iter.next().getDescription();
		Assert.assertTrue(d.isSuite());
		Assert.assertEquals("[TestSet1]", d.getDisplayName());
		d = iter.next().getDescription();
		Assert.assertTrue(d.isSuite());
		Assert.assertEquals("[TestSet2]", d.getDisplayName());
	}
	
	@RunWith(DescParameterizedTestSuite.class)
	public static class TestSuite{
		
		@Parameters
		public static Collection<Object[]> getParameters(){
			return Arrays.asList(new Object[][]{
				{new TestFactory("TestSet1")},
				{new TestFactory("TestSet2")}
			});
		}
		
		@ParameterDescription
		public static Object getDescription(TestSuite.TestFactory tf){
			return tf.getDescription();
		}
		
		protected static class TestFactory{
			protected String description;
			
			protected TestFactory(){
				super();
			}
			
			public TestFactory(String description){
				this.description = description;
			}
			
			public String getDescription(){
				return description;
			}
		}
		
		public TestSuite(TestFactory tf){
			// Nothing to do.
		}
		
		@Test
		public void test() throws Exception{
			// Nothing to do.
		}
		
	}
	
}
