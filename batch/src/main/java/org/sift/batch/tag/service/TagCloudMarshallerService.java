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
package org.sift.batch.tag.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.sift.batch.model.tagcloud.TagCloudModel;
import org.sift.batch.model.tagcloud.TagModel;
import org.sift.tagcloud.Tag;
import org.sift.tagcloud.TagCloud;
import org.sift.tagcloud.spi.service.PersistenceService;
import org.sift.tagcloud.ui.DisplayTag;
import org.sift.tagcloud.ui.DisplayTagCloud;
import org.springframework.beans.factory.InitializingBean;
import org.trpr.platform.integration.spi.marshalling.Marshaller;

/**
 * The <code>TagCloudMarshallerService</code> is an implementation of {@link PersistenceService} that marshalls a {@link TagCloud} to an output form
 * supported by the specified {@link Marshaller}
 * 
 * @author Regunath B
 * @version 1.0, 25 Jan 2013
 */
public class TagCloudMarshallerService <T extends Tag, S extends TagCloud<T>> implements PersistenceService<T,S>, InitializingBean {
	
	/** The file extension part map */
	public static final Map<String, String> FILE_EXTENSION_PARTS = new HashMap<String, String>();
	
	/** Initialize with types we recognize*/
	static {
		FILE_EXTENSION_PARTS.put("json",".json");
		FILE_EXTENSION_PARTS.put("xml",".xml");
	}

	/** The Marshaller implementation*/
	private Marshaller marshaller;
	
	/** The file extension part*/
	private String fileExtensionPart;
	
	/** The absolute directory path where tag clouds will be stored*/
	private String tagCloudsDirectory;

	/**
	 * Interface method implementation. Persists the specified TagCloud a marshalled file on the file system. Uses the {@link TagCloud#getSubject()} as the file name
	 * @see org.sift.tagcloud.spi.service.PersistenceService#persistTagCloud(org.sift.tagcloud.TagCloud)
	 */
	public void persistTagCloud(S tagCloud) throws RuntimeException {
		if (tagCloud.getSubject() == null || tagCloud.getSubject().trim().length() == 0) {
			throw new RuntimeException("Tag cloud's subject cannot be empty! File cannot be created with empty name.");
		}
		// write the tag cloud data by creating the TagCloudModel from the specified TagCloud 
		TagCloudModel tagCloudModel = new TagCloudModel();
		tagCloudModel.setSubject(tagCloud.getSubject());
	    for (T tag : tagCloud.getTagsList()) {
	    	TagModel tModel = new TagModel();
	    	tModel.setDisplayText(tag.getDisplayText());
	    	tModel.setWeight(tag.getWeight());
	    	tModel.setDescription(tag.getDescription());
	    	if (tag.getTagURL() != null) {
	    		tModel.setTagURL(tag.getTagURL().toExternalForm());
	    	}
	    	if (tag.getTagSourcesURL() != null) {
	    		tModel.setTagSourcesURL(tag.getTagSourcesURL().toExternalForm());
	    	}
	    	tModel.setTitle(tag.getTitle());
	    	tagCloudModel.getTags().add(tModel);
	    }
		
		try {
			this.marshallTagCloud(this.getTagCloudsDirectory() + File.separator + tagCloud.getSubject() + this.fileExtensionPart, tagCloudModel);
		} catch (RuntimeException pe) {
			throw new RuntimeException("Error persisting tag cloud with subject : " + tagCloud.getSubject(),pe);
		}
	}

	/**
	 * Interface method implementation. Loads the specified TagCloud from a marshalled file on the file system. Uses the {@link TagCloud#getSubject()} as the file name
	 * @see org.sift.tagcloud.spi.service.PersistenceService#loadTagCloud(org.sift.tagcloud.TagCloud)
	 */
	@SuppressWarnings("unchecked")
	public S loadTagCloud(S tagCloud) throws RuntimeException {
		File tagCloudDataFile = new File (this.getTagCloudsDirectory() + File.separator + tagCloud.getSubject() + this.fileExtensionPart);
		try {
			BufferedReader reader = new BufferedReader(new FileReader(tagCloudDataFile));
		    StringBuilder  stringBuilder = new StringBuilder();
		    String line = null;
		    while( ( line = reader.readLine() ) != null ) {
		        stringBuilder.append( line );
		    }
		    TagCloudModel tagCloudModel = (TagCloudModel) this.marshaller.unmarshal(stringBuilder.toString(), TagCloudModel.class);
		    tagCloud = (S)new DisplayTagCloud<DisplayTag>(tagCloudModel.getSubject());
		    for (TagModel tag : tagCloudModel.getTags()) {
		    	DisplayTag dTag = new DisplayTag(tag.getDisplayText(), tag.getWeight());
		    	dTag.setDescription(tag.getDescription());
		    	dTag.setTagURL(new URL(tag.getTagURL()));
		    	dTag.setTagSourcesURL(new URL(tag.getTagSourcesURL()));
		    	dTag.setTitle(tag.getTitle());
		    	tagCloud.addTag((T)dTag);
		    }
		    return tagCloud;
		} catch (Exception e) {
			throw new RuntimeException("Error loading tag cloud image for : " + tagCloud.getSubject() + " from file : " + tagCloudDataFile.getAbsolutePath(), e);
		}
	}

	/**
	 * Interface method implementation. Determine marshalled file extension part from class name of the specified Marshaller
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		for (String filePartIdentifier : FILE_EXTENSION_PARTS.keySet()) {
			if (this.marshaller.getClass().getName().toLowerCase().contains(filePartIdentifier)) {
				this.fileExtensionPart = FILE_EXTENSION_PARTS.get(filePartIdentifier);			
			}
		}
	}

	
	/**
	 * Helper method to write the tag cloud to the file system using the Marshaller
	 * @param filePath the absolute path to the data file
	 * @param tagCloudModel the TagCloudModel whose data is to be written to the specified file
	 * @throws RuntimeException in case of errors in writing the data file
	 */
	private void marshallTagCloud(String filePath, TagCloudModel tagCloudModel) throws RuntimeException {
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
		try {
			file.createNewFile();
			String data = this.marshaller.marshal(tagCloudModel);
			FileWriter writer = new FileWriter(file);
			writer.write(data);
			writer.flush();
			writer.close();			
		} catch (IOException e) {
			throw new RuntimeException("Error writing tag cloud image for : " + tagCloudModel.getSubject() + " to file : " + filePath, e);
		}		
	}
	
	/** Getter/Setter methods */
	public Marshaller getMarshaller() {
		return this.marshaller;
	}
	public void setMarshaller(Marshaller marshaller) {
		this.marshaller = marshaller;
	}
	public String getTagCloudsDirectory() {
		return this.tagCloudsDirectory;
	}
	public void setTagCloudsDirectory(String tagCloudsDirectory) {
		this.tagCloudsDirectory = tagCloudsDirectory;
	}

}
