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
package org.sift.tagcloud.spi.service;

import org.sift.tagcloud.TagCloud;
import org.trpr.platform.core.spi.persistence.PersistenceException;


/**
 * The <code>PersistenceService</code> interface provides methods to persist tag clouds.
 * 
 * @author Regunath B
 * @version 1.0, 25 Jan 2013
 */
public interface PersistenceService {

	/**
	 * Persists the specified TagCloud into an underlying persistence store
	 * @param tagCloud the TagCloud to be persisted
	 * @throws PersistenceException in case of persistence errors 
	 */
	public void persistTagCloud(TagCloud tagCloud) throws PersistenceException;
	
	/**
	 * Loads the specified TagClous from the underlying persistence store
	 * @param tagCloud the TagCloud to be loaded
	 * @return the loaded TagCloud
	 * @throws PersistenceException in case of persistence errors 
	 */
	public TagCloud loadTagCloud(TagCloud tagCloud) throws PersistenceException;
	
}
