package com.ziesemer.utils.codec.io;

import org.junit.Test;

import com.ziesemer.utils.codec.BaseCharCoderTest;

/**
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com.">&lt;www.ziesemer.com&gt;</a>
 */
public abstract class BaseProperIOTest extends BaseCharCoderTest{
	
	public BaseProperIOTest(TestFactory testFactory){
		super(testFactory);
	}

	@Test
	public abstract void testSimple() throws Exception;
	
	@Test
	public abstract void testShort() throws Exception;
	
	@Test
	public void testSmallerBufferThanCodec() throws Exception{
		test(1);
	}
	
	@Test
	public abstract void testSmallerBufferThanInput() throws Exception;
	
	protected abstract void test(int readBufferSize) throws Exception;
	
	@Test
	public abstract void testSingleChars() throws Exception;
	
	@Test
	public abstract void testLoop() throws Exception;
	
	/**
	 * Causes the same encoder/decoder to be reused.
	 */
	@Test
	public void testRepeat() throws Exception{
		for(int i=0; i < 5; i++){
			testSimple();
		}
	}
	
}
