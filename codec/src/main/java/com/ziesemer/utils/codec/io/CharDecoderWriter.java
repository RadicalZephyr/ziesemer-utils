package com.ziesemer.utils.codec.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CoderResult;

import com.ziesemer.utils.codec.ICharToByteDecoder;

/**
 * <p>
 * 	Accepts encoded characters, and writes the raw bytes.
 * 	Counter-part to {@link CharEncoderOutputStream}.
 * 	This is a push-interface; {@link CharDecoderInputStream} is the equivalent pull-interface.
 * </p>
 * <p>
 * 	Can be adapted to accept bytes from a provider (instead of characters)
 * 		by wrapping in a {@link java.io.OutputStreamWriter}.
 * </p>
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com.">&lt;www.ziesemer.com&gt;</a>
 */
public class CharDecoderWriter extends Writer{
	
	protected ICharToByteDecoder decoder;
	protected OutputStream outputStream;
	
	protected CharBuffer readBuffer;
	protected ByteBuffer writeBuffer;
	
	public CharDecoderWriter(ICharToByteDecoder decoder, OutputStream outputStream){
		this(decoder, outputStream, IOConstants.DEFAULT_READ_BUFFER_SIZE);
	}
	
	public CharDecoderWriter(ICharToByteDecoder decoder, OutputStream outputStream, int readBufferSize){
		this.decoder = decoder;
		this.outputStream = outputStream;
		
		this.readBuffer = CharBuffer.allocate(
			(int)Math.max(readBufferSize, Math.ceil(decoder.getMinInPerOut())));
		this.writeBuffer = ByteBuffer.allocate(
			(int)Math.ceil(readBuffer.capacity() * decoder.getMaxOutPerIn()));
	}

	@Override
	public void write(final char[] cbuf, final int off, final int len) throws IOException{
		int remain = len;
		int remainOff = off;
		while(remain > 0){
			int size = Math.min(remain, readBuffer.remaining());
			remain -= size;
			readBuffer.put(cbuf, remainOff, size);
			readBuffer.flip();
			remainOff += size;
			CoderResult cr = null;
			while(cr == null || cr.isOverflow()){
				cr = decoder.code(readBuffer, writeBuffer, false);
				readBuffer.compact();
				writeBuffer(cr);
			}
		}
	}
	
	protected void writeBuffer(final CoderResult cr) throws IOException{
		writeBuffer.flip();
		if(!cr.isUnderflow()){
			cr.throwException();
		}
		outputStream.write(writeBuffer.array(),
			writeBuffer.position(),
			writeBuffer.remaining());
		writeBuffer.clear();
	}
	
	@Override
	public void flush() throws IOException{
		outputStream.flush();
	}
	
	/**
	 * <p>
	 * 	Convenience method that calls {@link #close(boolean)} with <code>true</code>.
	 * </p>
	 */
	@Override
	public void close() throws IOException{
		close(true);
	}
	
	/**
	 * <p>
	 * 	Must be called when there is no more data to decode.
	 * 	This finishes the decoding, writes any finishing bytes required by the decoder,
	 * 		flushes the parent stream, and resets the decoder for possible reuse.
	 * </p>
	 * @param closeParent If <code>true</code>, {@link OutputStream#close()} will be called on the parent.
	 * 	If <code>false</code>, only {@link #flush() will be called}, allowing for
	 * 		additional data to be written to the parent before closing it.
	 */
	public void close(boolean closeParent) throws IOException{
		readBuffer.flip();
		CoderResult cr = decoder.code(readBuffer, writeBuffer, true);
		writeBuffer(cr);
		cr = decoder.flush(writeBuffer);
		writeBuffer(cr);
		
		if(closeParent){
			outputStream.close();
		}else{
			flush();
		}
		decoder.reset();
	}

}
