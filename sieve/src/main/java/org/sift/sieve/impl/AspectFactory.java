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
package org.sift.sieve.impl;

import java.net.URI;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * <code> AspectFactory </code> holds a list of {@link Aspect} mapped to a groupID. This map can be loaded
 * from a URI and can be persisted. This class provides methods to check whether a particular word is an aspect,
 * based on the GroupID, and also maps all the synonyms to a single Aspect name.
 * 
 * @author devashishshankar
 * @version 1.0, 23rd Feb, 2013
 */
public abstract class AspectFactory {
		
	/** Map holding the aspects by groupID */
	public Map<String,List<Aspect>> groupIDtoAspect;
	
	/** New aspect Factory */
	public AspectFactory() {
		this.groupIDtoAspect = new HashMap<String, List<Aspect>>();
	}
	
	/** Generates AspectFactory from the given URI */
	public AspectFactory(URI resource) {
		this.generate(resource);
	}
	
	/** 
	 * Gets the GroupIDs in the current instance
	 * @return List of string
	 */
	public String[] getGroupIds() {
		return this.groupIDtoAspect.keySet().toArray(new String[0]);
	}
	
	/**
	 * Adds a new GroupID
	 */
	public void addgroupID(String groupID) {
		if(!this.groupIDtoAspect.containsKey(groupID))
			this.groupIDtoAspect.put(groupID, new LinkedList<Aspect>());
	}
	
	/**
	 * Adds an aspect to groupID
	 */
	public void addAspectTogroupID(String groupID,Aspect aspect) {
		if(!this.groupIDtoAspect.get(groupID).contains(aspect))
			this.groupIDtoAspect.get(groupID).add(aspect);
	}
	
	/**
	 * Gets all the aspects in a GroupID
	 */
	public List<Aspect> getAspects(String groupID) {
		return new LinkedList<Aspect>(this.groupIDtoAspect.get(groupID));
	}
	
	/**
	 * Get aspect which maps to the word, based on the groupID
	 * @param word Word to be matched
	 * @param groupID GroupID in which matching should happen
	 * @return null if no aspect found in the group
	 */
	public String getAspect(String word,String groupID) {
		for(Aspect aspect:this.getAspects(groupID)) {
			if(aspect.contains(word))
				return aspect.getName();
		}
		return null;
	}
	
	/**
	 * Persist the current GroupID and Aspect information to the given URI
	 * @param resource URI to which information has to be persisted
	 */
	public abstract void persist(URI resource);

	/**
	 * Generate the GroupID and Aspect information from the given URI
	 * @param resource URI from which information has to be generated
	 */
	public abstract void generate(URI resource);
	
}
