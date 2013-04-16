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

import java.util.ArrayList;
import java.util.List;

import org.sift.runtime.Tuple;
import org.sift.runtime.spi.OutputCollector;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

/**
 * The <code>TupleListReader</code> is an implementation of the Spring Batch {@link ItemReader} that reads Tuple data as {@link Tuple}
 * instances from an {@link OutputCollector}
 * 
 * @author devashishshankar
 * @version 1.0, 20 Feb 2013
 */
public class TupleListReader implements ItemReader< List<Tuple> > {

	/** The Monitor object for serializing thread access*/
	private static final Object MONITOR = new Object();

	/** The OutputCollector to get Tuples and their aggregated values */
	private OutputCollector collector;
	
	/** 
	 * Interface method implementation. @see {ItemReader#read()}
	 */
	public List<Tuple> read() throws Exception, UnexpectedInputException,ParseException {
		synchronized(MONITOR) {
			if (this.collector.getEmittedTuples().size() == 0) {
				return null;
			}
			List<Tuple> tupleList = new ArrayList<Tuple>(this.collector.getEmittedTuples());
			this.collector.getEmittedTuples().clear();
			return tupleList;
		}
	}

	/** Getter/Setter methods */
	public OutputCollector getCollector() {
		return this.collector;
	}
	public void setCollector(OutputCollector collector) {
		this.collector = collector;
	}
}
