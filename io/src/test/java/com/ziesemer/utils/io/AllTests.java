package com.ziesemer.utils.io;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com">&lt;www.ziesemer.com&gt;</a>
 */
@SuiteClasses({
	AppendableWriterTest.class,
	CharSequenceReaderTest.class,
	NoFlushBufferedOutputStreamTest.class,
	NoFlushBufferedWriterTest.class,
	OutputStreamSplitterTest.class,
	RandomAccessFileInputStreamTest.class,
	RandomAccessFileOutputStreamTest.class,
	WriterSplitterTest.class
})
@RunWith(value=Suite.class)
public class AllTests{
	// Nothing to do.
}
