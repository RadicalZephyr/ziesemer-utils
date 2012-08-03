package com.ziesemer.utils.codec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com.">&lt;www.ziesemer.com&gt;</a>
 */
public class ConfigTest extends BaseCharCoderTest{
	
	public ConfigTest(TestFactory testFactory){
		super(testFactory);
	}
	
	@Test
	public void testConfig() throws Exception{
		IByteToCharEncoder encoder = testFactory.newEncoder();
		ICharToByteDecoder decoder = testFactory.newDecoder();
		
		IByteToCharEncoder encoder2 = testFactory.newEncoder();
		ICharToByteDecoder decoder2 = testFactory.newDecoder();
		
		// At this point, only checking that calls complete without exception...
		encoder.config(encoder2);
		decoder.config(decoder2);
		
		// Use the coders...
		encoder.code(ByteBuffer.wrap(testFactory.getShortDecodedTest()));
		decoder.code(CharBuffer.wrap(testFactory.getShortEncodedTest()));
		
		// Ensure the coders can be reconfigured, as the coder should now be in the "RESET" state...
		encoder.config(encoder2);
		decoder.config(decoder2);
		
		// Perform only partial operations...
		encoder.code(ByteBuffer.wrap(testFactory.getShortDecodedTest()),
			CharBuffer.allocate(0), false);
		decoder.code(CharBuffer.wrap(testFactory.getShortEncodedTest()),
			ByteBuffer.allocate(0), false);
		
		// Ensure that coders can't be reconfigured while active...
		assertConfigIllegalStateEx(encoder, encoder2);
		assertConfigIllegalStateEx(decoder, decoder2);
		
		// Unless reset...
		encoder.reset();
		decoder.reset();
		
		encoder.config(encoder2);
		decoder.config(decoder2);
	}
	
	protected <IN extends Buffer, OUT extends Buffer>
			void assertConfigIllegalStateEx(ICoder<IN, OUT> toConfig, ICoder<IN, OUT> fromConfig) throws Exception{
		try{
			toConfig.config(fromConfig);
			Assert.fail();
		}catch(IllegalStateException ise){
			// Expected.
		}
	}
	
	@Test
	public void testClone() throws Exception{
		IByteToCharEncoder encoder = testFactory.newEncoder();
		ICharToByteDecoder decoder = testFactory.newDecoder();
		
		IByteToCharEncoder encoderClone = (IByteToCharEncoder)encoder.clone();
		ICharToByteDecoder decoderClone = (ICharToByteDecoder)decoder.clone();
		
		Assert.assertFalse(encoder == encoderClone);
		Assert.assertTrue(encoder.getClass().isInstance(encoderClone));
		Assert.assertFalse(decoder == decoderClone);
		Assert.assertTrue(decoder.getClass().isInstance(decoderClone));
	}
	
	@Test
	public void testSerialize() throws Exception{
		IByteToCharEncoder encoder = testFactory.newEncoder();
		ICharToByteDecoder decoder = testFactory.newDecoder();
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try{
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			try{
				oos.writeObject(encoder);
				oos.writeObject(decoder);
			}finally{
				oos.close();
			}
		}finally{
			baos.close();
		}
		
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		try{
			ObjectInputStream ois = new ObjectInputStream(bais);
			try{
				Assert.assertTrue(encoder.getClass().isInstance(ois.readObject()));
				Assert.assertTrue(decoder.getClass().isInstance(ois.readObject()));
			}finally{
				ois.close();
			}
		}finally{
			bais.close();
		}
	}

}
