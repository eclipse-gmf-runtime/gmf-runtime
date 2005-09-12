/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.type.core.requests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.gmf.runtime.emf.type.core.internal.l10n.ResourceManager;

/**
 * Request to duplicate a model element.
 * 
 * @author ldamus
 */
public class DuplicateElementsRequest
	extends AbstractEditCommandRequest {
	
	/**
	 * The common container of all of the elements to be duplicated.
	 */
	private EObject commonContainer;

	/**
	 * The elements to be duplicated.
	 */
	private List elementsToDuplicate;

	/**
	 * This will be populated with all the elements that are duplicated after
	 * the command executes. The key is the original element and the value is
	 * the new duplicated element. There may be more elements duplicated than
	 * the original list of elements passed in (e.g. contained elements) --
	 * these will appear in this map.
	 */
	private Map allDuplicatedElementsMap = new HashMap();

	/**
	 * The duplicate element. Will be <code>null</code> until this request has
	 * been answered.
	 */
	private EObject duplicate;

	/**
	 * Constructs a new request to duplicate a model element.
	 */
	public DuplicateElementsRequest() {

		this(null);
	}

	/**
	 * Constructs a new request to duplicate a model element.
	 * 
	 * @param elementToDuplicate
	 *            the element to be duplicated
	 */
	public DuplicateElementsRequest(List elementsToDuplicate) {

		super();
		this.elementsToDuplicate = elementsToDuplicate;
	}
	
	/**
	 * Returns a map of all duplicated elements. This will be populated with all
	 * the elements that are duplicated after the command executes. The key is
	 * the original element and the value is the new duplicated element. There
	 * may be more elements duplicated than the original list of elements passed
	 * in (e.g. contained elements) -- these will appear in this map.
	 * 
	 * @return Returns the allDuplicatedElementsMap.
	 */
	public Map getAllDuplicatedElementsMap() {
		return allDuplicatedElementsMap;
	}

	/**
	 * Returns the primary elements to be duplicated.
	 * 
	 * @return Returns the elements to be duplicated.
	 */
	public List getElementsToBeDuplicated() {
		return elementsToDuplicate;
	}
	/**
	 * Gets the duplicate. Will return <code>null</code> until the request has
	 * been answered.
	 * 
	 * @return the duplicate
	 */
	public EObject getDuplicate() {
		return duplicate;
	}

	/**
	 * Sets the duplicate.
	 * 
	 * @param duplicate
	 *            the duplicate
	 */
	public void setAllDuplicatedElementsMap(Map duplicatedElementsMap) {
		this.allDuplicatedElementsMap = duplicatedElementsMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.IEditCommandRequest#getElementsToEdit()
	 */
	public List getElementsToEdit() {
		return elementsToDuplicate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditCommandRequest#getDefaultLabel()
	 */
	protected String getDefaultLabel() {
		return ResourceManager.getInstance().getString(
			"Request.Label.Duplicate"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.IEditCommandRequest#getEditHelperContext()
	 */
	public Object getEditHelperContext() {
		
		if (commonContainer == null) {
			commonContainer = getLeastCommonContainer(getElementsToBeDuplicated());
		}
		return commonContainer;
	}
	
	/**
	 * Finds the first common container of a collection of objects, or <code>null</code> if
	 * there is not common container.
	 * 
	 * @param objects
	 *            the elements
	 * @return the least common container that containes all of the <code>objects</code>, or
	 * <code>null</code> if there is no common container.
	 */
	private static EObject getLeastCommonContainer(Collection objects) {

		EObject commonContainer = null;
		List prevContainers = new ArrayList();

		for (Iterator i = objects.iterator(); i.hasNext();) {
			EObject nextElement = (EObject) i.next();
			
			boolean found = false;
			List containers = new ArrayList();
			EObject container = nextElement;
			
			// Construct the list of containers for this next element.
			while (container != null) {
				containers.add(container);

				if (!found) {

					if ((prevContainers.isEmpty()) || (commonContainer == null)) {
						commonContainer = container;
						found = true;

					} else if ((prevContainers.contains(container))
						&& (contains(container, commonContainer))) {

						commonContainer = container;
						found = true;
					}
				}
				container = container.eContainer();
			}

			if (!found) {
				return null;
			}
			prevContainers = containers;
		}
		return commonContainer;
	}

	/**
	 * Checks if a model element is contained by another element, recursively.
	 * 
	 * @param container
	 *            the container element
	 * @param eObject
	 *            the element to be tested to see if it is contained in the
	 *            container
	 * @return <code>true</code> if <code>container</code> contains
	 *         <code>eObject</code>,<code>false</code> otherwise.
	 */
	private static boolean contains(EObject container, EObject eObject) {

		if (container == eObject) {
			return true;
			
		} else if ((container == null) || (eObject == null)) {
			return false;
			
		} else {
			return contains(container, eObject.eContainer());
		}
	}
}