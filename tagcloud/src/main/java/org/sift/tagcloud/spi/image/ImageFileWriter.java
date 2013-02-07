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
package org.sift.tagcloud.spi.image;

import java.io.File;
import java.io.IOException;

import org.sift.tagcloud.ui.DisplayTag;
import org.sift.tagcloud.ui.DisplayTagCloud;

/**
 * The <code>ImageFileWriter</code> interface provides methods to write display tag clouds to specific image file types.
 * @see {@link DisplayTagCloud} 
 * 
 * @author Regunath B
 * @version 1.0, 25 Jan 2013
 */
public abstract class ImageFileWriter<S extends DisplayTag> {

	/** Constants for common image file types */
	public static final String PNG = "png";
	public static final String SVG = "svg";
	public static final String POST_SCRIPT = "ps";
	
	/** The file extension separator char*/
	public static final String FILE_EXTENSION_SEP = ".";
	
	/** The file type extension supported by this image writer*/
	private String fileType;
	
	/** Constructor for this class*/
	public ImageFileWriter(String fileType) {
		this.fileType = fileType;
	}
	
	/**
	 * Returns the image file type that this image writer produces
	 * @return the file type extension.
	 */
	public String getImageFileType() {
		return this.fileType;
	}
	
	/**
	 * Writes an image representation of the specified DisplayTagCloud to the specified file path.
	 * @param filePath the absolute path to the image file
	 * @param displayTagCloud the DisplayTagCloud whose image is to be written to the specified file
	 * @throws RuntimeException in case of errors in writing the image file
	 */
	public void writeImageFile(String filePath, DisplayTagCloud<S> displayTagCloud) throws RuntimeException {
		if (!filePath.endsWith(this.getImageFileType())) {
			throw new RuntimeException("Invalid file path : " + filePath + ". Supported type is : " + this.getImageFileType());
		}
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
		try {
			file.createNewFile();
		} catch (IOException e) {
			throw new RuntimeException("Error writing tag cloud image for : " + displayTagCloud.getSubject() + " to file : " + filePath, e);
		}
		writeImageContents(file, displayTagCloud);
	}
	
	/**
	 * Writes the display tag cloud image to the file specified
	 * @param file the File to write the tag cloud image to
	 * @param displayTagCloud the DisplayTagCloud whose image is to be written to the specified file
	 * @throws PlatformException in case of errors in writing the image file
	 */
	protected abstract void writeImageContents(File file, DisplayTagCloud<S> displayTagCloud) throws RuntimeException;
	
}
