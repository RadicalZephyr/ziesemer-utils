package com.ziesemer.utils.codec.test;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CoderResult;

import com.ziesemer.utils.codec.base.CharToByteDecoder;

/**
 * <p>This class is essentially for testing the tests, and is NOT designed for actual use,
 * 		especially considering single-byte character encodings are obsolete.
 * 	Please file a feature request if there is a realized need for an improved
 * 		version of this class.</p>
 *
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com.">&lt;www.ziesemer.com&gt;</a>
 */
public class SingleByteDecoder
		extends CharToByteDecoder<SingleByteDecoder>{
	
	private static final long serialVersionUID = 1L;
	
	public SingleByteDecoder(){
		super(1, 1, 1);
	}
	
	@Override
	protected CoderResult codingLoop(CharBuffer in, ByteBuffer out, boolean endOfInput){
		while(in.hasRemaining()){
			if(out.remaining() < maxOutPerIn){
				return CoderResult.OVERFLOW;
			}
			
			if(in.hasRemaining()){
				out.put((byte)in.get());
			}else{
				return CoderResult.UNDERFLOW;
			}
		}
		return CoderResult.UNDERFLOW;
	}

}
