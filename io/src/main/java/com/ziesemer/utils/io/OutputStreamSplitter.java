package com.ziesemer.utils.io;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>
 * 	Writes to zero or more {@link OutputStream}s from one {@link OutputStream}.
 * </p>
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com.">&lt;www.ziesemer.com&gt;</a>
 */
public class OutputStreamSplitter extends OutputStream{
	
	protected static final Logger LOGGER = Logger.getLogger(OutputStreamSplitter.class.getName());
	
	protected List<OutputStream> clients = new LinkedList<OutputStream>();
	protected List<OutputStream> errorClients = new LinkedList<OutputStream>();

	public OutputStreamSplitter(OutputStream... streams){
		this(Arrays.asList(streams));
	}
	
	public OutputStreamSplitter(Collection<? extends OutputStream> streams){
		this.clients.addAll(streams);
	}
	
	@Override
	public void write(int b) throws IOException{
		for(OutputStream os : clients){
			try{
				os.write(b);
			}catch(Exception ex){
				LOGGER.log(Level.SEVERE, "Error writing to client: " + ex.toString(), ex);
				errorClients.add(os);
			}
		}
		processErrors();
	}
	
	@Override
	public void write(byte[] b, int off, int len) throws IOException{
		for(OutputStream os : clients){
			try{
				os.write(b, off, len);
			}catch(Exception ex){
				LOGGER.log(Level.SEVERE, "Error writing to client: " + ex.toString(), ex);
				errorClients.add(os);
			}
		}
		processErrors();
	}
	
	@Override
	public void flush() throws IOException{
		for(OutputStream os : clients){
			try{
				os.flush();
			}catch(Exception ex){
				LOGGER.log(Level.SEVERE, "Error flushing client: " + ex.toString(), ex);
				errorClients.add(os);
			}
		}
		processErrors();
	}
	
	@Override
	public void close() throws IOException{
		for(OutputStream os : clients){
			try{
				os.close();
			}catch(Exception ex){
				LOGGER.log(Level.SEVERE, "Error closing client: " + ex.toString(), ex);
			}
		}
	}
	
	protected void processErrors(){
		for(OutputStream os : errorClients){
			clients.remove(os);
			try{
				os.close();
			}catch(Exception ex){
				LOGGER.log(Level.SEVERE, "Error closing errored client: " + ex.toString(), ex);
			}
		}
		errorClients.clear();
	}

}
