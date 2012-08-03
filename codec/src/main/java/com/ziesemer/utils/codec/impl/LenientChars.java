package com.ziesemer.utils.codec.impl;

import java.io.Serializable;
import java.util.BitSet;

/**
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com.">&lt;www.ziesemer.com&gt;</a>
 */
public abstract class LenientChars implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * A {@link LenientChars} instance meant to be reused that always returns <code>true</code>.
	 */
	public static final LenientChars ALL = new AllChars(true);
	/**
	 * A {@link LenientChars} instance meant to be reused that always returns <code>false</code>.
	 */
	public static final LenientChars NONE = new AllChars(false);
	
	/**
	 * <p>Returns a {@link LenientChars} instance configured using the passed-in {@link BitSet}.</p>
	 * <p>Only a reference to the passed-in {@link BitSet} is maintained;
	 * 	i.e., a shallow-copy vs. a deep-copy is performed.</p>
	 */
	public static LenientChars partial(BitSet lenientChars){
		return new PartialChars(lenientChars);
	}
	
	public abstract boolean isLenient(int c);
	
	protected static class AllChars extends LenientChars{
		private static final long serialVersionUID = 1L;
		protected final boolean lenientAll;
		
		public AllChars(boolean lenientAll){
			this.lenientAll = lenientAll;
		}
		
		@Override
		public boolean isLenient(int c){
			return lenientAll;
		}
	}
	
	protected static class PartialChars extends LenientChars{
		private static final long serialVersionUID = 1L;
		protected final BitSet lenientChars;
		
		public PartialChars(BitSet lenientChars){
			if(lenientChars == null){
				throw new IllegalArgumentException("BitSet must not be null.");
			}
			this.lenientChars = lenientChars;
		}
		
		@Override
		public boolean isLenient(int c){
			return lenientChars.size() > c && lenientChars.get(c);
		}
	}
	
}
