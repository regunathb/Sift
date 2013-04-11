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
package org.sift.batch.tag;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.sift.runtime.Fields;
import org.sift.runtime.Tuple;
import org.sift.tagcloud.ui.DisplayTag;
import org.sift.tagcloud.ui.DisplayTagCloud;


/**
 * <code>TagCloudFactory </code> generates TagClouds based on the tuples, i.e. if positive and negative sentiments
 * exist in the tuple, different tag clouds are generated for each sentiment
 * 
 * @author devashishshankar
 * @version 1.0, 25th Feb, 2013 
 */
public class TagCloudFactory { 

	/** Label of  element for positive sentiments */
	static public final String POSITIVE_LABEL = "positive";

	/** Label of  element for negative sentiments */
	static public final String NEGATIVE_LABEL = "negative";

	/** Label of  element for neutral sentiments */
	static public final String NEUTRAL_LABEL = "neutral";

	/** Index of tagclouds in the tag cloud list */
	final private int POS_INDEX = 0;
	final private int NEG_INDEX = 1;
	final private int NEUTRAL_INDEX = 2;

	/** The list of tagClouds */
	private List<DisplayTagCloud<DisplayTag>> tagCloudList;

	/** Constructor. Initializes the factory based on a single tuple. Warning: The tag cloud
	 * list is generated based on this tuple. The rest of the Tuples should be similar */
	public TagCloudFactory(Tuple tuple) {
		this.tagCloudList = new ArrayList<DisplayTagCloud<DisplayTag>>();
		String key = tuple.getString(Fields.KEY);
		String subject = key.substring(0, key.indexOf(Tuple.KEY_SEP_CHAR));
		if(tuple.contains(Fields.SENTIMENT)) {
			DisplayTagCloud<DisplayTag> tagCloudPositive = new DisplayTagCloud<DisplayTag>(subject+"_"+POSITIVE_LABEL);		
			DisplayTagCloud<DisplayTag> tagCloudNegative = new DisplayTagCloud<DisplayTag>(subject+"_"+NEGATIVE_LABEL);		
			DisplayTagCloud<DisplayTag> tagCloudNeutral = new DisplayTagCloud<DisplayTag>(subject+"_"+NEUTRAL_LABEL);
			this.tagCloudList.add(tagCloudPositive);
			this.tagCloudList.add(tagCloudNegative);
			this.tagCloudList.add(tagCloudNeutral);
		}
		else {
			this.tagCloudList.add(new DisplayTagCloud<DisplayTag>(subject));	
		}
	}

	/**
	 * Adds a tuple to the corresponding Tagcloud
	 * @param tuple Tuple to be added
	 * @param displayTag displayTag corresponding to the tuple
	 */
	public void add(Tuple tuple, DisplayTag displayTag) {
		try {
			if(this.tagCloudList.size()==1) {
				this.tagCloudList.get(0).addTag(displayTag);
			}
			else if(this.tagCloudList.size()==3) {
				if(tuple.getString(Fields.SENTIMENT).equals(POSITIVE_LABEL)) {
					this.tagCloudList.get(POS_INDEX).addTag(displayTag);
				}
				else if(tuple.getString(Fields.SENTIMENT).equals(NEGATIVE_LABEL)) {
					this.tagCloudList.get(NEG_INDEX).addTag(displayTag);
				}

				else if(tuple.getString(Fields.SENTIMENT).equals(NEUTRAL_LABEL)) {
					this.tagCloudList.get(NEUTRAL_INDEX).addTag(displayTag);
				}
				else { //Shouldn't happen
					throw new RuntimeException("Error while getting tag sentiment");
				}
			}
		}
		catch(IndexOutOfBoundsException e) {
			throw new RuntimeException("Mix of Tuples with and without sentiments");
		}
	}
	
	/** 
	 * Gets all the TagClouds
	 * @return List of all DisplayTagCloud held by this class
	 */
	public List<DisplayTagCloud<DisplayTag>> getAll() {
		List<DisplayTagCloud<DisplayTag>> tagCloudList = new LinkedList<DisplayTagCloud<DisplayTag>>();
		for (DisplayTagCloud<DisplayTag> tagCloud : this.tagCloudList) {
			if(tagCloud.getTagsList().size()>0)
				tagCloudList.add(tagCloud);
		}
		return tagCloudList;
	}
}
