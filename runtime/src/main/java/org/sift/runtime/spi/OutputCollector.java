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
package org.sift.runtime.spi;

import java.util.List;

import org.sift.runtime.Tuple;

/**
 * The <code>OutputCollector</code> provides methods for {@link Processor} instances to emit processed data
 * 
 * @author Regunath B
 * @version 1.0, 28 Jan 2013
 */
public interface OutputCollector{

	/** 
	 * Emits the specified Tuple post processing
	 * @param tuple the Tuple containing data elements
	 */
	public void emit(Tuple tuple);
	
	/**
	 * Returns the {@link Tuple} instances emitted to this collector
	 * @return List of Tuple instances collected by this collector
	 */
	public List<Tuple> getEmittedTuples();
	
	/**
	 * Sets the specified Tuple instances as the values collected by this OutputCollector.
	 * WARNING : replaces all values that may have been collected by this OutputCollector
	 * @param tuples the Tuple instances to be set on this OutputCollector
	 */
	public void setTuples(Tuple... tuples);
}
