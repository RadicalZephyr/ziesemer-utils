package com.ziesemer.utils.codec.impl;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CoderResult;

import com.ziesemer.utils.codec.base.CharToByteDecoder;

/**
 * <p>
 * 	Decodes from &quot;hexadecimal encoding&quot;,
 * 		where each byte is represented by 2 characters (0-padded if necessary).
 * </p>
 * <p>
 * 	By default, will throw a {@link java.nio.charset.MalformedInputException} if any non-significant
 * 		characters are encountered.
 * 	This can be customized by configuring non-significant characters to ignore by using
 * 		{@link #setLenientChars(LenientChars)}.
 * </p>
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com.">&lt;www.ziesemer.com&gt;</a>
 */
public class HexDecoder
		extends CharToByteDecoder<HexDecoder>
		implements ILenientCharsSupport{
	
	private static final long serialVersionUID = 1L;
	
	protected LenientChars lenientChars;
	
	public HexDecoder(){
		super(2, 0.5f, 0.5f);
	}
	
	@Override
	protected void configImpl(HexDecoder base){
		lenientChars = base.lenientChars;
	}
	
	@Override
	protected void init(){
		super.init();
		if(lenientChars == null){
			lenientChars = LenientChars.NONE;
		}
	}
	
	public LenientChars getLenientChars(){
		return lenientChars;
	}
	public HexDecoder setLenientChars(LenientChars lenientChars){
		checkConfigAllowed();
		this.lenientChars = checkNullArgument(lenientChars);
		return this;
	}
	
	@Override
	protected CoderResult codingLoop(final CharBuffer in, final ByteBuffer out, final boolean endOfInput){
		while(in.hasRemaining()){
			if(out.remaining() < maxOutPerIn){
				return CoderResult.OVERFLOW;
			}
			
			int mark = in.position();
			int x = -1, y = -1;
			if((x = getPartialByte(in)) < 0
					|| (y = getPartialByte(in)) < 0){
				if(x == -2 || y == -2){
					int malLength = in.position() - mark;
					in.position(mark);
					return CoderResult.malformedForLength(malLength);
				}
				in.position(mark);
				return CoderResult.UNDERFLOW;
			}
			out.put((byte)((x << 4) + y));
		}
		return CoderResult.UNDERFLOW;
	}
	
	protected int getPartialByte(final CharBuffer in){
		int result = -1;
		while(in.hasRemaining() && result == -1){
			char c = in.get();
			result = Character.digit(c, 0x10);
			if(result == -1 && !lenientChars.isLenient(c)){
				return -2;
			}
		}
		return result;
	}

}
