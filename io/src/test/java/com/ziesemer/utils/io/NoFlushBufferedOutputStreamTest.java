package com.ziesemer.utils.io;

import org.junit.Assert;
import org.junit.Test;

import com.ziesemer.utils.junit.BaseTest;

/**
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com">&lt;www.ziesemer.com&gt;</a>
 */
public class NoFlushBufferedOutputStreamTest extends BaseTest{
	
	@Test
	public void test() throws Exception{
		FlushCloseTestOutputStream fct = new FlushCloseTestOutputStream();
		NoFlushBufferedOutputStream nfbos = new NoFlushBufferedOutputStream(fct);
		try{
			nfbos.write(1);
			nfbos.flush();
			Assert.assertFalse(fct.isFlushed);
			Assert.assertFalse(fct.isClosed);
			nfbos.flush(true);
			Assert.assertTrue(fct.isFlushed);
			// Reset test data, so the flush can be redetected below.
			fct.isFlushed = false;
		}finally{
			nfbos.close();
		}
		Assert.assertTrue(fct.isFlushed);
		Assert.assertTrue(fct.isClosed);
		
		Assert.assertArrayEquals(new byte[]{1}, fct.getOut().toByteArray());
	}
	
}
