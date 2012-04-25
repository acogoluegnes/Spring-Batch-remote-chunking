/**
 * 
 */
package com.zenika.batch;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

/**
 * @author acogoluegnes
 *
 */
public class NoOpItemReader<T> implements ItemReader<T> {

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemReader#read()
	 */
	@Override
	public T read() throws Exception, UnexpectedInputException, ParseException,
			NonTransientResourceException {
		throw new UnsupportedOperationException("null method shouldn't be called!");
	}

}
