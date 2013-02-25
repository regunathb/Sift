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
package org.sift.sieve.impl;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.sift.runtime.Fields;
import org.sift.runtime.Tuple;
import org.sift.runtime.impl.ValueAggregatingOutputCollector;
import org.sift.runtime.spi.OutputCollector;
import org.sift.runtime.spi.Shuffler;

/**
 * The <code>SentimentBasedValueAggregator</code> is a {@link ValueAggregatingOutputCollector} extension that seperates
 * tuples based on Sentiment, if Sentiment field is found in Tuples and aggregates them accordingly.
 * 
 * @author Devashish Shankar
 * @version 1.0, 23 Feb 2013
 */
public class SentimentBasedValueAggregator extends ValueAggregatingOutputCollector {

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
		if(!tuples[0].contains(Fields.SENTIMENT)) { //If sentiment is not a field, use the super class (ValueAggregatingOutputCollector)
			super.setTuples(tuples);
			return;
		}
		synchronized(this) {
			Collections.addAll(this.getEmittedTuples(), tuples);
			//Create tuple list for each sentiment
			List<Tuple> negativeTuples = new LinkedList<Tuple>();
			List<Tuple> positiveTuples = new LinkedList<Tuple>();
			List<Tuple> neutralTuples = new LinkedList<Tuple>();
			//Add tuples to list according to sentiment
			for(Tuple tuple:this.getEmittedTuples()) {
				if(tuple.getString(Fields.SENTIMENT).equals(SentimentProcessor.posLabel)) {
					positiveTuples.add(tuple);
				}
				else if(tuple.getString(Fields.SENTIMENT).equals(SentimentProcessor.negLabel)) {
					negativeTuples.add(tuple);
				}
				else if(tuple.getString(Fields.SENTIMENT).equals(SentimentProcessor.neutralLabel)) {
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
			this.delegate.setTuples(super.aggregateValue(sortMergedTuples));
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
