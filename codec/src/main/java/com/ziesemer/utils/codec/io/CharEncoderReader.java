package com.ziesemer.utils.codec.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CoderResult;

import com.ziesemer.utils.codec.IByteToCharEncoder;
import com.ziesemer.utils.codec.base.CodingStates;

/**
 * <p>
 * 	Reads encoded characters from raw bytes.
 * 	Counter-part to {@link CharDecoderInputStream}.
 * 	This is a pull-interface; {@link CharEncoderOutputStream} is the equivalent push-interface.
 * </p>
 * <p>
 * 	Can be adapted to read characters to the consumer (instead of raw bytes)
 * 		by wrapping in a {@link java.io.InputStreamReader}.
 * </p>
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com.">&lt;www.ziesemer.com&gt;</a>
 */
public class CharEncoderReader extends Reader{
	
	protected IByteToCharEncoder encoder;
	protected InputStream reader;
	
	protected ByteBuffer readBuffer;
	protected CharBuffer writeBuffer;
	
	protected CodingStates state = CodingStates.RESET;
	
	public CharEncoderReader(IByteToCharEncoder encoder, InputStream reader){
		this(encoder, reader, IOConstants.DEFAULT_READ_BUFFER_SIZE);
	}
	
	public CharEncoderReader(IByteToCharEncoder encoder, InputStream reader, int readBufferSize){
		this.encoder = encoder;
		this.reader = reader;
		
		this.readBuffer = ByteBuffer.allocate(
			(int)Math.max(readBufferSize, Math.ceil(encoder.getMinInPerOut())));
		this.writeBuffer = CharBuffer.allocate(
			(int)Math.ceil(readBuffer.capacity() * encoder.getMaxOutPerIn()));
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
				CoderResult cr = encoder.code(readBuffer, writeBuffer, state == CodingStates.END);
				writeBuffer.flip();
				if(!cr.isUnderflow()){
					cr.throwException();
				}
				readBuffer.compact();
			}else if(state == CodingStates.END){
				writeBuffer.clear();
				CoderResult cr = encoder.flush(writeBuffer);
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
	public int read(final char[] cbuf, final int off, final int len) throws IOException{
		if(fillBuffer()){
			int size = Math.min(len, writeBuffer.remaining());
			writeBuffer.get(cbuf, off, size);
			return size;
		}
		return -1;
	}
	
	@Override
	public void close() throws IOException{
		encoder.reset();
	}

}
