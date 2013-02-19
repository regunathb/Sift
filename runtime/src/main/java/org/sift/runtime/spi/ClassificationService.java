package org.sift.runtime.spi;

import org.sift.batch.model.sentimentdata.ClassificationCollection;

/**
 * <code>ClassificationService</code> is used to access the Sentiment Analysis API to get the sentiment information through 
 * {@link ClassificationCollection}
 * 
 * @author devashishshankar
 * @version 1.0, 15 Feb, 2013
 */
public interface ClassificationService {
	
	public ClassificationCollection getSentiment(String line);
}
