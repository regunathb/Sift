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

import org.sift.runtime.Fields;
import org.sift.runtime.Tuple;
import org.springframework.batch.item.ItemProcessor;

import com.flipkart.sift.sieve.spi.AspectFactory;

/**
 * <code>FilteringProcessor </code> filters certain tuples according to category based filters. It also merges 
 * synonymous tuples.
 * 
 * @author devashishshankar
 * @version 1.0 20th Jan, 2013
 */
public class FilteringProcessor implements ItemProcessor< List<Tuple>, List<Tuple> > {

	/** AspectFactory which holds all the aspects to be displayed by groupID  */
	private AspectFactory aspectFactory;
	
	/**
	 * Interface method implementation. Removes aspects not found in the GroupID. Maps synonyms
	 * to the correct aspect. (This information is generated from AspectFactory. If aspectFactory is 
	 * not initialized, this processor does nothing)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	@Override
	public List<Tuple> process(List<Tuple> tuples) throws Exception {
		List<Tuple> returnTuples = new LinkedList<Tuple>();
		//If aspectFactory is not set, leave Tuples as it is
		if(aspectFactory==null)
			return tuples;
		for (Tuple t:tuples) {
			String groupID = "";
			String aspect = this.aspectFactory.getAspect((String)t.getList(Fields.VALUES).get(0), groupID);
			if(aspect==null) {
				continue;
			}
			else {
				Tuple returnTuple = t.clone();
				String[] tupleValues = this.getSubjectAndTag(t.getString(Fields.KEY));
				returnTuple.setValue(Fields.KEY, tupleValues[0]+Tuple.KEY_SEP_CHAR+aspect);
				returnTuples.add(returnTuple);
			}
		}
		return returnTuples;
	}
	
	/**
	 * Helper method to get subject and tag
	 */
	private String[] getSubjectAndTag(String key) {
		String[] values = new String[2];
		values[0] = key.substring(0, key.indexOf(Tuple.KEY_SEP_CHAR));
		values[1] = key.substring(key.indexOf(Tuple.KEY_SEP_CHAR) + 1, key.length());
		return values;
	}

	/** Getter/Setter methods */
	public AspectFactory getAspectFactory() {
		return aspectFactory;
	}

	public void setAspectFactory(AspectFactory aspectFactory) {
		this.aspectFactory = aspectFactory;
	}
	/** End Getter/Setter methods */
}
