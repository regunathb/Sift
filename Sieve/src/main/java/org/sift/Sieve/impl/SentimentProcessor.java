/*
 * Copyright 2012-2015, the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sift.Sieve.impl;

import org.sift.runtime.model.sentimentdata.Classification;
import org.sift.runtime.model.sentimentdata.ClassificationCollection;
import org.sift.runtime.Fields;
import org.sift.runtime.Tuple;
import org.sift.runtime.spi.ClassificationService;
import org.sift.runtime.spi.OutputCollector;
import org.sift.runtime.spi.Processor;

/**
 * <code> {@link SentimentProcessor} </code> is an implementation of {@link Processor} that filters sentences of 
 * specified sentiment only. 
 * 
 * @author devashishshankar
 * @version 1.0, 19th Feb, 2013
 */
public class SentimentProcessor implements Processor {

	/** The sentiment to be extracted */
	private String sentimentFilter;

	/** The minimum probability measure to classify */
	private double probConfidence;

	/** The number of Classifiers that should agree to classify */
	private int numberConfidence;

	/** Service providing functionality of Classifying texts into sentiments */
	private ClassificationService classificationService;

	/** Label of  element for positive sentiments */
	static public String posLabel = "positive";

	/** Label of  element for negative sentiments */
	static public String negLabel = "negative";

	/** Label of  element for neutral sentiments */
	static public String neutralLabel = "neutral";

	/** 
	 * Interface method Implementation. Filters a category of sentiments.
	 * @see org.sift.runtime.spi.Processor#process()
	 */
	@Override
	public void process(Tuple tuple, OutputCollector collector) {
		//If there are list of values, creates a new Tuple for each
		for(Object value : tuple.getList(Fields.VALUES)) {
			String strValue = (String) value;
			Tuple returnTuple = tuple.clone();
			returnTuple.addField(Fields.SENTIMENT);
			returnTuple.setValue(Fields.SENTIMENT, this.getSentiment((String)tuple.getList(Fields.VALUES).get(0)));
			returnTuple.setValue(Fields.VALUES, null);
			returnTuple.addToList(Fields.VALUES, strValue);
			if(strValue.length()>0)
				collector.emit(returnTuple);
		}
	}

	/** Queries the Sentiment API to get the sentiment of the line
	 *  Warning: Won't work for multiple classifiers.
	 */
	public String getSentiment(String line) {
		ClassificationCollection coll = this.classificationService.getSentiment(line);
		for (Classification classifierData:coll.getClassification()) {
			if(classifierData.getLabel().equals(SentimentProcessor.posLabel)) {
				if(classifierData.getProbability().getPos()>this.probConfidence) {
					return SentimentProcessor.posLabel;
				}
			}
			if(classifierData.getLabel().equals(SentimentProcessor.negLabel)) {
				if(classifierData.getProbability().getNeg()>this.probConfidence) {
					return SentimentProcessor.negLabel;
				}
			}
		}
		return SentimentProcessor.neutralLabel;
	}
	
	/** Getter/Setter Methods */
	public String getSentimentFilter() {
		return sentimentFilter;
	}
	public void setSentimentFilter(String sentimentFilter) {
		this.sentimentFilter = sentimentFilter;
	}

	public double getProbConfidence() {
		return probConfidence;
	}

	public void setProbConfidence(double probConfidence) {
		this.probConfidence = probConfidence;
	}

	public int getNumberConfidence() {
		return numberConfidence;
	}

	public void setNumberConfidence(int numberConfidence) {
		this.numberConfidence = numberConfidence;
	}

	public ClassificationService getClassificationService() {
		return classificationService;
	}

	public void setClassificationService(ClassificationService classificationService) {
		this.classificationService = classificationService;
	}
	/** End Getter/Setter Methods */
}
