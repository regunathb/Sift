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
package org.sift.tagcloud.test;

import org.sift.tagcloud.impl.service.ImageFilePersistenceService;
import org.sift.tagcloud.ui.DisplayTag;
import org.sift.tagcloud.ui.DisplayTagCloud;

/**
 * <code>TagCloudTest </code> is a test class for creating tag clouds
 * 
 * @author Regunath B
 * @version 1.0, 11 Apr, 2013 
 */
public class TagCloudTest {

	private static final String[][] LABELS_WEIGHTS = {
		{"sift","4"},
		{"creating","2"},
		{"nice","3"},
		{"project","1"},
		{"tags","5"},
	};
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public TagCloudTest() {
		DisplayTagCloud<DisplayTag> tagCloud = new DisplayTagCloud<DisplayTag>("siftsample");
		for (String[] labelWeight : LABELS_WEIGHTS) {
			DisplayTag tag = new DisplayTag(labelWeight[0],Integer.valueOf(labelWeight[1]));
			tagCloud.addTag(tag);
		}
		ImageFilePersistenceService imageWriter = new ImageFilePersistenceService();
		imageWriter.setTagCloudsDirectory("/Users/regunath.balasubramanian/Documents/junk/tagclouds");
		imageWriter.persistTagCloud(tagCloud);
	}
	
	public static void main(String[] args) {
		new TagCloudTest();
	}
}
