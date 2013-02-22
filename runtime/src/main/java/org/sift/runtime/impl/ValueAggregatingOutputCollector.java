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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.sift.runtime.Fields;
import org.sift.runtime.Tuple;
import org.sift.runtime.spi.OutputCollector;
import org.sift.runtime.spi.Shuffler;

/**
 * The <code>ValueAggregatingOutputCollector</code> is a Composite {@link OutputCollector} implementation that delegates all calls to the configured
 * OutputCollector. Additionally, it uses a {@link Shuffler} to sort merge the {@link Tuple} values and aggregates the merged values
 * 
 * @author Regunath B
 * @version 1.0, 28 Jan 2013
 */
public class ValueAggregatingOutputCollector implements OutputCollector {

	/** The OutputCollector delegate */
	private OutputCollector delegate;

	/** The Shuffler instance */
	private Shuffler shuffler;

	/**
	 * Interface method implementation. Throws {@link UnsupportedOperationException} as this composite does not support emitting values from outside this class
	 * @see org.sift.runtime.spi.OutputCollector#emit(org.sift.runtime.Tuple)
	 */
	public void emit(Tuple tuple) {
		throw new UnsupportedOperationException("Cannot emit Tuple instances to this class. Use ValueAggregatingOutputCollector#setTuples instead");
	}

	/**
	 * Interface method implementation. Returns the List of {@link Tuple} instances returned by the namesake method of the delegate
	 * @see org.sift.runtime.spi.OutputCollector#getEmittedTuples()
	 */
	public List<Tuple> getEmittedTuples() {
		return this.delegate.getEmittedTuples();
	}

	/**
	 * Interface method implementation. Uses the configured {@link Shuffler} to sort and merge the {@link Tuple} instances. Also aggregates the tuple values.
	 * NOTE : this method is synchronized on this instance in order to protect from concurrent calls to this method.
	 * @see org.sift.runtime.spi.OutputCollector#setTuples(org.sift.runtime.Tuple[])
	 */
	public void setTuples(Tuple... tuples) {
		synchronized(this) {
			Collections.addAll(this.getEmittedTuples(), tuples);
			//Create tuple list for each sentiment
			List<Tuple> negativeTuples = new LinkedList<Tuple>();
			List<Tuple> positiveTuples = new LinkedList<Tuple>();
			List<Tuple> neutralTuples = new LinkedList<Tuple>();
			//Add tuples to list according to sentiment
			for(Tuple tuple:this.getEmittedTuples()) {
				if(tuple.getList(Fields.SENTIMENT).get(0).equals(SentimentProcessor.posLabel)) {
					positiveTuples.add(tuple);
				}
				else if(tuple.getList(Fields.SENTIMENT).get(0).equals(SentimentProcessor.negLabel)) {
					negativeTuples.add(tuple);
				}
				else if(tuple.getList(Fields.SENTIMENT).get(0).equals(SentimentProcessor.neutralLabel)) {
					neutralTuples.add(tuple);
				}
				else { //Shouldn't happen
					throw new RuntimeException("Tuple with wrong sentiment");
				}
			}
			//Sort each list separately
			List<Tuple> sortMergedTuples = this.shuffler.sort(positiveTuples);
			sortMergedTuples.addAll(this.shuffler.sort(negativeTuples));
			sortMergedTuples.addAll(this.shuffler.sort(neutralTuples));
			//Add all tuples to delegate
			this.delegate.setTuples(aggregateValue(sortMergedTuples));
		}	
	}
	
	/**
	 * Aggregates values of tuples
	 * @param sortMergedTuples Tuples, where list of values indicate weight
	 * @return aggregatedTuples as an array of tuples, where all values have been aggregated
	 */
	private Tuple[] aggregateValue(List<Tuple> sortMergedTuples) {
		Tuple[] aggregatedTuples = new Tuple[sortMergedTuples.size()];
		// now aggregate the values treating them as type integer
		int count = 0;
		for (Tuple sortMergedTuple : sortMergedTuples) {
			Tuple aggregatedValuesTuple = new Tuple(Fields.KEY,Fields.SOURCES,Fields.VALUES,Fields.SENTIMENT);
			aggregatedValuesTuple.setValue(Fields.KEY, sortMergedTuple.getString(Fields.KEY));
			aggregatedValuesTuple.setValue(Fields.SOURCES, sortMergedTuple.getList(Fields.SOURCES));
			Integer aggregateValue = 0;
			for (Object value : sortMergedTuple.getList(Fields.VALUES)) {
				aggregateValue += (Integer)value;
			}
			aggregatedValuesTuple.addToList(Fields.VALUES, aggregateValue);
			aggregatedValuesTuple.addToList(Fields.SENTIMENT, sortMergedTuple.getList(Fields.SENTIMENT).get(0));
			aggregatedTuples[count] = aggregatedValuesTuple;	
			count++;
		}
		return aggregatedTuples;		
	}
	
	/** Getter/Setter methods */
	public OutputCollector getDelegate() {
		return this.delegate;
	}
	public void setDelegate(OutputCollector delegate) {
		this.delegate = delegate;
	}
	public Shuffler getShuffler() {
		return this.shuffler;
	}
	public void setShuffler(Shuffler shuffler) {
		this.shuffler = shuffler;
	}
}
