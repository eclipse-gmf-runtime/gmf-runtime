/******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.core.runtime.IAdaptable;

/**
 * An adapter that allows for the setting of the object later.
 * 
 * @author cmahoney
 * @canBeSeenBy %partners
 */
public class ObjectAdapter
	implements IAdaptable {

	/** the object */
	private List theList = new ArrayList();

	/**
	 * Constructor for an ObjectAdapter.
	 */
	public ObjectAdapter() {
		super();
	}

	/**
	 * Constructor for an ObjectAdapter.
	 * @param object The object that can adapt.
	 */
	public ObjectAdapter(Object object) {
		super();
		assert null != object : "ObjectAdapter constructor received NULL as argument"; //$NON-NLS-1$
		setObject(object);
	}

	/**
	 * Constructor for an ObjectAdapter.
	 * @param list The list to adapt.
	 */
	public ObjectAdapter(List list) {
		super();
		assert null != list : "ObjectAdapter received NULL list as argument"; //$NON-NLS-1$
		theList.addAll(list);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		ListIterator li = theList.listIterator();
		while (li.hasNext()) {
			Object theObject = li.next();
			if (theObject != null
				&& adapter.isAssignableFrom(theObject.getClass()))
				return theObject;
		}
		return null;
	}

	/**
	 * Sets the object.
	 * 
	 * @param theObject
	 *            The object to set.
	 */
	public void setObject(Object theObject) {
		theList = new ArrayList();
		theList.add(theObject);
	}

}