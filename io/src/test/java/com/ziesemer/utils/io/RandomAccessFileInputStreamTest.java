package com.ziesemer.utils.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.ziesemer.utils.junit.BaseTest;

/**
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com">&lt;www.ziesemer.com&gt;</a>
 */
public class RandomAccessFileInputStreamTest extends BaseTest{
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();
	
	protected static final byte[] TEST_DATA = new byte[]{
		1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
	
	@Test
	public void test() throws Exception{
		File file = folder.newFile("Test.bin");
		
		FileOutputStream fos = new FileOutputStream(file);
		try{
			fos.write(TEST_DATA);
		}finally{
			fos.close();
		}
		
		RandomAccessFile raf = new RandomAccessFile(file, "r");
		try{
			RandomAccessFileInputStream fis = new RandomAccessFileInputStream(raf);
			
			Assert.assertEquals(raf, fis.getRandomAccessFile());
			
			try{
				byte[] bytes = new byte[TEST_DATA.length];
				
				// No reason why these read calls shouldn't return all available data,
				// 	but good practice.
				for(int read = 0, pos = 0;
						(read = fis.read(bytes, pos, bytes.length - pos)) > 0;){
					pos += read;
				}
				// Check end-of-stream.
				Assert.assertEquals(-1, fis.read());
				
				Assert.assertArrayEquals(TEST_DATA, bytes);
				fis.reset();
				
				for(int i=0; i < TEST_DATA.length; i++){
					Assert.assertEquals(TEST_DATA[i], fis.read());
				}
				fis.reset();
				
				// Below array is intentionally created too big, by 3.
				bytes = new byte[TEST_DATA.length + 3];
				
				for(int read = 0, pos = 3;
						(read = fis.read(bytes, pos, TEST_DATA.length - pos)) > 0;){
					pos += read;
				}
				
				for(int i=0; i < TEST_DATA.length - 3; i++){
					Assert.assertEquals(TEST_DATA[i], bytes[i + 3]);
				}
				fis.reset();
				
				// Test skip()
				fis.skip(5);
				Assert.assertEquals(6, fis.read());
				
				// Test mark / reset()
				Assert.assertTrue(fis.markSupported());
				fis.mark(0); // Value is not really used.
				Assert.assertEquals(7, fis.read());
				Assert.assertEquals(8, fis.read());
				fis.reset();
				Assert.assertEquals(7, fis.read());
				
				// Test extended skip()
				fis.skip(Integer.MIN_VALUE);
				Assert.assertEquals(1, fis.read());
				
				// Test extra, overloaded reset
				fis.reset(5);
				Assert.assertEquals(6, fis.read());
				
			}finally{
				fis.close();
			}
		}finally{
			// Should already be called by RandomAccessFileOutputStream's .close(), above.
			raf.close();
		}
	}

}
