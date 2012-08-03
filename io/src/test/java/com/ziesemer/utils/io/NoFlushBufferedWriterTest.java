package com.ziesemer.utils.io;

import org.junit.Assert;
import org.junit.Test;

import com.ziesemer.utils.junit.BaseTest;

/**
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com">&lt;www.ziesemer.com&gt;</a>
 */
public class NoFlushBufferedWriterTest extends BaseTest{
	
	@Test
	public void test() throws Exception{
		FlushCloseTestWriter fct = new FlushCloseTestWriter();
		NoFlushBufferedWriter nfbw = new NoFlushBufferedWriter(fct);
		try{
			nfbw.write("1");
			nfbw.flush();
			Assert.assertFalse(fct.isFlushed);
			Assert.assertFalse(fct.isClosed);
			nfbw.flush(true);
			Assert.assertTrue(fct.isFlushed);
			// Reset test data, so the flush can be redetected below.
			fct.isFlushed = false;
		}finally{
			nfbw.close();
		}
		Assert.assertTrue(fct.isFlushed);
		Assert.assertTrue(fct.isClosed);
		
		Assert.assertEquals("1", fct.toString());
	}
	
}
