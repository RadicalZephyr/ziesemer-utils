package com.ziesemer.utils.io;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.ziesemer.utils.junit.BaseTest;

/**
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com">&lt;www.ziesemer.com&gt;</a>
 */
public class OutputStreamSplitterTest extends BaseTest{
	
	@Test
	public void testNormal() throws Exception{
		FlushCloseTestOutputStream fctos1 = new FlushCloseTestOutputStream();
		FlushCloseTestOutputStream fctos2 = new FlushCloseTestOutputStream();
		List<FlushCloseTestOutputStream> streams = Arrays.asList(fctos1, fctos2);
		// Test varargs
		OutputStreamSplitter oss = new OutputStreamSplitter(fctos1, fctos2);
		try{
			oss.write(1);
			oss.write(new byte[]{2, 3, 4});
			oss.write(new byte[]{3, 4, 5, 6, 7, 8, 9}, 2, 3);
			Assert.assertFalse(fctos1.isClosed);
			Assert.assertFalse(fctos2.isClosed);
			oss.flush();
			for(FlushCloseTestOutputStream os : streams){
				Assert.assertTrue(os.isFlushed);
			}
		}finally{
			oss.close();
		}
		
		for(FlushCloseTestOutputStream os : streams){
			Assert.assertTrue(os.isClosed);
			Assert.assertArrayEquals(
				new byte[]{1, 2, 3, 4, 5, 6, 7},
				os.getOut().toByteArray());
		}
	}
	
	@Test
	public void testErrors() throws Exception{
		FlushCloseTestOutputStream normal = new FlushCloseTestOutputStream();
		FlushCloseTestOutputStream writeError = new FlushCloseTestOutputStream();
		FlushCloseTestOutputStream flushError = new FlushCloseTestOutputStream();
		FlushCloseTestOutputStream closeError = new FlushCloseTestOutputStream();
		List<FlushCloseTestOutputStream> streams = Arrays.asList(
			normal, writeError, flushError, closeError);
		
		OutputStreamSplitter oss = new OutputStreamSplitter(streams);
		try{
			oss.write(1);
			oss.flush();
			
			writeError.writeError = true;
			oss.write(2);
			oss.flush();
			
			flushError.flushError = true;
			oss.write(3);
			oss.flush();
			
			oss.write(4);
		}finally{
			oss.close();
		}
		
		for(FlushCloseTestOutputStream w : streams){
			Assert.assertTrue(w.isFlushed);
			Assert.assertTrue(w.isClosed);
		}
		Assert.assertEquals(4, normal.getOut().toByteArray().length);
		Assert.assertEquals(1, writeError.getOut().toByteArray().length);
		Assert.assertEquals(3, flushError.getOut().toByteArray().length);
		Assert.assertEquals(4, closeError.getOut().toByteArray().length);
	}
	
}
