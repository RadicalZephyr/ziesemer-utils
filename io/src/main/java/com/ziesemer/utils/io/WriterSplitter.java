package com.ziesemer.utils.io;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>
 * 	Writes to zero or more {@link Writer}s from one {@link Writer}.
 * </p>
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com.">&lt;www.ziesemer.com&gt;</a>
 */
public class WriterSplitter extends Writer{
	
	protected static final Logger LOGGER = Logger.getLogger(WriterSplitter.class.getName());
	
	protected List<Writer> clients = new LinkedList<Writer>();
	protected List<Writer> errorClients = new LinkedList<Writer>();

	public WriterSplitter(Writer... streams){
		this(Arrays.asList(streams));
	}
	
	public WriterSplitter(Collection<? extends Writer> streams){
		this.clients.addAll(streams);
	}
	
	@Override
	public void write(char[] cbuf, int off, int len) throws IOException{
		for(Writer w : clients){
			try{
				w.write(cbuf, off, len);
			}catch(Exception ex){
				LOGGER.log(Level.SEVERE, "Error writing to client: " + ex.toString(), ex);
				errorClients.add(w);
			}
		}
		processErrors();
	}
	
	@Override
	public void flush() throws IOException{
		for(Writer w : clients){
			try{
				w.flush();
			}catch(Exception ex){
				LOGGER.log(Level.SEVERE, "Error flushing client: " + ex.toString(), ex);
				errorClients.add(w);
			}
		}
		processErrors();
	}
	
	@Override
	public void close() throws IOException{
		for(Writer w : clients){
			try{
				w.close();
			}catch(Exception ex){
				LOGGER.log(Level.SEVERE, "Error closing client: " + ex.toString(), ex);
			}
		}
	}
	
	protected void processErrors(){
		for(Writer w : errorClients){
			clients.remove(w);
			try{
				w.close();
			}catch(Exception ex){
				LOGGER.log(Level.SEVERE, "Error closing errored client: " + ex.toString(), ex);
			}
		}
		errorClients.clear();
	}
	
}
