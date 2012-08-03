package com.ziesemer.utils.codec.impl;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CoderResult;

import com.ziesemer.utils.codec.base.ByteToCharEncoder;
import com.ziesemer.utils.codec.charLists.Base64CharList;

/**
 * <p>
 * 	Encodes to &quot;Base64 encoding&quot;,
 * 		as specified by <a href="http://tools.ietf.org/html/rfc4648#section-4"
 * 		>http://tools.ietf.org/html/rfc4648#section-4</a>.
 * </p>
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com.">&lt;www.ziesemer.com&gt;</a>
 */
public class Base64Encoder
		extends ByteToCharEncoder<Base64Encoder>{
	
	private static final long serialVersionUID = 1L;
	
	public static final String DEFAULT_SECTION_SEPARATOR = "\r\n";
	
	protected static final float CORE_CHARS_PER_BYTE = 4 / 3f;
	
	protected char[] byteToCharList;
	protected String sectionSeparator;
	protected int separatorFreq;
	
	protected transient long sepCharsWritten = 0;
	protected transient int minOutFree;
	
	public Base64Encoder(){
		super(3, CORE_CHARS_PER_BYTE, CORE_CHARS_PER_BYTE);
	}
	
	@Override
	protected void configImpl(Base64Encoder base){
		this.byteToCharList = base.byteToCharList;
		this.sectionSeparator = base.sectionSeparator;
		this.separatorFreq = base.separatorFreq;
	}
	
	@Override
	protected void init(){
		if(byteToCharList == null){
			byteToCharList = Base64CharList.build();
		}
		if(byteToCharList.length < 64){
			throw new IllegalArgumentException("byteToCharList must contain at least 64 characters.");
		}
		if(sectionSeparator == null){
			sectionSeparator = DEFAULT_SECTION_SEPARATOR;
		}
		
		minOutFree = 4;
		
		if(!((separatorFreq >= 0)
				&& (separatorFreq % 4 == 0))){
			throw new IllegalArgumentException("separatorFreq must be a positive multiple of 4.");
		}
		
		if(separatorFreq > 0){
			final int secSepLen = sectionSeparator.length();
			averageOutPerIn = CORE_CHARS_PER_BYTE
				+ (secSepLen / (float)separatorFreq) * CORE_CHARS_PER_BYTE;
			maxOutPerIn = CORE_CHARS_PER_BYTE + secSepLen;
			minOutFree += secSepLen;
		}
	}
	
	public char[] getByteToCharList(){
		return byteToCharList;
	}
	public Base64Encoder setByteToCharList(char[] byteToCharList){
		checkConfigAllowed();
		this.byteToCharList = checkNullArgument(byteToCharList);
		return this;
	}
	
	public String getSectionSeparator(){
		return sectionSeparator;
	}
	public Base64Encoder setSectionSeparator(String sectionSeparator){
		checkConfigAllowed();
		this.sectionSeparator = checkNullArgument(sectionSeparator);
		return this;
	}
	
	public int getSeparatorFreq(){
		return separatorFreq;
	}
	public Base64Encoder setSeparatorFreq(int separatorFreq){
		checkConfigAllowed();
		this.separatorFreq = separatorFreq;
		return this;
	}
	
	@Override
	protected CoderResult codingLoop(final ByteBuffer in, final CharBuffer out, final boolean endOfInput){
		while(in.remaining() >= 3){
			if(out.remaining() < minOutFree){
				return CoderResult.OVERFLOW;
			}
			
			final int x = in.get() & 0xFF;
			final int y = in.get() & 0xFF;
			final int z = in.get() & 0xFF;
			
			addSectionSeparator(out);
			out.put(byteToCharList[x >> 2]);
			out.put(byteToCharList[((x & 3) << 4) | (y >> 4)]);
			out.put(byteToCharList[((y & 0xF) << 2) | (z >> 6)]);
			out.put(byteToCharList[z & 0x3F]);
			sepCharsWritten += 4;
		}
		if(endOfInput && in.hasRemaining()){
			if(out.remaining() < minOutFree){
				return CoderResult.OVERFLOW;
			}
			final int x = in.get() & 0xFF;
			if(in.remaining() == 1){
				final int y = in.get() & 0xFF;
				
				addSectionSeparator(out);
				out.put(byteToCharList[x >> 2]);
				out.put(byteToCharList[((x & 3) << 4) | (y >> 4)]);
				out.put(byteToCharList[(y & 0xF) << 2]);
				out.put('=');
			}else{
				addSectionSeparator(out);
				out.put(byteToCharList[x >> 2]);
				out.put(byteToCharList[(x & 3) << 4]);
				out.put("==");
			}
		}
		
		return CoderResult.UNDERFLOW;
	}
	
	protected void addSectionSeparator(CharBuffer out){
		if((separatorFreq > 0)
				&& (sepCharsWritten % separatorFreq == 0)
				&& sepCharsWritten > 0){
			out.put(sectionSeparator);
		}
	}
	
	@Override
	protected void resetImpl(){
		sepCharsWritten = 0;
	}
	
}
