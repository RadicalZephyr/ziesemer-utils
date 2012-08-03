package com.ziesemer.utils.codec.charLists;

/**
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com.">&lt;www.ziesemer.com&gt;</a>
 */
public class HexLowerCharList extends ReadOnlyCharList{
	
	public HexLowerCharList(){
		super(build());
	}
	
	public static char[] build(){
		char[] chars = new char[0x10];
		for(int i=0; i < chars.length; i++){
			chars[i] = Character.forDigit(i, chars.length);
		}
		return chars;
	}
	
}
