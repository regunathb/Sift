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
import org.springframework.batch.item.ItemProcessor;

/**
 * <code>FilteringProcessor </code> filters certain tuples according to category based filters. It also merges 
 * synonymous tuples.
 * 
 * @author devashishshankar
 * @version 1.0 20th Jan, 2013
 */
public class FilteringProcessor implements ItemProcessor< List<Tuple>, List<Tuple> > {

	/**
	 * Interface method implementation. 
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	@Override
	public List<Tuple> process(List<Tuple> tuples) throws Exception {
		//TODO: This is a stub. Category based filtering and synonym matching to be done here
		return tuples;
	}
}
