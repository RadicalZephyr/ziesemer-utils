package com.ziesemer.utils.codec.charLists;

import java.util.Arrays;

/**
 * <p>
 * 	<a href="http://tools.ietf.org/html/rfc4648#section-4"
 * 		>RFC 4648 section 4: "Base 64 Encoding"</a>
 * </p>
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com.">&lt;www.ziesemer.com&gt;</a>
 */
public class Base64CharList extends ReadOnlyCharList{
	
	public static final int UNMAPPED = -1;
	
	protected Base64CharList(char[] chars){
		super(chars);
	}
	
	public Base64CharList(){
		super(build());
	}
	
	protected static char[] buildBase(){
		char[] chars = new char[0x40];
		byte pos = 0;
		for(char c = 'A'; c <= 'Z'; c++){
			chars[pos++] = c;
		}
		for(char c = 'a'; c <= 'z'; c++){
			chars[pos++] = c;
		}
		for(char c = '0'; c <= '9'; c++){
			chars[pos++] = c;
		}
		return chars;
	}
	
	public static char[] build(){
		char[] chars = buildBase();
		byte pos = 0x3E;
		chars[pos++] = '+';
		chars[pos++] = '/';
		return chars;
	}
	
	public static int[] reverse(char[] chars){
		int[] result = new int[0x80];
		Arrays.fill(result, UNMAPPED);
		for(int i=0; i < chars.length; i++){
			result[chars[i]] = i;
		}
		return result;
	}
	
}
