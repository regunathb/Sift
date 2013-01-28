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
package org.sift.tagcloud.impl.image;

import org.sift.tagcloud.impl.image.png.PNGImageWriter;
import org.sift.tagcloud.impl.image.postscript.PostscriptImageWriter;
import org.sift.tagcloud.impl.image.svg.SVGImageWriter;
import org.sift.tagcloud.spi.image.ImageFileWriter;
import org.sift.tagcloud.ui.DisplayTag;
import org.trpr.platform.core.PlatformException;
import org.trpr.platform.core.spi.persistence.PersistenceException;


/**
 * The <code>ImageWriterFactory</code> is a simple factory implementation for {@link ImageFileWriter} implementations
 * 
 * @author Regunath B
 * @version 1.0, 28 Jan 2013
 */
public class ImageWriterFactory {

	/**
	 * Creates and returns a {@link ImageFileWriter} instance for the specified file type.
	 * @param fileType a valid file type as defined in {@link ImageFileWriter} such as {@link ImageFileWriter#PNG}
	 * @return ImageFileWriter instance for the specified file type
	 * @throws PlatformException in case of errors while creating an instance of specific ImageFileWriter type
	 */
	public static ImageFileWriter<DisplayTag> getImageFileWriter(String fileType) throws PlatformException {
		if (fileType.equalsIgnoreCase(ImageFileWriter.PNG)) {
			return new PNGImageWriter<DisplayTag>();
		}
		if (fileType.equalsIgnoreCase(ImageFileWriter.SVG)) {
			return new SVGImageWriter<DisplayTag>();
		}
		if (fileType.equalsIgnoreCase(ImageFileWriter.POST_SCRIPT)) {
			return new PostscriptImageWriter<DisplayTag>();
		}
		throw new PersistenceException("Unrecognized file type : " + fileType + ". Check JavaDoc of ImageFileWriter for supported types.");
	}
	
	/**
	 * Creates an instance of the default image writer type. Returns an instance of {@link PNGImageWriter}
	 * @return an instance of PNGImageWriter
	 * @throws PlatformException in case of errors while creating the default image writer
	 */
	public static ImageFileWriter<DisplayTag> getDefaultImageFileWriter() throws PlatformException {
		return new PNGImageWriter<DisplayTag>();
	}
	
}
