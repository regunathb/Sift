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
			List<Tuple> sortMergedTuples = this.shuffler.sort(this.getEmittedTuples());
			Tuple[] aggregatedTuples = new Tuple[sortMergedTuples.size()];
			// now aggregate the values treating them as type double
			for (int i=0; i < aggregatedTuples.length; i++) {
				Tuple aggregatedValuesTuple = new Tuple(sortMergedTuples.get(i).getKey());
				Double aggregateValue = 0d;
				for (Object value : sortMergedTuples.get(i).getValues()) {
					aggregateValue += Double.parseDouble((String)value);
				}
				aggregatedValuesTuple.addValue(String.valueOf(aggregateValue));
				System.out.println(aggregatedValuesTuple);
				aggregatedTuples[i] = aggregatedValuesTuple;
			}
			this.delegate.setTuples(aggregatedTuples);
		}		
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
