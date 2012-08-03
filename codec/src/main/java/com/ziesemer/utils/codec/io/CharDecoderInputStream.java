package com.ziesemer.utils.codec.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CoderResult;

import com.ziesemer.utils.codec.ICharToByteDecoder;
import com.ziesemer.utils.codec.base.CodingStates;

/**
 * <p>
 * 	Reads raw bytes from encoded characters.
 * 	Counter-part to {@link CharEncoderReader}.
 * 	This is a pull-interface; {@link CharDecoderWriter} is the equivalent push-interface.
 * </p>
 * <p>
 * 	Can be adapted to read characters to the consumer (instead of raw bytes)
 * 		by wrapping in a {@link java.io.InputStreamReader}.
 * 	This is only valid if the decoded form of the data is known to only
 * 		contain valid characters.
 * 	Can also be adapted to read bytes from a provider (instead of characters)
 * 		by using a {@link java.io.InputStreamReader} as the {@link Reader}.
 * </p>
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com.">&lt;www.ziesemer.com&gt;</a>
 */
public class CharDecoderInputStream extends InputStream{
	
	protected ICharToByteDecoder decoder;
	protected Reader reader;
	
	protected CharBuffer readBuffer;
	protected ByteBuffer writeBuffer;
	
	protected CodingStates state = CodingStates.RESET;
	
	public CharDecoderInputStream(ICharToByteDecoder decoder, Reader reader){
		this(decoder, reader, IOConstants.DEFAULT_READ_BUFFER_SIZE);
	}
	
	public CharDecoderInputStream(ICharToByteDecoder decoder, Reader reader, int readBufferSize){
		this.decoder = decoder;
		this.reader = reader;
		
		this.readBuffer = CharBuffer.allocate(
			(int)Math.max(readBufferSize, Math.ceil(decoder.getMinInPerOut())));
		this.writeBuffer = ByteBuffer.allocate(
			(int)Math.ceil(readBuffer.capacity() * decoder.getMaxOutPerIn()));
	}
	
	protected boolean fillBuffer() throws IOException{
		if(state == CodingStates.RESET){
			state = CodingStates.CODING;
			writeBuffer.flip();
		}
		
		while(!writeBuffer.hasRemaining()){
			if(state == CodingStates.CODING){
				int read = reader.read(readBuffer.array(), readBuffer.position(), readBuffer.remaining());
				if(read == -1){
					state = CodingStates.END;
				}else{
					readBuffer.position(readBuffer.position() + read);
				}
				readBuffer.flip();
				
				writeBuffer.clear();
				CoderResult cr = decoder.code(readBuffer, writeBuffer, state == CodingStates.END);
				writeBuffer.flip();
				if(!cr.isUnderflow()){
					cr.throwException();
				}
				readBuffer.compact();
			}else if(state == CodingStates.END){
				writeBuffer.clear();
				CoderResult cr = decoder.flush(writeBuffer);
				writeBuffer.flip();
				if(!cr.isUnderflow()){
					cr.throwException();
				}
				state = CodingStates.FLUSHED;
				return writeBuffer.hasRemaining();
			}else{
				return false;
			}
		}
		return true;
	}
	
	@Override
	public int read() throws IOException{
		if(fillBuffer()){
			int result = 0xFF & writeBuffer.get();
			return result;
		}
		return -1;
	}
	
	@Override
	public int read(final byte[] b, final int off, final int len) throws IOException{
		if(fillBuffer()){
			int size = Math.min(len, writeBuffer.remaining());
			writeBuffer.get(b, off, size);
			return size;
		}
		return -1;
	}
	
	@Override
	public void close() throws IOException{
		decoder.reset();
	}

}
