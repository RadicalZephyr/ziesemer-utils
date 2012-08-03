package com.ziesemer.utils.codec.charLists;

/**
 * <p>
 * 	<a href="http://tools.ietf.org/html/rfc4648#section-5"
 * 		>RFC 4648 section 5: "Base 64 Encoding with URL and Filename Safe Alphabet"</a>
 * </p>
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com.">&lt;www.ziesemer.com&gt;</a>
 */
public class Base64URLCharList extends Base64CharList{
	
	public Base64URLCharList(){
		super(build());
	}
	
	public static char[] build(){
		char[] chars = buildBase();
		byte pos = 0x3E;
		chars[pos++] = '-';
		chars[pos++] = '_';
		return chars;
	}
	
}
