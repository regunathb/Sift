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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.sift.runtime.Fields;
import org.sift.runtime.Tuple;
import org.sift.runtime.spi.Shuffler;

/**
 * The <code>MemSortMergeShuffler</code> is a memory based {@link Shuffler} implementation that first sorts the Tuple instances by their keys
 * and then merges identical Tuple values into a Tuple, identified by the common key value
 * 
 * @author Regunath B
 * @version 1.0, 28 Jan 2013
 */
public class MemSortMergeShuffler implements Shuffler, Comparator<Tuple> {

	/**
	 * Interface method implementation. Sorts the specified Tuple list in memory
	 * @see org.sift.runtime.spi.Shuffler#sort(java.util.List)
	 */
	public List<Tuple> sort(List<Tuple> tuples) {
		Collections.sort(tuples, this);		
		List<Tuple> sortMergeTuples = new LinkedList<Tuple>();
		Tuple mergedTuple = null;
		for (Tuple tuple : tuples) {
			if(mergedTuple == null ) {
				mergedTuple = tuple;
				mergedTuple = tuple.clone();
				//Resetting values and sources so that they don't have references to original Tuple
				mergedTuple.setValue(Fields.SOURCES, null);
				mergedTuple.setValue(Fields.SOURCES, tuple.getList(Fields.SOURCES));
				mergedTuple.setValue(Fields.VALUES, null);
				mergedTuple.addToList(Fields.VALUES, tuple.getList(Fields.VALUES).get(0));
				continue;				
			}
			if (!mergedTuple.getString(Fields.KEY).equals(tuple.getString(Fields.KEY))) {
				//Copying the list before adding to mergedTuple
				sortMergeTuples.add(mergedTuple);
				mergedTuple = tuple;
				// now recreate the new mergedTuple with the current one
				mergedTuple = tuple.clone();
				//Resetting values and sources so that they don't have references to original Tuple
				mergedTuple.setValue(Fields.SOURCES, null);
				mergedTuple.setValue(Fields.SOURCES, tuple.getList(Fields.SOURCES));
				mergedTuple.setValue(Fields.VALUES, null);
				mergedTuple.addToList(Fields.VALUES, tuple.getList(Fields.VALUES).get(0));
			} else if (mergedTuple.getString(Fields.KEY).equals(tuple.getString(Fields.KEY))) { // double check
				//Add the source URIs				
				for (Object uri:tuple.getList(Fields.SOURCES)) {
					if(mergedTuple.getList(Fields.SOURCES)==null || !mergedTuple.getList(Fields.SOURCES).contains(uri))
						mergedTuple.addToList(Fields.SOURCES, uri);
				}
				List<Object> oldTupleValues = new ArrayList<Object>();
				oldTupleValues.addAll( tuple.getList(Fields.VALUES));
				Collections.addAll(mergedTuple.getList(Fields.VALUES), oldTupleValues.toArray(new Object[0]));
			} else { // this should never happen
				throw new RuntimeException("Unable to sort and merge tuple data!");
			}
		}
		return sortMergeTuples;
	}

	/**
	 * Interface method implementation. Compares the keys of the specified Tuple instances using their natural ordering of characters
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Tuple tuple1, Tuple tuple2) {
		return tuple1.getString(Fields.KEY).compareTo(tuple2.getString(Fields.KEY));
	}

}
