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
package org.sift.tagcloud;

import java.util.LinkedList;
import java.util.List;

/**
 * The <code>TagCloud</code> is an aggregate of {@link Tag} instances for a given subject.
 * 
 * @author Regunath B
 * @version 1.0, 24 Jan 2013
 */
public class TagCloud {

	/** The subject for this tag cloud */
	private String subject;
	
	/** The list of tags in this tag cloud for the given subject*/
	private List<Tag> tagsList  = new LinkedList<Tag>();
	
	/**
	 * Constructor for this class.
	 * @param subject the String identifier for the subject of this tag cloud
	 */
	public TagCloud(String subject) {
		this.subject = subject;
	}

	/**
	 * Adds the specified Tag to the list of tags for the subject
	 * @param tag the Tag to be added
	 */
	public void addTag(Tag tag) {
		if (!this.tagsList.contains(tag)) {
			this.tagsList.add(tag);
		}
	}	
	
	/**
	 * Removes the specified tag from this tag cloud
	 * @param tag the Tag to be removed
	 * @return true if remove was successful, false otherwise - even if the tag does not exist
	 */
	public boolean remove(Tag tag) {
		return this.tagsList.remove(tag);
	}
	
	/** Getter/Setter methods*/
	public List<Tag> getTagsList() {
		return this.tagsList;
	}
	public void setTagsList(List<Tag> tagsList) {
		this.tagsList = tagsList;
	}
	public String getSubject() {
		return this.subject;
	}
	/** End Getter/Setter methods*/
	
}
