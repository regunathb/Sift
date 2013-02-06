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
package org.sift.tagcloud.impl.service;

import java.io.File;

import org.sift.tagcloud.Tag;
import org.sift.tagcloud.TagCloud;
import org.sift.tagcloud.impl.image.ImageWriterFactory;
import org.sift.tagcloud.spi.image.ImageFileWriter;
import org.sift.tagcloud.spi.service.PersistenceService;
import org.sift.tagcloud.ui.DisplayTag;
import org.sift.tagcloud.ui.DisplayTagCloud;

/**
 * The <code>FilePersistenceService</code> is an implementation of {@link PersistenceService} that writes the tag cloud as an image of suitable type
 * to the file system.
 * 
 * @author Regunath B
 * @version 1.0, 25 Jan 2013
 */
public class FilePersistenceService<T extends Tag, S extends TagCloud<T>> implements PersistenceService<T,S> {
	
	/** The image writer implementation, initialized to the default implementation */
	private ImageFileWriter<DisplayTag> imageWriter = ImageWriterFactory.getDefaultImageFileWriter();
	
	/** The absolute directory path where tag clouds will be stored*/
	private String tagCloudsDirectory;

	/**
	 * Persists the specified TagCloud as an image file on the file system. Uses the {@link TagCloud#getSubject()} as the file name
	 * @see org.sift.tagcloud.spi.service.PersistenceService#persistTagCloud(org.sift.tagcloud.TagCloud)
	 */
	@SuppressWarnings("unchecked")
	public void persistTagCloud(S tagCloud) throws RuntimeException {
		if (tagCloud.getSubject() == null || tagCloud.getSubject().trim().length() == 0) {
			throw new RuntimeException("Tag cloud's subject cannot be empty! File cannot be created with empty name.");
		}
		if (!DisplayTagCloud.class.isAssignableFrom(tagCloud.getClass())) {
			throw new RuntimeException("This service supports persisting only DisplayTagCloud instances. Specified TagCloud (Subject : " + tagCloud.getSubject() + ") is of type : " + tagCloud.getClass().getName());
		}
		
		// lay out the tag cloud
		tagCloud.layoutTagCloud();
		
		// write the tag cloud image 
		try {
			this.imageWriter.writeImageFile(this.getTagCloudsDirectory() + File.separator + tagCloud.getSubject() + ImageFileWriter.FILE_EXTENSION_SEP + this.getFileType(), (DisplayTagCloud<DisplayTag>)tagCloud);
		} catch (RuntimeException pe) {
			throw new RuntimeException("Error persisting tag cloud with subject : " + tagCloud.getSubject(),pe);
		}
	}

	/**
	 * Interface method implementation. WARNING : Not implemented and throws an {@link UnsupportedOperationException} to the effect
	 * @see org.sift.tagcloud.spi.service.PersistenceService#loadTagCloud(org.sift.tagcloud.TagCloud)
	 */
	public S loadTagCloud(S tagCloud) throws RuntimeException {
		throw new UnsupportedOperationException("Loading of tag clouds from image files is not supported!");
	}
	
	/** Start Getter/Setter methods */
	public String getFileType() {
		return imageWriter.getImageFileType();
	}
	/**
	 * Sets the file type for persistence to one of the supported types by this class. For example {@link ImageFileWriter#PNG}, 
	 * {@link ImageFileWriter#SVG}, {@link ImageFileWriter#POST_SCRIPT}
	 * @param fileType the valid file type supported by this class
	 * @throws RuntimeException in case of unsupported file types
	 */
	public void setFileType(String fileType) throws RuntimeException {
		this.imageWriter = ImageWriterFactory.getImageFileWriter(fileType);
	}
	public String getTagCloudsDirectory() {
		return this.tagCloudsDirectory;
	}
	public void setTagCloudsDirectory(String tagCloudsDirectory) {
		this.tagCloudsDirectory = tagCloudsDirectory;
	}		
	/** End Getter/Setter methods */

}
