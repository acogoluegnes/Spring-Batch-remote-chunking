/**
 * 
 */
package com.zenika.batch;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

/**
 * @author acogoluegnes
 *
 */
public class TransferRequestFileItemWriter implements
		ItemWriter<TransferRequest> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TransferRequestFileItemWriter.class);
	
	private String outputDirectory;

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemWriter#write(java.util.List)
	 */
	@Override
	public void write(List<? extends TransferRequest> items) throws Exception {
		for(TransferRequest request : items) {
			LOGGER.debug("Writing {} to file",request);
			File file = new File(outputDirectory,"transfer-request-"+request.getId());
			file.createNewFile();
			Files.append(request.toString(),file,Charsets.UTF_8);
		}
	}

	public void setOutputDirectory(String outputDirectory) {
		this.outputDirectory = outputDirectory;
	}
	
}
