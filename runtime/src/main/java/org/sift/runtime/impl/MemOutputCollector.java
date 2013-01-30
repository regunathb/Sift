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

/**
 * The <code>MemOutputCollector</code> is a memory based {@link OutputCollector} implementation
 * 
 * @author Regunath B
 * @version 1.0, 28 Jan 2013
 */
public class MemOutputCollector implements OutputCollector {

	/** List of Tuple instances emitted to this OutputCollector */
	private List<Tuple> emittedTuples = new LinkedList<Tuple>();
	
	/**
	 * Interface method implementation. Collects the emitted Tuple in an in-memory collection
	 * @see org.sift.runtime.spi.OutputCollector#emit(org.sift.runtime.Tuple)
	 */
	public void emit(Tuple tuple) {
		this.emittedTuples.add(tuple);
	}

	/**
	 * Interface method implementation. Returns the List of {@link Tuple} instances collected in memory
	 * @see org.sift.runtime.spi.OutputCollector#getEmittedTuples()
	 */
	public List<Tuple> getEmittedTuples() {
		return this.emittedTuples;
	}

	/**
	 * Interface method implementation. Sets the specified Tuple instances as the values collected bu this collector
	 * @see org.sift.runtime.spi.OutputCollector#setTuples(org.sift.runtime.Tuple[])
	 */
	public void setTuples(Tuple... tuples) {
		this.emittedTuples.clear();
		Collections.addAll(this.emittedTuples, tuples);
	}

}
