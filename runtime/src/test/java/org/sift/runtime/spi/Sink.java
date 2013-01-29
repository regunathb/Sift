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

import org.sift.runtime.Tuple;

/**
 * The <code>Sink</code> acts as a collector/aggregator of processed {@link Tuple} data
 * 
 * @author Regunath B
 * @version 1.0, 28 Jan 2013
 */
public interface Sink {

	/**
	 * Processes the specified Tuple. Implementations of this interface consume the output suitably.
	 * @param tuple the Tuple to process
	 */
	public void process(Tuple tuple);
	
	/**
	 * Flushes the Tuple instances processed by this Sink
	 */
	public void flush();
}
