/**
 * 
 */
package com.zenika.batch;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

/**
 * @author acogoluegnes
 *
 */
public class NoOpItemWriter<T> implements ItemWriter<T> {

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemWriter#write(java.util.List)
	 */
	@Override
	public void write(List<? extends T> items) throws Exception {
		throw new UnsupportedOperationException("write method shouldn't be called!");
	}

}
