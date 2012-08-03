package com.ziesemer.utils.codec;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;

import org.junit.Assert;
import org.junit.Test;

import com.ziesemer.utils.codec.io.CharDecoderInputStream;
import com.ziesemer.utils.codec.io.CharEncoderOutputStream;
import com.ziesemer.utils.io.AppendableWriter;

/**
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com.">&lt;www.ziesemer.com&gt;</a>
 */
public class InsaneTest extends BaseCharCoderTest{
	
	public InsaneTest(TestFactory testFactory){
		super(testFactory);
	}
	
	/**
	 * <p>Similar to {@link ConfigTest#testSerialize()}, but uses the encoder and decoder
	 * 	to serialize/deserialize themselves as characters instead of bytes.</p>
	 */
	@Test
	public void testInsane() throws Exception{
		IByteToCharEncoder encoder = testFactory.newEncoder();
		ICharToByteDecoder decoder = testFactory.newDecoder();
		
		final AppendableWriter out = new AppendableWriter();
		CharEncoderOutputStream ceos = new CharEncoderOutputStream(encoder, out);
		try{
			ObjectOutputStream oos = new ObjectOutputStream(ceos);
			try{
				oos.writeObject(encoder);
				oos.writeObject(decoder);
			}finally{
				oos.close();
			}
		}finally{
			ceos.close();
		}
		
		StringReader in = new StringReader(out.toString());
		try{
			CharDecoderInputStream cdis = new CharDecoderInputStream(decoder, in);
			try{
				ObjectInputStream ois = new ObjectInputStream(cdis);
				try{
					Assert.assertTrue(encoder.getClass().isInstance(ois.readObject()));
					Assert.assertTrue(decoder.getClass().isInstance(ois.readObject()));
				}finally{
					ois.close();
				}
			}finally{
				cdis.close();
			}
		}finally{
			in.close();
		}
	}

}
