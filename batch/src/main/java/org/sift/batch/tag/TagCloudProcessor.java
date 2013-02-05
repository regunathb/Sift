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

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.sift.tagcloud.ui.DisplayTag;
import org.sift.tagcloud.ui.DisplayTagCloud;
import org.springframework.batch.item.ItemProcessor;

/**
 * The <code>TagCloudProcessor</code> is an implementation of the Spring Batch {@link ItemProcessor} that orders tags by descending order of their weights 
 * and uses only the first N tags as identified by {@link TagCloudProcessor#getMaxTags()}
 * 
 * @author Regunath B
 * @version 1.0, 31 Jan 2013
 */
public class TagCloudProcessor implements ItemProcessor<DisplayTagCloud<DisplayTag>,DisplayTagCloud<DisplayTag>>, Comparator<DisplayTag>{

	/** Default max number of tags to display*/
	private static final int MAX_TAGS = 75;
	
	/** The max number of tags to display */
	private int maxTags = MAX_TAGS;

	/**
	 * Interface method implementation. Sorts the tag values by descending order of weights and retains only the top ones that fall witnin
	 * {@link TagCloudProcessor#getMaxTags()}
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	public DisplayTagCloud<DisplayTag> process(DisplayTagCloud<DisplayTag> tagCloud) throws Exception {
		Collections.sort(tagCloud.getTagsList(), this);
		List<DisplayTag> finalDisplayTags = new LinkedList<DisplayTag>();
		int maxSize = Math.min(tagCloud.getTagsList().size(), this.getMaxTags());
		for (int i=0; i < maxSize; i++) {
			finalDisplayTags.add(tagCloud.getTagsList().get(i));
		}
		tagCloud.removeAllTags();
		for (DisplayTag tag : finalDisplayTags) {
			tagCloud.addTag(tag);
		}
		return tagCloud;
	}

	/**
	 * Interface method implementation. Does a descending compare using display tag weights
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(DisplayTag tag1, DisplayTag tag2) {
		return tag2.getWeight() - tag1.getWeight();
	}

	/** Getter/Setter methods */
	public int getMaxTags() {
		return this.maxTags;
	}
	public void setMaxTags(int maxTags) {
		this.maxTags = maxTags;
	}

}
