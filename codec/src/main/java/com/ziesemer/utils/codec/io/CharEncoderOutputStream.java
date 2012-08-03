package com.ziesemer.utils.codec.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CoderResult;

import com.ziesemer.utils.codec.IByteToCharEncoder;

/**
 * <p>
 * 	Accepts raw bytes, and writes the encoded characters.
 * 	Counter-part to {@link CharDecoderWriter}.
 * 	This is a push-interface; {@link CharEncoderReader} is the equivalent pull-interface.
 * </p>
 * <p>Can be adapted to write bytes to the consumer (instead of characters)
 * 		by using a {@link java.io.OutputStreamWriter} as the {@link Writer}.
 * 	Can also be adapted to accept characters from the provider (instead of raw bytes)
 * 		by wrapping in a {@link java.io.OutputStreamWriter}.
 * </p>
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com.">&lt;www.ziesemer.com&gt;</a>
 */
public class CharEncoderOutputStream extends OutputStream{
	
	protected IByteToCharEncoder encoder;
	protected Writer writer;
	
	protected ByteBuffer readBuffer;
	protected CharBuffer writeBuffer;
	
	protected byte[] singleByte = new byte[1];
	
	public CharEncoderOutputStream(IByteToCharEncoder encoder, Writer writer){
		this(encoder, writer, IOConstants.DEFAULT_READ_BUFFER_SIZE);
	}
	
	public CharEncoderOutputStream(IByteToCharEncoder encoder, Writer writer, int readBufferSize){
		this.encoder = encoder;
		this.writer = writer;
		
		this.readBuffer = ByteBuffer.allocate(
			(int)Math.max(readBufferSize, Math.ceil(encoder.getMinInPerOut())));
		this.writeBuffer = CharBuffer.allocate(
			(int)Math.ceil(readBuffer.capacity() * encoder.getMaxOutPerIn()));
	}
	
	@Override
	public void write(int b) throws IOException{
		singleByte[0] = (byte)b;
		write(singleByte);
	}
	
	@Override
	public void write(final byte[] b, final int off, final int len) throws IOException{
		int remain = len;
		int remainOff = off;
		while(remain > 0){
			int size = Math.min(remain, readBuffer.remaining());
			remain -= size;
			readBuffer.put(b, remainOff, size);
			readBuffer.flip();
			remainOff += size;
			CoderResult cr = null;
			while(cr == null || cr.isOverflow()){
				cr = encoder.code(readBuffer, writeBuffer, false);
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
		writer.write(writeBuffer.array(),
			writeBuffer.position(),
			writeBuffer.remaining());
		writeBuffer.clear();
	}
	
	@Override
	public void flush() throws IOException{
		writer.flush();
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
	 * 	Must be called when there is no more data to encode.
	 * 	This finishes the encoding, writes any finishing characters required by the encoder,
	 * 		flushes the parent writer, and resets the encoder for possible reuse.
	 * </p>
	 * @param closeParent If <code>true</code>, {@link Writer#close()} will be called on the parent.
	 * 	If <code>false</code>, only {@link #flush() will be called}, allowing for
	 * 		additional data to be written to the parent before closing it.
	 */
	public void close(boolean closeParent) throws IOException{
		readBuffer.flip();
		CoderResult cr = encoder.code(readBuffer, writeBuffer, true);
		writeBuffer(cr);
		cr = encoder.flush(writeBuffer);
		writeBuffer(cr);
		
		if(closeParent){
			writer.close();
		}else{
			flush();
		}
		encoder.reset();
	}

}
