package com.ziesemer.utils.codec.impl;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CoderResult;
import java.nio.charset.MalformedInputException;

import com.ziesemer.utils.codec.base.CharToByteDecoder;
import com.ziesemer.utils.codec.charLists.Base64CharList;

/**
 * <p>
 * 	Decodes from &quot;Base64 encoding&quot;,
 * 		as specified by <a href="http://tools.ietf.org/html/rfc4648#section-4"
 * 		>http://tools.ietf.org/html/rfc4648#section-4</a>.
 * </p>
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com.">&lt;www.ziesemer.com&gt;</a>
 */
public class Base64Decoder
		extends CharToByteDecoder<Base64Decoder>
		implements ILenientCharsSupport{
	
	private static final long serialVersionUID = 1L;
	
	protected static final int BYTE_PADDING = -2;
	
	protected int[] charToByteList;
	protected LenientChars lenientChars;
	
	public Base64Decoder(){
		super(4, 3 / 4f, 3 / 4f);
	}
	
	@Override
	protected void configImpl(Base64Decoder base){
		charToByteList = base.charToByteList;
		lenientChars = base.lenientChars;
	}
	
	@Override
	protected void init(){
		super.init();
		if(charToByteList == null){
			charToByteList = Base64CharList.reverse(Base64CharList.build());
		}
		if(charToByteList.length < 64){
			throw new IllegalArgumentException("byteToCharList must contain at least 64 values.");
		}
		this.charToByteList['='] = BYTE_PADDING;
		if(lenientChars == null){
			lenientChars = LenientChars.NONE;
		}
	}
	
	public int[] getCharToByteList(){
		return charToByteList;
	}
	public Base64Decoder setCharToByteList(int[] charToByteList){
		checkConfigAllowed();
		this.charToByteList = checkNullArgument(charToByteList);
		return this;
	}
	
	public LenientChars getLenientChars(){
		return lenientChars;
	}
	public Base64Decoder setLenientChars(LenientChars lenientChars){
		checkConfigAllowed();
		this.lenientChars = checkNullArgument(lenientChars);
		return this;
	}
	
	@Override
	protected CoderResult codingLoop(final CharBuffer in, final ByteBuffer out, final boolean endOfInput){
		while(in.remaining() >= 4){
			if(out.remaining() < 3){
				return CoderResult.OVERFLOW;
			}
			
			int inMark = in.position();
			int outMark = out.position();
			try{
				final int a = getNext(in);
				final int b = getNext(in);
				out.put((byte)((a << 2) | (b >> 4)));
				final int c = getNext(in);
				if(c == BYTE_PADDING){
					final int d = getNext(in);
					if(d != BYTE_PADDING){
						in.position(inMark);
						out.position(outMark);
						return CoderResult.malformedForLength(4);
					}
				}else{
					out.put((byte)((b << 4) | (c >> 2)));
					final int d = getNext(in);
					if(d != BYTE_PADDING){
						out.put((byte)((c << 6) | d));
					}
				}
			}catch(BufferUnderflowException bue){
				in.position(inMark);
				out.position(outMark);
				return CoderResult.UNDERFLOW;
			}catch(MalformedInputException mie){
				in.position(inMark);
				out.position(outMark);
				return CoderResult.malformedForLength(4);
			}
		}
		
		processRemainder(in);
		
		return CoderResult.UNDERFLOW;
	}
	
	protected int getNext(final CharBuffer in) throws MalformedInputException{
		int c = in.get();
		int b = charToByteList[c];
		while(b == Base64CharList.UNMAPPED && lenientChars.isLenient(c)){
			c = in.get();
			b = charToByteList[c];
		}
		if(b == Base64CharList.UNMAPPED){
			throw new MalformedInputException(1);
		}
		return b;
	}
	
	// Necessary for trailing "lenient" characters.
	protected void processRemainder(final CharBuffer in){
		for(int i=in.position(); i < in.limit();){
			int c = in.get(i);
			int b = charToByteList[c];
			if(b == Base64CharList.UNMAPPED && lenientChars.isLenient(c)){
				in.position(++i);
			}else{
				break;
			}
		}
	}
	
}
