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
package org.sift.batch.tuple;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

import org.sift.runtime.Tuple;
import org.sift.runtime.spi.OutputCollector;
import org.springframework.batch.item.ItemWriter;

/**
 * The <code>TupleWriter</code> is a Spring Batch {@link ItemWriter} implementation that writes the output to the configured
 * {@link OutputCollector} instance
 * 
 * @author Regunath B
 * @version 1.0, 28 Jan 2013
 */
public class TupleWriter implements ItemWriter<Tuple> {

	/** The OutputCollector to write the output to*/
	private OutputCollector collector;
	
	/**
	 * Interface method implementation. Writes the out to the cobfigured {@link OutputCollector}
	 * @see org.springframework.batch.item.ItemWriter#write(java.util.List)
	 */
	public void write(List<? extends Tuple> tuples) throws Exception {
		List<Tuple> containedTuples = new ArrayList<Tuple>();
		for (Tuple t : tuples) {
			if (t.getKey().equals(Tuple.UNDEFINED_KEY)) { // it is an collection of Tuple instances
				Collections.addAll(containedTuples, t.getValues().toArray(new Tuple[0]));				
			} else {
				Collections.addAll(containedTuples,t);
			}
		}
		this.collector.setTuples(containedTuples.toArray(new Tuple[0]));
	}
	
	/** Getter/Setter methods */
	public OutputCollector getCollector() {
		return this.collector;
	}
	public void setCollector(OutputCollector collector) {
		this.collector = collector;
	}	

}
