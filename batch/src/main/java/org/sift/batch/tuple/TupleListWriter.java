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

import java.util.List;

import org.sift.runtime.Tuple;
import org.sift.runtime.spi.OutputCollector;
import org.springframework.batch.item.ItemWriter;

/**
 * The <code>TupleListWriter</code> is a Spring Batch {@link ItemWriter} implementation that writes the output to the configured
 * {@link OutputCollector} instance
 * 
 * @author devashishshankar
 * @version 1.0, 20 Feb 2013
 */
public class TupleListWriter implements ItemWriter< List<Tuple> > {

	/** The OutputCollector to write the output to*/
	private OutputCollector collector;

	/**
	 * Interface method implementation. Writes the out to the configured {@link OutputCollector}
	 * @see org.springframework.batch.item.ItemWriter#write(java.util.List)
	 */
	@Override
	public void write(List<? extends List<Tuple>> tuplesList) throws Exception {
		List<Tuple> tuples = tuplesList.get(0);
		this.collector.setTuples(tuples.toArray(new Tuple[0]));
	}	


	/** Getter/Setter methods */
	public OutputCollector getCollector() {
		return this.collector;
	}
	public void setCollector(OutputCollector collector) {
		this.collector = collector;
	}
}
