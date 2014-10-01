/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *   
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package org.air.shared.resources;

import java.util.Hashtable;

import org.air.shared.utils.StringUtils;

public class ResourceManager {
	
	private static ResourceManager Resource = null;
	
	private Hashtable<String, AbstractResource> mMapOfResources = new Hashtable<String, AbstractResource>(); 
	
	public synchronized void addToResources(String key, AbstractResource resource, boolean checkIfResourceExists) throws ResourceExistsException, ResourceInitializationException{
		if (mMapOfResources.containsKey(key) && checkIfResourceExists)
			throw new ResourceExistsException(StringUtils.format("Resource with name {0} already exists.", key));
	
		//else add it after loading it up.
		resource.load();
		mMapOfResources.put(key, resource);
	}
	
	public <T> T getResource(String key)
	{
		if (mMapOfResources.containsKey(key))
			return (T) mMapOfResources.get(key);
		else
			return null;
	}
	
	public static ResourceManager getInstance()
	{
		if (Resource == null)
			init();
		return Resource;
	}
	
	private static synchronized void init()
	{
		if (Resource == null)
		{
			Resource = new ResourceManager();
		}
	}
	
	private ResourceManager(){};
}
