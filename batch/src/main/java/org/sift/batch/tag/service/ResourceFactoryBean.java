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

import org.springframework.beans.factory.FactoryBean;
import org.trpr.platform.runtime.impl.config.FileLocator;

/**
 * <code>ResourceFactoryBean</code> is a Spring Factory Bean for getting the absolute path of a file located within 
 * the Trooper project. It uses {@link FileLocator} to find the file. It accepts the fileName and returns it's absolute
 * path.
 * 
 * @author devashishshankar
 * @version 1.0, 28th Feb, 2013
 */
public class ResourceFactoryBean implements FactoryBean<String> {

	private String resourceFileName;
	private String resourceFilePath=null;
	
	/**
	 * Interface method implementation. Returns the absolute file path as a String, null if file not found
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	@Override
	public String getObject() throws Exception {
		if(resourceFilePath == null) {
			this.resourceFilePath = FileLocator.findUniqueFile(this.resourceFileName).getAbsolutePath();
			return this.resourceFilePath;
		}
		return null;
	}

	/**
	 * Interface method implementation. Returns type of {@link String}
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	@Override
	public Class<?> getObjectType() {
		return String.class;
	}
	
	/**
	 * Interface method implementation.
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	@Override
	public boolean isSingleton() {
		return false;
	}

	/** Start Getter/Setter methods*/	
	public String getResourceFileName() {
		return resourceFileName;
	}

	public void setResourceFileName(String resourceFileName) {
		this.resourceFileName = resourceFileName;
	}
	/** End Getter/Setter methods*/
}
