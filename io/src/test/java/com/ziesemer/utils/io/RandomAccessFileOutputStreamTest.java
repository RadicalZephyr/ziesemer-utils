package com.ziesemer.utils.io;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
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
public class RandomAccessFileOutputStreamTest extends BaseTest{
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();
	
	@Test
	public void test() throws Exception{
		File file = folder.newFile("Test.bin");
		
		RandomAccessFile raf = new RandomAccessFile(file, "rw");
		try{
			RandomAccessFileOutputStream fos = new RandomAccessFileOutputStream(raf);
			
			Assert.assertEquals(raf, fos.getRandomAccessFile());
			
			try{
				fos.write(1);
				fos.write(new byte[]{2, 3, 4});
				fos.write(new byte[]{3, 4, 5, 6, 7, 8, 9}, 2, 3);
			}finally{
				fos.close();
			}
		}finally{
			// Should already be called by RandomAccessFileOutputStream's .close(), above.
			raf.close();
		}
		
		FileInputStream fis = new FileInputStream(file);
		try{
			DataInputStream dis = new DataInputStream(fis);
			try{
				byte[] bytes = new byte[7];
				dis.readFully(bytes);
				Assert.assertArrayEquals(
					new byte[]{1, 2, 3, 4, 5, 6, 7},
					bytes);
			}finally{
				dis.close();
			}
		}finally{
			fis.close();
		}
	}

}
