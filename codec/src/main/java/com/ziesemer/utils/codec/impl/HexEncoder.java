package com.ziesemer.utils.codec.impl;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CoderResult;

import com.ziesemer.utils.codec.base.ByteToCharEncoder;
import com.ziesemer.utils.codec.charLists.HexUpperCharList;

/**
 * <p>
 * 	Encodes to &quot;hexadecimal encoding&quot;,
 * 		where each byte is represented by 2 characters (0-padded if necessary).
 * </p>
 * <p>
 * 	Can be configured to encode using a specific character list.
 * 	This is designed only for choosing between the {@link HexUpperCharList} (default) and
 * 		{@link com.ziesemer.utils.codec.charLists.HexLowerCharList}, as the {@link HexDecoder}
 * 		currently does not offer a reciprocal configuration option and can only decode from the
 * 		standard format used by these lists.
 * </p>
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com.">&lt;www.ziesemer.com&gt;</a>
 */
public class HexEncoder
		extends ByteToCharEncoder<HexEncoder>{
	
	private static final long serialVersionUID = 1L;
	
	public static final String DEFAULT_SECTION_SEPARATOR = " ";
	
	protected char[] byteToCharList;
	protected String sectionSeparator;
	protected int separatorFreq;
	
	protected transient long sepBytesWritten = 0;
	
	public HexEncoder(){
		super(1, 2, 2);
	}
	
	@Override
	protected void configImpl(HexEncoder base){
		this.byteToCharList = base.byteToCharList;
		this.sectionSeparator = base.sectionSeparator;
		this.separatorFreq = base.separatorFreq;
	}
	
	@Override
	protected void init(){
		super.init();
		if(byteToCharList == null){
			byteToCharList = HexUpperCharList.build();
		}
		if(byteToCharList.length < 16){
			throw new IllegalArgumentException("byteToCharList must contain at least 16 characters.");
		}
		if(sectionSeparator == null){
			sectionSeparator = DEFAULT_SECTION_SEPARATOR;
		}
		
		if(separatorFreq > 0){
			this.averageOutPerIn = 2 + (sectionSeparator.length() / (float)separatorFreq);
			this.maxOutPerIn = 2 + sectionSeparator.length();
		}
	}
	
	public char[] getByteToCharList(){
		return byteToCharList;
	}
	public HexEncoder setByteToCharList(char[] byteToCharList){
		checkConfigAllowed();
		this.byteToCharList = checkNullArgument(byteToCharList);
		return this;
	}
	
	public String getSectionSeparator(){
		return sectionSeparator;
	}
	public HexEncoder setSectionSeparator(String sectionSeparator){
		checkConfigAllowed();
		this.sectionSeparator = checkNullArgument(sectionSeparator);
		return this;
	}
	
	public int getSeparatorFreq(){
		return separatorFreq;
	}
	public HexEncoder setSeparatorFreq(int separatorFreq){
		checkConfigAllowed();
		this.separatorFreq = separatorFreq;
		return this;
	}
	
	@Override
	protected CoderResult codingLoop(final ByteBuffer in, final CharBuffer out, final boolean endOfInput){
		while(in.hasRemaining()){
			if(out.remaining() < maxOutPerIn){
				return CoderResult.OVERFLOW;
			}
			
			if((separatorFreq > 0)
					&& (sepBytesWritten % separatorFreq == 0)
					&& sepBytesWritten > 0){
				out.put(sectionSeparator);
			}
			
			final int b = in.get();
			out.put(byteToCharList[(b >> 4) & 0xF]);
			out.put(byteToCharList[b & 0xF]);
			sepBytesWritten++;
		}
		return CoderResult.UNDERFLOW;
	}
	
	@Override
	protected void resetImpl(){
		sepBytesWritten = 0;
	}
	
}
