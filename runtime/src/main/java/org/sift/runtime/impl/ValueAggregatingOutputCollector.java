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
	
	/** The Monitor object for serializing thread access*/
	private static final Object MONITOR = new Object();
	
	/** The OutputCollector delegate */
	protected OutputCollector delegate;
	
	/** The Shuffler instance */
	protected Shuffler shuffler;

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
		synchronized(MONITOR) {
			Collections.addAll(this.getEmittedTuples(), tuples);
			List<Tuple> sortMergedTuples = this.shuffler.sort(this.getEmittedTuples());
			Tuple[] aggregatedTuples = this.aggregateValue(sortMergedTuples);
			this.delegate.setTuples(aggregatedTuples);
		}	
	}

	/**
	 * Aggregates values of tuples.
	 * @param sortMergedTuples Tuples, where list of values indicate weight
	 * @return aggregatedTuples as an array of tuples, where all values have been aggregated
	 */
	protected Tuple[] aggregateValue(List<Tuple> sortMergedTuples) {
		Tuple[] aggregatedTuples = new Tuple[sortMergedTuples.size()];
		// now aggregate the values treating them as type integer
		int count = 0;
		for (Tuple sortMergedTuple : sortMergedTuples) {
			Tuple aggregatedValuesTuple = sortMergedTuple.clone();
			aggregatedValuesTuple.setValue(Fields.VALUES, null);
			Integer aggregateValue = 0;
			for (Object value : sortMergedTuple.getList(Fields.VALUES)) {
				aggregateValue += (Integer)value;
			}
			aggregatedValuesTuple.addToList(Fields.VALUES, aggregateValue);
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