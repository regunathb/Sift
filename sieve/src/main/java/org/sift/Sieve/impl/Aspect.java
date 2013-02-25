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
package org.sift.Sieve.impl;

import java.util.LinkedList;
import java.util.List;

/**
 * <code> Aspect </code> is a class holding an Aspect name and its synonyms. Helps in mapping synonyms to a single Aspect.
 * @author devashishshankar
 * @version 1.0, 23 Feb, 2013
 */
public class Aspect {
	
	/** Name or identifier of the aspect */
	private String name;
	/** Synonyms mapped to the Aspect*/
	private List<String> synonyms;
	
	/** Create new empty Aspect */
	public Aspect() {
		this.synonyms = new LinkedList<String>();
	}

	/** Create new  Aspect with name */
	public Aspect(String name) {
		this.name = name;
		this.synonyms = new LinkedList<String>();
		this.synonyms.add(name);
	}
	
	/** Checks if aspect has a word (Substrings are matched) */
	public boolean contains(String word) {
		if(synonyms.contains(word)) {
			return true;
		}
		return false;
	}
	
	/** Getter/Setter methods */
	public List<String> getSynonyms() {
		return this.synonyms;
	}
	public void setSynonyms(List<String> synonyms) {
		this.synonyms = synonyms;
	}
	public void setName(String name) {
		this.name = name;
		this.synonyms = new LinkedList<String>();
		this.synonyms.add(name);
	}
	public String getName() {
		return name;
	}	
	/** End Getter/Setter methods */
}
