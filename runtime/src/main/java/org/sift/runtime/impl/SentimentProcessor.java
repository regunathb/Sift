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
package org.sift.runtime.impl;

import org.sift.batch.model.sentimentdata.Classification;
import org.sift.batch.model.sentimentdata.ClassificationCollection;
import org.sift.runtime.Tuple;
import org.sift.runtime.spi.ClassificationService;
import org.sift.runtime.spi.OutputCollector;
import org.sift.runtime.spi.Processor;

/**
 * <code> {@link SentimentProcessor} </code> is an implementation of {@link Processor} that filters sentences of 
 * specified sentiment only. 
 * 
 * @author devashishshankar
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
	public String posLabel = "pos";
	
	/** Label of  element for negative sentiments */
	public String negLabel = "neg";
	
	@Override
	public void process(Tuple tuple, OutputCollector collector) {
		Tuple returnTuple = new Tuple(tuple.getKey(), tuple.getSource());
		for (Object value : tuple.getValues()) {
			String line = (String) value;
			if(isCorrectSentiment(line)) {
				returnTuple.addValue(line.trim());
			}
		}
		if(returnTuple.getValues().toArray().length>0)
			collector.emit(returnTuple);
	}
	
	/** Queries the Sentiment API to get the sentiment of the line */
	public boolean isCorrectSentiment(String line) {
		ClassificationCollection coll = this.classificationService.getSentiment(line);
		int correctClasscount=0;
		for (Classification classifierData:coll.getClassification()) {
			if(classifierData.getLabel().equals(this.sentimentFilter)) {
				if(this.sentimentFilter.equals(this.posLabel)) {
					if(classifierData.getProbability().getPos()>this.probConfidence) {
//						correctClasscount++;
//						System.out.println("Line queried: "+line);
//						System.out.println("Output:");
//						System.out.println("label: "+classifierData.getLabel());
//						System.out.println("pos: "+classifierData.getProbability().getPos());
//						System.out.println("neg: "+classifierData.getProbability().getNeg());
					}
				}
				if(this.sentimentFilter.equals(this.negLabel)) {
					if(classifierData.getProbability().getNeg()>this.probConfidence) {
//						correctClasscount++;
//						System.out.println("Line queried: "+line);
//						System.out.println("Output:");
//						System.out.println("label: "+classifierData.getLabel());
//						System.out.println("pos: "+classifierData.getProbability().getPos());
//						System.out.println("neg: "+classifierData.getProbability().getNeg());
					}
				}
			}
		}
		if(correctClasscount>=this.numberConfidence)
			return true;
		return false;
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
