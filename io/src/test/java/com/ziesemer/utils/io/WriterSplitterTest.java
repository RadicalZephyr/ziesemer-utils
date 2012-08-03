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
public class WriterSplitterTest extends BaseTest{
	
	@Test
	public void testNormal() throws Exception{
		FlushCloseTestWriter fctw1 = new FlushCloseTestWriter();
		FlushCloseTestWriter fctw2 = new FlushCloseTestWriter();
		List<FlushCloseTestWriter> writers = Arrays.asList(fctw1, fctw2);
		// Test varargs
		WriterSplitter ws = new WriterSplitter(fctw1, fctw2);
		try{
			ws.write('a');
			ws.write("bcd");
			ws.write("cdefghi", 2, 3);
			Assert.assertFalse(fctw1.isClosed);
			Assert.assertFalse(fctw2.isClosed);
			ws.flush();
			for(FlushCloseTestWriter w : writers){
				Assert.assertTrue(w.isFlushed);
			}
		}finally{
			ws.close();
		}
		
		for(FlushCloseTestWriter w : writers){
			Assert.assertTrue(w.isClosed);
			Assert.assertEquals("abcdefg", w.toString());
		}
	}
	
	@Test
	public void testErrors() throws Exception{
		FlushCloseTestWriter normal = new FlushCloseTestWriter();
		FlushCloseTestWriter writeError = new FlushCloseTestWriter();
		FlushCloseTestWriter flushError = new FlushCloseTestWriter();
		FlushCloseTestWriter closeError = new FlushCloseTestWriter();
		List<FlushCloseTestWriter> writers = Arrays.asList(
			normal, writeError, flushError, closeError);
		
		WriterSplitter ws = new WriterSplitter(writers);
		try{
			ws.write('a');
			ws.flush();
			
			writeError.writeError = true;
			ws.write('b');
			ws.flush();
			
			flushError.flushError = true;
			ws.write('c');
			ws.flush();
			
			ws.write('d');
		}finally{
			ws.close();
		}
		
		for(FlushCloseTestWriter w : writers){
			Assert.assertTrue(w.isFlushed);
			Assert.assertTrue(w.isClosed);
		}
		Assert.assertEquals("abcd", normal.toString());
		Assert.assertEquals("a", writeError.toString());
		Assert.assertEquals("abc", flushError.toString());
		Assert.assertEquals("abcd", closeError.toString());
	}
	
}
