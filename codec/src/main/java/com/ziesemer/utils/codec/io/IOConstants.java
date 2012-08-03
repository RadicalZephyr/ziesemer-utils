package com.ziesemer.utils.codec.io;

import java.nio.charset.Charset;

/**
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com.">&lt;www.ziesemer.com&gt;</a>
 */
public class IOConstants{
	public static final int DEFAULT_READ_BUFFER_SIZE = 1 << 10;
	
	/**
	 * Guaranteed to be available per {@link Charset}.
	 */
	public static final Charset UTF_8 = Charset.forName("UTF-8");
}
