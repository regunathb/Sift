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

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.sift.runtime.Tuple;
import org.sift.runtime.impl.MemOutputCollector;
import org.sift.runtime.spi.Processor;
import org.springframework.batch.item.ItemProcessor;

/**
 * The <code>ProcessorChainItemProcessor</code> is an implementation of the Spring Batch {@link ItemProcessor} implementation that subjects the
 * passed in data input to a series of Sift runtime {@link Processor} implementations.
 * 
 * @author Regunath B
 * @version 1.0, 28 Jan 2013
 */
public class ProcessorChainItemProcessor<T,S> implements ItemProcessor<T,S> {

	/** The list of Processor instances to pass the Tuple through */
	private List<Processor> processors = new LinkedList<Processor>();

	/**
	 * Interface method implementation. Subjects the specified Tuple through a set of configured {@link Processor} instances, 
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public S process(T paramTuple) throws Exception {
		Tuple tuple = (Tuple) paramTuple;
		//Stack holding list of tuples to be passed on to the next Processor
		Stack<Tuple> returnedTuples = new Stack<Tuple>();
		returnedTuples.push(tuple);
		for (Processor p : this.getProcessors()) {
			MemOutputCollector collector = new MemOutputCollector();
			//Process all the tuples
			for(Tuple returnTuple :returnedTuples) {
				p.process(returnTuple, collector);
			}
			//Clear and add the new tuples
			returnedTuples.clear();
			returnedTuples.addAll(collector.getEmittedTuples());
		}
		List<Tuple> returnTuples = new LinkedList<Tuple>(returnedTuples);
		return (S)returnTuples;
	}

	/** Getter/Setter methods*/
	public List<Processor> getProcessors() {
		return this.processors;
	}
	public void setProcessors(List<Processor> processors) {
		this.processors = processors;
	}

}
