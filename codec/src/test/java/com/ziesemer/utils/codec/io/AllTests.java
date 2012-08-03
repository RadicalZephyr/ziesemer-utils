package com.ziesemer.utils.codec.io;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com">&lt;www.ziesemer.com&gt;</a>
 */
@SuiteClasses({
	CharDecoderInputStreamTest.class,
	CharDecoderWriterTest.class,
	CharEncoderOutputStreamTest.class,
	CharEncoderReaderTest.class
})
@RunWith(value=Suite.class)
public class AllTests{
	// Nothing to do.
}
